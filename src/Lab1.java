import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Lab1 extends JFrame {

    int startX = 50;
    int startY = 50;
    int centerX = 150;
    int centerY = 150;
    int width = 200;
    int height = 200;
    int radius = width / 2;
    static double qBit0;
    static double qBit1;
    static int measuredQBitIndex;
    static boolean isHadamard = false;

    public Lab1(String windowName) {
        setTitle(windowName);
        setSize(width * 2, height * 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        // Center of Circle x = 150, y = 150
        drawCircleCoordinates(g);
        drawQBit(g);
    }

    public void drawCircleCoordinates(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(startX, startY, width, height);
        g.drawLine(centerX, startY - 15, centerX, startY + height + 15); // Vert
        g.drawLine(startX - 15, centerY, startX + width + 15, centerY); // Hor

        if (!isHadamard) {
            g.drawString("|1>", centerX + 5, startY - 5);
            g.drawString("|0>", startX + width + 5, centerY - 5);
        } else {
            g.drawString("|0>'", centerX + (int)(1 / Math.sqrt(2) * 100), centerY - (int)(1 / Math.sqrt(2) * 100));
            g.drawString("|1>'", centerX + (int)(1 / Math.sqrt(2) * 100) - 15, centerY + (int)(1 / Math.sqrt(2) * 100));
        }
    }

    public void drawQBit(Graphics g) {
        if (!isHadamard) {
            int qBitX = (int) (qBit0 * 100);
            int qBitY = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(qBitX, 2));
            //System.out.println(qBitX + " " + qBitY);
            g.setColor(Color.GREEN);
            g.drawOval(centerX + qBitX, centerY - qBitY, 10, 10);
            g.fillOval(centerX + qBitX, centerY - qBitY, 10, 10);

            g.setColor(Color.RED);
            if (measuredQBitIndex == 0) {
                g.drawOval(startX + width - 5, centerY - 5, 10, 10);
                g.fillOval(startX + width - 5, centerY - 5, 10, 10);
            } else {
                g.drawOval(centerX - 5, startY - 5, 10, 10);
                g.fillOval(centerX - 5, startY - 5, 10, 10);
            }
        } else {
            int qBitX = (int) (qBit0 * 100);
            int qBitY = (int) (qBit1 * 100);
            g.setColor(Color.GREEN);
            g.drawOval(centerX + qBitX, centerY - qBitY, 10, 10);
            g.fillOval(centerX + qBitX, centerY - qBitY, 10, 10);

            g.setColor(Color.RED);
            if (measuredQBitIndex == 1) {
                // Down point
                g.drawOval(centerX + (int)(1 / Math.sqrt(2) * 100), centerY + (int)(1 / Math.sqrt(2) * 100), 10, 10);
                g.fillOval(centerX + (int)(1 / Math.sqrt(2) * 100), centerY + (int)(1 / Math.sqrt(2) * 100), 10, 10);
            } else {
                // Up point
                g.drawOval(centerX + (int)(1 / Math.sqrt(2) * 100), centerY - (int)(1 / Math.sqrt(2) * 100), 10, 10);
                g.fillOval(centerX + (int)(1 / Math.sqrt(2) * 100), centerY - (int)(1 / Math.sqrt(2) * 100), 10, 10);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Classic
        System.out.println("Classic");
        calculateQBitClassic();
        Lab1 window1 = new Lab1("Classic");
        Thread.sleep(250);

        // Hadamard
        System.out.println("\nHadamard");
        calculateQBitHadamard();
        Lab1 window2 = new Lab1("Hadamard");
    }

    public static void calculateQBitClassic() {
        // QBit
        int[] basicQBitZero = {1, 0}; // |0> = [1, 0] = x
        int[] basicQBitOne  = {0, 1}; // |1> = [0, 1] = y

        // Set qBit
        Random rnd = new Random();
        double a0 = rnd.nextDouble();
        double a1 = Math.sqrt(1 - Math.pow(a0, 2));
        Lab1.qBit0 = a0; Lab1.qBit1 = a1;
        System.out.println("a0, a1: " + a0 + ", " + a1);

        // Set probabilities
        double p0 = Math.pow(a0, 2);
        double p1 = 1 - p0;
        System.out.println("p0, p1: " + p0 + ", " + p1);

        // Measure
        int[] measuredQBit;
        double measureProb = rnd.nextDouble();
        System.out.println("Probability: " + measureProb);

        if (measureProb < p0) {
            measuredQBit = basicQBitZero;
            measuredQBitIndex = 0;
        } else {
            measuredQBit = basicQBitOne;
            measuredQBitIndex = 1;
        }
        System.out.println("Measured qBit: " + "[" + measuredQBit[0] + ", " + measuredQBit[1] + "]");
        System.out.println("Measured qBit State: " + "|" + measuredQBitIndex + ">");
    }

    public static void calculateQBitHadamard() {
        isHadamard = true;
        // QBit
        int[] basicQBitZero = {1, 0}; // |0> = [1, 0] = x
        int[] basicQBitOne  = {0, 1}; // |1> = [0, 1] = y

        // Set qBit
        Random rnd = new Random();
        double a0 = rnd.nextDouble();
        double a1 = Math.sqrt(1 - Math.pow(a0, 2));
        // Lab1.qBit0 = a0; Lab1.qBit1 = a1;
        System.out.println("a0, a1: " + a0 + ", " + a1);

        // Hadamard matrix
        double coefficient = 1 / Math.sqrt(2);
        double[][] H = {
                {coefficient,  coefficient},
                {coefficient, -coefficient}
        };
        System.out.println("H: " + H[0][0] + ", " + H[0][1] + ", "+ H[1][0] + ", "+ H[1][1]);
        double b0 = H[0][0] * a0 + H[0][1] * a1;
        double b1 = H[1][0] * a0 + H[1][1] * a1;
        Lab1.qBit0 = b0; Lab1.qBit1 = b1;
        System.out.println("b0, b1: " + b0 + ", " + b1);

        // Set probabilities
        double pa0 = Math.pow(a0, 2);
        double pa1 = 1 - pa0;
        System.out.println("pa0, pa1: " + pa0 + ", " + pa1);

        // TODO By formula !!!
        double pb0 = Math.pow(b0, 2);
        double pb1 = 1 - pb0;
        System.out.println("pb0, pb1: " + pb0 + ", " + pb1);
        // TODO By formula !!!

        // Measure
        int[] measuredQBit;
        double measureProb = rnd.nextDouble();
        System.out.println("Probability: " + measureProb);

        if (measureProb < pa0) {
            measuredQBit = basicQBitZero;
            measuredQBitIndex = 0;
        } else {
            measuredQBit = basicQBitOne;
            measuredQBitIndex = 1;
        }
        System.out.println("Measured qBit: " + "[" + measuredQBit[0] + ", " + measuredQBit[1] + "]");
        System.out.println("Measured qBit State: " + "|" + measuredQBitIndex + ">");
    }
}










// 0,6239721601200177
// 0,7814465710431897

// 0,9993307044047539
// 0,03658064015156254




