#!/bin/bash
echo "========================================================"
echo "          TEST COMPLET - TP7 JAVA NATIF"
echo "========================================================"
echo ""

echo "📊 1. COMPILATION BIBLIOTHÈQUE C"
echo "   Compilation de libmath.so..."
gcc -shared -fPIC -o lib/libmath.so src/c/mathlib.c -lm
if [ $? -eq 0 ]; then
    echo "   ✅ libmath.so créé"
else
    echo "   ❌ Erreur compilation C"
    exit 1
fi
echo ""

echo "🔗 2. TEST JNI"
echo "   Compilation JNI..."
cd src/jni
javac TestJNI.java
javac -h . TestJNI.java
gcc -shared -fPIC -I/usr/lib64/jvm/java-25-openjdk/include \
    -I/usr/lib64/jvm/java-25-openjdk/include/linux \
    -o ../../lib/libtestjni.so TestJNI.c ../../lib/libmath.so -lm
cd ../..
echo "   Exécution JNI..."
java -Djava.library.path=lib -cp src/jni TestJNI
echo ""

echo "🚀 3. TEST FFM (Java 25)"
echo "   Compilation FFM..."
cd src/ffm
javac --enable-preview --release 25 MathFFM.java
echo "   Exécution FFM..."
java --enable-preview --enable-native-access=ALL-UNNAMED MathFFM
cd ../..
echo ""

echo "⚡ 4. COMPILATION NATIVE"
echo "   Création de l'exécutable natif..."
if command -v native-image &> /dev/null; then
    native-image --enable-preview --enable-native-access=ALL-UNNAMED \
        -H:Name=bin/tp7-native-final \
        -cp src/ffm MathFFM
    echo ""
    echo "   Exécution native:"
    ./bin/tp7-native-final
    echo ""
    echo "   ⏱️  Benchmark démarrage:"
    echo -n "   Native: "
    { time ./bin/tp7-native-final >/dev/null 2>&1; } 2>&1 | grep real | awk '{print $2}'
else
    echo "   ⚠️  GraalVM native-image non installé"
    echo "   Installation: gu install native-image"
fi
echo ""
echo "========================================================"
echo "✅ MINI-PROJET TP7 COMPLET"
echo "========================================================"