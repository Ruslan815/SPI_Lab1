import java.util.*;

public class Lab2 {
    private static final Random rnd = new Random();
    private static int generatedAxis;

    public static double[] generateQBit() {
        int prob = rnd.nextInt(4);
        double[] qBit = new double[2];
        double coefficient = 1 / Math.sqrt(2);
        switch (prob) {
            case 0:
                generatedAxis = 0;
                qBit = new double[]{1, 0};
                break;
            case 1:
                generatedAxis = 0;
                qBit = new double[]{0, 1};
                break;
            case 2:
                generatedAxis = 1;
                qBit = new double[]{coefficient, coefficient};
                break;
            case 3:
                generatedAxis = 1;
                qBit = new double[]{coefficient, -coefficient};
                break;
        }
        System.out.println("Generated qBit: " + Arrays.toString(qBit));
        return qBit;
    }

    public static int chooseMeter(double[] qBit) {
        int meterType = rnd.nextInt(2); // 0 - classic, 1 - rotated (Hadamard)
        System.out.println("Meter type(0-classic, 1-rotated): " + meterType);

        // Meter QBits
        double coefficient = 1 / Math.sqrt(2);
        double[] basicQBitZero = {1, 0}; // |0> = [1, 0] = x
        double[] basicQBitOne = {0, 1}; // |1> = [0, 1] = y
        double[] rotatedQBitZero = {coefficient, coefficient};  // |0>' = [0.7, 0.7]
        double[] rotatedQBitOne = {coefficient, -coefficient}; // |1>' = [0.7, -0.7]

        // Get passed qBit values
        double a0 = qBit[0];
        double a1 = qBit[1];
        System.out.println("a0, a1: " + a0 + ", " + a1);

        // If rotated axis
        double[][] H = {
                {coefficient,  coefficient},
                {coefficient, -coefficient}
        };
        double b0 = H[0][0] * a0 + H[0][1] * a1; b0 = Math.round(b0);
        double b1 = H[1][0] * a0 + H[1][1] * a1; b1 = Math.round(b1);

        // Set probabilities
        double p0 = Math.pow(a0, 2);
        double p1 = 1 - p0;
        if (meterType == 1 && generatedAxis == 1) {
            System.out.println("b0, b1: " + b0 + ", " + b1);
            p0 = Math.pow(b0, 2);
            p1 = 1 - p0;
        }
        System.out.println("p0, p1: " + p0 + ", " + p1);

        // Measure
        double[] measuredQBit = new double[2];
        double measureProb = rnd.nextDouble();
        System.out.println("Probability: " + measureProb);
        int measuredQBitIndex;
        if (measureProb < p0) { // Measure
            measuredQBitIndex = 0;
        } else {
            measuredQBitIndex = 1;
        }

        // Compare qBit with Axis of Meter
        boolean classicAxisComparison = false;
        boolean rotatedAxisComparison = false;
        if (meterType == 0) { // Classic
            classicAxisComparison = Arrays.equals(qBit, basicQBitZero) || Arrays.equals(qBit, basicQBitOne);
        } else { // Rotated
            rotatedAxisComparison = Arrays.equals(qBit, rotatedQBitZero) || Arrays.equals(qBit, rotatedQBitOne);
        }
        System.out.println("qBit axis equals Meter Classic axis: " + classicAxisComparison);
        System.out.println("qBit axis equals Meter Rotated axis: " + rotatedAxisComparison);


        // Get measure result
        int measureResult;
        if (classicAxisComparison) {
            if (measuredQBitIndex == 0) {
                measuredQBit = basicQBitZero;
                measureResult = 0;
            } else {
                measuredQBit = basicQBitOne;
                measureResult = 1;
            }
        } else if (rotatedAxisComparison) {
            if (measuredQBitIndex == 0) {
                measuredQBit = rotatedQBitZero;
                measureResult = 0;
            } else {
                measuredQBit = rotatedQBitOne;
                measureResult = 1;
            }
        } else {
            measureResult = 2;
        }

        if (measureResult != 2) {
            System.out.println("Secret Bit: " + measureResult);
            System.out.println("Measured qBit: " + "[" + measuredQBit[0] + ", " + measuredQBit[1] + "]");
        }

        return meterType;
    }

    public static void main(String[] args) {
        int i = 0;
        while (true) {
            System.out.println("ITERATION #" + i++);
            double[] generatedQBit = generateQBit();
            int meterType = chooseMeter(generatedQBit);
            if (meterType == generatedAxis) {
                break;
            }
            System.out.println();
        }
    }
}
