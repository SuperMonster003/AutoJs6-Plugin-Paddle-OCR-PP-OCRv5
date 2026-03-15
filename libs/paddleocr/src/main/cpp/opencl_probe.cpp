#include <dlfcn.h>
#include <jni.h>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "OpenCLProbe", __VA_ARGS__)

extern "C" JNIEXPORT jint JNICALL
Java_com_baidu_paddle_lite_ocr_OpenCLProbe_nativeProbeOpenCL(JNIEnv*, jclass) {
    void* h = dlopen("libOpenCL.so", RTLD_NOW | RTLD_LOCAL);
    if (!h) {
        LOGI("dlopen libOpenCL.so failed");
        return -1;
    }
    using PFN_clGetPlatformIDs = int (*)(unsigned, void*, unsigned*);
    auto clGetPlatformIDs = (PFN_clGetPlatformIDs)dlsym(h, "clGetPlatformIDs");
    if (!clGetPlatformIDs) { dlclose(h); LOGI("dlsym clGetPlatformIDs failed"); return -2; }

    unsigned num = 0;
    int st = clGetPlatformIDs(0u, nullptr, &num);
    dlclose(h);
    if (st != 0 /* CL_SUCCESS */) { LOGI("clGetPlatformIDs status=%d", st); return -3; }
    LOGI("OpenCL platforms = %u", num);
    return (jint)num;
}
