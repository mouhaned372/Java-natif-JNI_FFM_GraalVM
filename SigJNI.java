public class SigJNI {
static {
System.loadLibrary("sig");
}
public static native double hyp(double a, double b);
public static native double avg(double[] data);
public static native double var(double[] data);
public static native double[] convolve(double[] signal, double[] kernel);
}
