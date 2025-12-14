import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import static java.lang.foreign.ValueLayout.*;

public class MathFFM {
    public static void main(String[] args) throws Throwable {
        System.load("/home/mouhaned/Téléchargements/TP7/TP7_Final/lib/libmath.so");
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.loaderLookup();

        double[] data = {1.0, 2.0, 3.0, 4.0, 5.0};

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment seg = arena.allocateFrom(JAVA_DOUBLE, data);

            MethodHandle avgHandle = linker.downcallHandle(
                    lookup.find("avg").orElseThrow(),
                    FunctionDescriptor.of(JAVA_DOUBLE, ADDRESS, JAVA_INT)
            );
            double avg = (double) avgHandle.invoke(seg, data.length);
            System.out.println("FFM - Moyenne: " + avg);

            MethodHandle varHandle = linker.downcallHandle(
                    lookup.find("var").orElseThrow(),
                    FunctionDescriptor.of(JAVA_DOUBLE, ADDRESS, JAVA_INT)
            );
            double var = (double) varHandle.invoke(seg, data.length);
            System.out.println("FFM - Variance: " + var);

            MethodHandle medianHandle = linker.downcallHandle(
                    lookup.find("median").orElseThrow(),
                    FunctionDescriptor.of(JAVA_DOUBLE, ADDRESS, JAVA_INT)
            );
            double median = (double) medianHandle.invoke(seg, data.length);
            System.out.println("FFM - Médiane: " + median);
        }

        double[] signal = {1, 2, 3, 4, 5};
        double[] kernel = {0.5, 0.5};

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment s = arena.allocateFrom(JAVA_DOUBLE, signal);
            MemorySegment k = arena.allocateFrom(JAVA_DOUBLE, kernel);
            MemorySegment out = arena.allocateFrom(JAVA_DOUBLE, signal.length);

            MethodHandle convolveHandle = linker.downcallHandle(
                    lookup.find("convolve").orElseThrow(),
                    FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT, ADDRESS, JAVA_INT, ADDRESS)
            );
            convolveHandle.invoke(s, signal.length, k, kernel.length, out);

            double[] result = out.toArray(JAVA_DOUBLE);
            System.out.println("FFM - Convolution: " + Arrays.toString(result));
        }
    }
}