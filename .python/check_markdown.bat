@echo off
setlocal
pushd "%~dp0.."
python .python/generate_markdown.py --check
set "RESULT=%ERRORLEVEL%"
popd
exit /b %RESULT%
