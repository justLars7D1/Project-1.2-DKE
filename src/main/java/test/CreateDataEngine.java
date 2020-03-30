package test;

import physicsengine.functions.FunctionParserRPN;

public class CreateDataEngine {
    public static void main(String[] args) {
        FunctionParserRPN f = new FunctionParserRPN("x^2");
        double targetX = 3;
        for(double h = Math.pow(10, -6); h <= 1; h *= 10) {
            System.out.print(h + "," + Math.pow(targetX, 2) + ",");
            double t = 0;
            double x = f.evaluate(0, 0);
            while (t <= targetX) {
                x += h*Math.pow(t, 2);
                t += h;
            }
            System.out.print(x + ",");
            t = 0;
            x = f.evaluate(0, 0);
            while (t <= targetX) {
                x += h*Math.pow(t,2) + 0.5*Math.pow(h, 2)*2*t;
                t += h;
            }
            System.out.println(x);
        }
    }
}
