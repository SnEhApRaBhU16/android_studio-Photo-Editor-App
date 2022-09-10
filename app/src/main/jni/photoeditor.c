#include<jni.h>
//b,g,r
JNIEXPORT void JNICALL
Java_com_example_miniproj_Addframe_red(JNIEnv *env, jclass clazz, jintArray pixels_,
                                                 jint width, jint height) {
    // TODO: implement red()
    jint *pixels=(*env)->GetIntArrayElements(env,pixels_,NULL);
    unsigned char *colors=(unsigned char *)pixels;
    int pixelCount=width*height*4;
    int i=0;
    while(i<pixelCount){

       if(colors[i+2]<250){
           colors[i+2]+=3;
       }
       i+=4;
    }
    (*env)->ReleaseIntArrayElements(env,pixels_,pixels,0);
}

JNIEXPORT void JNICALL
Java_com_example_miniproj_Addframe_green(JNIEnv *env, jclass clazz, jintArray pixels_, jint width,
                                         jint height) {
    jint *pixels=(*env)->GetIntArrayElements(env,pixels_,NULL);
    unsigned char *colors=(unsigned char *)pixels;
    // TODO: implement green()
    int pixelCount=width*height*4;
    int i=0;
    while(i<pixelCount){

        if(colors[i+1]<250){
            colors[i+1]+=3;
        }
        i+=4;
    }
    (*env)->ReleaseIntArrayElements(env,pixels_,pixels,0);
}

JNIEXPORT void JNICALL
Java_com_example_miniproj_Addframe_blue(JNIEnv *env, jclass clazz, jintArray pixels_, jint width,
                                        jint height) {
    jint *pixels=(*env)->GetIntArrayElements(env,pixels_,NULL);
    // TODO: implement blue()
    unsigned char *colors=(unsigned char *)pixels;
    int pixelCount=width*height*4;
    int i=0;
    while(i<pixelCount){
        if(colors[i]<250){
            colors[i]+=3;
        }
        i+=4;
    }
    (*env)->ReleaseIntArrayElements(env,pixels_,pixels,0);
}