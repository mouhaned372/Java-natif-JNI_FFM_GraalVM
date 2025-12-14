#include <jni.h>
#include "TestJNI.h"
#include <math.h>
#include <stdlib.h>
#include <string.h>

JNIEXPORT jdouble JNICALL Java_TestJNI_avg
  (JNIEnv *env, jclass cls, jdoubleArray arr) {
    jint n = (*env)->GetArrayLength(env, arr);
    jdouble *ptr = (*env)->GetDoubleArrayElements(env, arr, NULL);

    double sum = 0.0;
    for(int i = 0; i < n; i++) sum += ptr[i];
    double result = sum / n;

    (*env)->ReleaseDoubleArrayElements(env, arr, ptr, 0);
    return result;
}

JNIEXPORT jdouble JNICALL Java_TestJNI_var
  (JNIEnv *env, jclass cls, jdoubleArray arr) {
    jint n = (*env)->GetArrayLength(env, arr);
    jdouble *ptr = (*env)->GetDoubleArrayElements(env, arr, NULL);

    double sum = 0.0;
    for(int i = 0; i < n; i++) sum += ptr[i];
    double avg = sum / n;

    double var_sum = 0.0;
    for(int i = 0; i < n; i++) {
        double diff = ptr[i] - avg;
        var_sum += diff * diff;
    }

    (*env)->ReleaseDoubleArrayElements(env, arr, ptr, 0);
    return var_sum / n;
}

int compare_double(const void *a, const void *b) {
    double x = *(double*)a;
    double y = *(double*)b;
    if (x < y) return -1;
    if (x > y) return 1;
    return 0;
}

JNIEXPORT jdouble JNICALL Java_TestJNI_median
  (JNIEnv *env, jclass cls, jdoubleArray arr) {
    jint n = (*env)->GetArrayLength(env, arr);
    jdouble *ptr = (*env)->GetDoubleArrayElements(env, arr, NULL);

    double *copy = malloc(n * sizeof(double));
    memcpy(copy, ptr, n * sizeof(double));
    qsort(copy, n, sizeof(double), compare_double);

    double result;
    if (n % 2 == 0) {
        result = (copy[n/2 - 1] + copy[n/2]) / 2.0;
    } else {
        result = copy[n/2];
    }

    free(copy);
    (*env)->ReleaseDoubleArrayElements(env, arr, ptr, 0);
    return result;
}

JNIEXPORT jdouble JNICALL Java_TestJNI_distance
  (JNIEnv *env, jclass cls, jdouble x1, jdouble y1, jdouble x2, jdouble y2) {
    double dx = x2 - x1;
    double dy = y2 - y1;
    return sqrt(dx*dx + dy*dy);
}

JNIEXPORT jdouble JNICALL Java_TestJNI_angle
  (JNIEnv *env, jclass cls, jdouble x1, jdouble y1, jdouble x2, jdouble y2) {
    double dx = x2 - x1;
    double dy = y2 - y1;
    return atan2(dy, dx);
}