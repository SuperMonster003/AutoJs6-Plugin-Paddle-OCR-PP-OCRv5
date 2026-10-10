<#
.SYNOPSIS
Rebuilds libopencv_java4.so for the OpenCV 4.8.0 Java AAR of this plugin.

.DESCRIPTION
Clones the upstream OpenCV 4.8.0 tag, verifies its tag object and commit, builds the
opencv_java target for each ABI with Android NDK r28c, CMake 3.22.1 and 16 KB ELF segment
alignment, strips the result and injects it into libs/org-opencv-4_8_0/opencv-4.8.0.aar
with tools/opencv/opencv_java_aar.py, which also rewrites the provenance record.

Rebuilt libraries are functionally equivalent to the recorded ones; absolute build paths
are embedded by the upstream sources, so byte-identical output is not expected. Verify the
plugin on devices after a rebuild.

.EXAMPLE
pwsh -File tools/opencv/build_opencv_java.ps1 -JavaHome E:\.java\jdk-17.0.12
#>
#Requires -Version 7.0
[CmdletBinding()]
param(
    [string]$AndroidSdk,
    [string]$JavaHome,
    [string]$PythonExecutable,
    [string]$WorkDirectory,
    [string[]]$Abis = @("arm64-v8a", "armeabi-v7a", "x86", "x86_64"),
    [switch]$ReuseSource,
    [switch]$SkipInject
)

$ErrorActionPreference = "Stop"
$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDirectory = Resolve-Path (Join-Path $scriptDirectory "..\..")

if ([string]::IsNullOrWhiteSpace($AndroidSdk)) {
    $localProperties = Join-Path $projectDirectory "local.properties"
    if (Test-Path -LiteralPath $localProperties) {
        $sdkLine = Get-Content -LiteralPath $localProperties | Where-Object { $_ -match '^sdk\.dir=' } | Select-Object -First 1
        if ($sdkLine) {
            $AndroidSdk = $sdkLine.Substring("sdk.dir=".Length) -replace '\\:', ':' -replace '\\\\', '\'
        }
    }
}
if ([string]::IsNullOrWhiteSpace($AndroidSdk)) { throw "Pass -AndroidSdk or configure sdk.dir in local.properties" }
$AndroidSdk = (Resolve-Path -LiteralPath $AndroidSdk).Path
if ([string]::IsNullOrWhiteSpace($WorkDirectory)) { $WorkDirectory = Join-Path $projectDirectory "build\opencv-java-ndk28" }
$WorkDirectory = [System.IO.Path]::GetFullPath($WorkDirectory)
$sourceDirectory = Join-Path $WorkDirectory "opencv-4.8.0"
$outputDirectory = Join-Path $WorkDirectory "native-libs"
$ndkDirectory = Join-Path $AndroidSdk "ndk\28.2.13676358"
$cmakeBin = Join-Path $AndroidSdk "cmake\3.22.1\bin"
$expectedTagObject = "53296de62872b5e7d042ddffb49679fbdcca99f6"
$expectedCommit = "f9a59f2592993d3dcc080e495f4f5e02dd8ec7ef"

$ndkProperties = Join-Path $ndkDirectory "source.properties"
if (-not (Test-Path -LiteralPath $ndkProperties) -or
    (Get-Content -LiteralPath $ndkProperties -Raw) -notmatch '(?m)^Pkg\.Revision\s*=\s*28\.2\.13676358\s*$') {
    throw "Android NDK 28.2.13676358 is not installed under $AndroidSdk"
}
$cmakeVersion = & (Join-Path $cmakeBin "cmake.exe") --version 2>&1
if ($LASTEXITCODE -ne 0 -or $cmakeVersion[0] -notmatch '^cmake version 3\.22\.1(?:[-+][^\s]+)?$') {
    throw "Expected Android SDK CMake 3.22.1 under $cmakeBin"
}
if ([string]::IsNullOrWhiteSpace($JavaHome)) { $JavaHome = $env:JAVA_HOME_17 }
if ([string]::IsNullOrWhiteSpace($JavaHome) -or -not (Test-Path -LiteralPath (Join-Path $JavaHome "bin\java.exe"))) {
    throw "Pass a JDK 17 installation with -JavaHome or JAVA_HOME_17"
}
$JavaHome = (Resolve-Path -LiteralPath $JavaHome).Path
if (((& (Join-Path $JavaHome "bin\java.exe") -version 2>&1) -join "`n") -notmatch 'version "17\.') {
    throw "OpenCV Java bindings must be configured with JDK 17: $JavaHome"
}
if ([string]::IsNullOrWhiteSpace($PythonExecutable)) { $PythonExecutable = (Get-Command python -ErrorAction Stop).Source }
$PythonExecutable = (Resolve-Path -LiteralPath $PythonExecutable).Path

New-Item -ItemType Directory -Force -Path $WorkDirectory | Out-Null
if (-not (Test-Path -LiteralPath (Join-Path $sourceDirectory ".git"))) {
    & git -c core.longpaths=true clone --branch 4.8.0 --depth 1 https://github.com/opencv/opencv.git $sourceDirectory
    if ($LASTEXITCODE -ne 0) { throw "Unable to clone OpenCV 4.8.0" }
} elseif (-not $ReuseSource) {
    throw "Source already exists at $sourceDirectory; pass -ReuseSource after verifying it"
}
if ((& git -C $sourceDirectory rev-parse HEAD).Trim() -ne $expectedCommit) { throw "Unexpected OpenCV commit in $sourceDirectory" }
if ((& git -C $sourceDirectory rev-parse "4.8.0^{tag}").Trim() -ne $expectedTagObject) { throw "Unexpected OpenCV 4.8.0 tag object" }
if (& git -C $sourceDirectory status --porcelain --untracked-files=no) { throw "OpenCV source has tracked modifications" }

$abiConfigurations = @{
    "arm64-v8a" = @{ CMakeAbi = "arm64-v8a"; Ipp = "OFF" }
    "armeabi-v7a" = @{ CMakeAbi = "armeabi-v7a with NEON"; Ipp = "OFF" }
    "x86" = @{ CMakeAbi = "x86"; Ipp = "ON" }
    "x86_64" = @{ CMakeAbi = "x86_64"; Ipp = "ON" }
}
$oldPath, $oldJavaHome, $oldNdk = $env:PATH, $env:JAVA_HOME, $env:ANDROID_NDK
try {
    $env:PATH = "$JavaHome\bin;$cmakeBin;$oldPath"
    $env:JAVA_HOME = $JavaHome
    $env:ANDROID_HOME = $AndroidSdk
    $env:ANDROID_SDK = $AndroidSdk
    $env:ANDROID_NDK = $ndkDirectory
    foreach ($abi in $Abis) {
        $configuration = $abiConfigurations[$abi]
        if ($null -eq $configuration) { throw "Unsupported ABI: $abi" }
        $binaryDirectory = Join-Path $WorkDirectory "build-$abi"
        $logFile = Join-Path $WorkDirectory "build-$abi.log"
        # Keep these options in sync with BUILD in tools/opencv/opencv_java_aar.py.
        $cmakeArguments = @(
            "-S", $sourceDirectory, "-B", $binaryDirectory, "-G", "Ninja",
            "-DCMAKE_MAKE_PROGRAM=$cmakeBin\ninja.exe",
            "-DCMAKE_TOOLCHAIN_FILE=$ndkDirectory\build\cmake\android.toolchain.cmake",
            "-DCMAKE_BUILD_TYPE=Release",
            "-DCMAKE_SHARED_LINKER_FLAGS=-Wl,-z,max-page-size=16384",
            "-DINSTALL_CREATE_DISTRIB=ON", "-DWITH_OPENCL=OFF", "-DBUILD_KOTLIN_EXTENSIONS=OFF",
            "-DBUILD_ANDROID_PROJECTS=ON", "-DBUILD_JAVA=ON", "-DWITH_IPP=$($configuration.Ipp)", "-DWITH_TBB=ON",
            "-DBUILD_EXAMPLES=OFF", "-DBUILD_TESTS=OFF", "-DBUILD_PERF_TESTS=OFF", "-DBUILD_DOCS=OFF",
            "-DBUILD_ANDROID_EXAMPLES=OFF", "-DINSTALL_ANDROID_EXAMPLES=OFF",
            "-DANDROID_STL=c++_shared", "-DANDROID_ABI=$($configuration.CMakeAbi)", "-DANDROID_TOOLCHAIN=clang",
            "-DANDROID_NATIVE_API_LEVEL=24", "-DANDROID_COMPILE_SDK_VERSION=32", "-DANDROID_TARGET_SDK_VERSION=32",
            "-DANDROID_MIN_SDK_VERSION=24", "-DANDROID_GRADLE_PLUGIN_VERSION=7.3.1", "-DGRADLE_VERSION=7.5.1",
            "-DKOTLIN_PLUGIN_VERSION=1.5.20",
            "-DPYTHON_DEFAULT_EXECUTABLE=$PythonExecutable", "-DPYTHON3_EXECUTABLE=$PythonExecutable"
        )
        $configureOutput = & (Join-Path $cmakeBin "cmake.exe") @cmakeArguments 2>&1
        $configureOutput | Set-Content -LiteralPath $logFile -Encoding utf8
        if ($LASTEXITCODE -ne 0) { throw "OpenCV CMake configuration failed for $abi; see $logFile" }
        if (($configureOutput -join "`n") -notmatch 'Java wrappers:\s+YES') { throw "OpenCV Java wrappers are disabled for $abi; see $logFile" }
        & (Join-Path $cmakeBin "cmake.exe") --build $binaryDirectory --target opencv_java --parallel *>> $logFile
        if ($LASTEXITCODE -ne 0) { throw "OpenCV JNI build failed for $abi; see $logFile" }
        $library = Join-Path $binaryDirectory "jni\$abi\libopencv_java4.so"
        if (-not (Test-Path -LiteralPath $library)) { throw "OpenCV JNI output is missing for ${abi}: $library" }
        $destination = Join-Path $outputDirectory "$abi\libopencv_java4.so"
        New-Item -ItemType Directory -Force -Path (Split-Path -Parent $destination) | Out-Null
        Copy-Item -LiteralPath $library -Destination $destination -Force
        & (Join-Path $ndkDirectory "toolchains\llvm\prebuilt\windows-x86_64\bin\llvm-strip.exe") $destination
        if ($LASTEXITCODE -ne 0) { throw "Unable to strip OpenCV JNI for $abi" }
        Write-Host "Built $destination"
    }
} finally {
    $env:PATH, $env:JAVA_HOME, $env:ANDROID_NDK = $oldPath, $oldJavaHome, $oldNdk
}

if ($SkipInject) {
    Write-Host "Skipped AAR injection; libraries are in $outputDirectory"
    return
}
& $PythonExecutable (Join-Path $scriptDirectory "opencv_java_aar.py") replace --native-dir $outputDirectory
if ($LASTEXITCODE -ne 0) { throw "Unable to inject the rebuilt libraries into the OpenCV AAR" }
