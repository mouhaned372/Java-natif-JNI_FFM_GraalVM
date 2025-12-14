#include <math.h>
#include <stdlib.h>
#include <string.h>

double avg(double *data, int n) {
    if (n <= 0) return 0.0;
    double s = 0;
    for(int i = 0; i < n; i++) s += data[i];
    return s / n;
}

double var(double *data, int n) {
    if (n <= 1) return 0.0;
    double m = avg(data, n);
    double s = 0;
    for(int i = 0; i < n; i++) {
        double d = data[i] - m;
        s += d * d;
    }
    return s / n;
}

int compare_double(const void *a, const void *b) {
    double x = *(double*)a;
    double y = *(double*)b;
    if (x < y) return -1;
    if (x > y) return 1;
    return 0;
}

double median(double *data, int n) {
    if (n <= 0) return 0.0;
    double *copy = malloc(n * sizeof(double));
    memcpy(copy, data, n * sizeof(double));
    qsort(copy, n, sizeof(double), compare_double);
    double result = (n % 2 == 0) ?
        (copy[n/2 - 1] + copy[n/2]) / 2.0 : copy[n/2];
    free(copy);
    return result;
}

void convolve(const double *signal, int n,
              const double *kernel, int k,
              double *out) {
    for(int i = 0; i < n; i++) out[i] = 0.0;
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < k; j++) {
            int idx = i - j;
            if(idx >= 0) out[i] += signal[idx] * kernel[j];
        }
    }
}

void mean_filter(const double *signal, int n,
                 int window_size, double *out) {
    if (window_size % 2 == 0) window_size++;
    int half = window_size / 2;
    for(int i = 0; i < n; i++) {
        double sum = 0.0; int count = 0;
        for(int j = -half; j <= half; j++) {
            int idx = i + j;
            if(idx >= 0 && idx < n) {
                sum += signal[idx]; count++;
            }
        }
        out[i] = (count > 0) ? sum / count : 0.0;
    }
}

double distance(double x1, double y1, double x2, double y2) {
    double dx = x2 - x1, dy = y2 - y1;
    return sqrt(dx*dx + dy*dy);
}

double angle(double x1, double y1, double x2, double y2) {
    double dx = x2 - x1, dy = y2 - y1;
    return atan2(dy, dx);
}

double dot_product(double *v1, double *v2, int n) {
    double result = 0.0;
    for(int i = 0; i < n; i++) result += v1[i] * v2[i];
    return result;
}