package demo.parallel;

import java.lang.reflect.Field;

/**
 * Unit tests for new operations in {@link Complex}.
 * Run with: java demo.parallel.ComplexTest
 */
public class ComplexTest {

    private static final double EPS = 1e-9;

    public static void main(String[] args) {
        testMinus();
        testScale();
        testCopyCreatesIndependentObject();
        System.out.println("All Complex tests passed.");
    }

    private static void testMinus() {
        Complex value = new Complex(3.5, -2.0);
        value.minus(new Complex(1.5, 4.0));

        assertClose(real(value), 2.0, "minus should update real part");
        assertClose(imag(value), -6.0, "minus should update imaginary part");
    }

    private static void testScale() {
        Complex value = new Complex(-2.0, 4.5);
        value.scale(-0.5);

        assertClose(real(value), 1.0, "scale should multiply real part");
        assertClose(imag(value), -2.25, "scale should multiply imaginary part");
    }

    private static void testCopyCreatesIndependentObject() {
        Complex source = new Complex(2.0, 3.0);
        Complex copy = source.copy();
        copy.minus(new Complex(1.0, 1.0)).scale(2.0);

        // Source must remain unchanged after mutating copy.
        assertClose(real(source), 2.0, "copy should not mutate source real part");
        assertClose(imag(source), 3.0, "copy should not mutate source imaginary part");

        // Copy must contain transformed values.
        assertClose(real(copy), 2.0, "copy should keep independent real value");
        assertClose(imag(copy), 4.0, "copy should keep independent imaginary value");
    }

    private static double real(Complex value) {
        return getField(value, "re");
    }

    private static double imag(Complex value) {
        return getField(value, "im");
    }

    private static double getField(Complex value, String fieldName) {
        try {
            Field field = Complex.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getDouble(value);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError("Cannot access field: " + fieldName, ex);
        }
    }

    private static void assertClose(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > EPS) {
            throw new AssertionError(
                    message + ", expected: " + expected + ", actual: " + actual);
        }
    }
}
