package test;

import physicsengine.functions.FunctionParserRPN;

public class CreateDataDerivative {
    public static void main(String[] args) {
        FunctionParserRPN f = new FunctionParserRPN("x^2");
        double acc = Math.pow(10, -10);
        for(double i = acc; i <= 1; i*=10) {
            f.ACCURACYGRADIENTFACTOR = i;
            double v = f.gradient(1, 0).get_x();
            System.out.println(i +","+ Math.abs(2-v));
        }
    }
}
