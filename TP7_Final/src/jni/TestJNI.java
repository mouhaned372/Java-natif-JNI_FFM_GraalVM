public class TestJNI {
    static {
        System.load("/home/mouhaned/Téléchargements/TP7/TP7_Final/lib/libmath.so");
    }

    public static native double avg(double[] data);
    public static native double var(double[] data);
    public static native double median(double[] data);
    public static native double distance(double x1, double y1, double x2, double y2);
    public static native double angle(double x1, double y1, double x2, double y2);

    public static void main(String[] args) {
        double[] data = {1.0, 2.0, 3.0, 4.0, 5.0};
        System.out.println("Test JNI:");
        System.out.println("Moyenne: " + avg(data));
        System.out.println("Variance: " + var(data));
        System.out.println("Médiane: " + median(data));
        System.out.println("Distance (0,0)-(3,4): " + distance(0, 0, 3, 4));
        System.out.println("Angle (0,0)-(1,1): " + angle(0, 0, 1, 1));
    }
}