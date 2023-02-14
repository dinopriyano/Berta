#include <jni.h>
#include <string>

extern "C" jobject Java_com_dino_core_utils_NdkUtils_adrNativeValues(
        JNIEnv* env,
        jobject /* this */) {
    jclass mapClass = env->FindClass("java/util/HashMap");
    if(mapClass == NULL) {
        return NULL;
    }
    jmethodID init = env->GetMethodID(mapClass, "<init>", "(I)V");
    jmethodID put = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    jsize map_len = 4;
    jobject hashMap = env->NewObject(mapClass, init, map_len);
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("BASE_API_URL"), env->NewStringUTF("https://newsapi.org/"));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("API_KEY"), env->NewStringUTF("")); // put your News Api Key here

    return hashMap;
}