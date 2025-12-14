import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import static java.lang.foreign.ValueLayout.*;

public class SigFFM {
    public static void main(String[] args) throws Throwable {

        System.load("libsig.so");

        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.loaderLookup();

        System.out.println("=== Test FFM ===");
        double[] data = {1, 2, 3, 4, 5};
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment seg = arena.allocateFrom(JAVA_DOUBLE, data);

            MethodHandle mh = linker.downcallHandle(
                    lookup.find("avg").orElseThrow(),
                    FunctionDescriptor.of(
                            JAVA_DOUBLE,
                            ADDRESS,
                            JAVA_INT
                    )
            );
            double avg = (double) mh.invoke(seg, data.length);
            System.out.println("avg = " + avg);
        } catch (Exception e) {
            System.err.println("Erreur avg: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("\n=== Test convolution ===");
        double[] signal = {1, 2, 3, 4, 5};
        double[] kernel = {1, 1};
        try (Arena arena = Arena.ofConfined()) {
            var s = arena.allocateFrom(JAVA_DOUBLE, signal);
            var k = arena.allocateFrom(JAVA_DOUBLE, kernel);
            var out = arena.allocateFrom(JAVA_DOUBLE, signal.length);

            MethodHandle mh = linker.downcallHandle(
                    lookup.find("convolve").orElseThrow(),
                    FunctionDescriptor.ofVoid(
                            ADDRESS, JAVA_INT,
                            ADDRESS, JAVA_INT,
                            ADDRESS
                    )
            );

            mh.invoke(s, signal.length, k, kernel.length, out);
            double[] res = out.toArray(JAVA_DOUBLE);
            System.out.println("Résultat convolution: " + Arrays.toString(res));
        } catch (Exception e) {
            System.err.println("Erreur convolution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

//=== Test FFM ===
//avg = 3.0
//
//=== Test convolution ===
//Résultat convolution: [1.0]

