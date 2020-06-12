package physicsengine.functions;

import physicsengine.Vector3d;

import java.util.*;

/**
 * Represents any function in string form that can be put in by the user. It will automatically converted to an
 * actual mathematical function (denoted in Reverse Polish Notation)
 */
public class FunctionParserRPN implements Function2d {

    //Enum with precedences
    private enum Operator {
        function(6),
        power(5),
        division(4),
        multiplication(3),
        subtraction(2),
        addition(1);
        Operator(int p) { precedence = p; }
        final int precedence;
    }

    //Map with precedences
    private static final HashMap<String, Operator> ops = new HashMap<>() {{
        put("sin", Operator.function);
        put("cos", Operator.function);
        put("tan", Operator.function);
        put("^", Operator.power);
        put("/", Operator.division);
        put("*", Operator.multiplication);
        put("-", Operator.subtraction);
        put("+", Operator.addition);
    }};

    //Actual function
    private String function;
    //Reverse polish order representation
    private String[] reversePolishOrder;

    //Constructor
    public FunctionParserRPN(String function) {
        this.function = function;
        StringBuilder goodFunctionFormat = new StringBuilder();
        for (int i = 0; i < function.length(); i++) {
            if (i+1 < function.length() && function.charAt(i) == '-' && Character.isDigit(function.charAt(i+1))) {
                goodFunctionFormat.append(function.charAt(i));
            } else if (ops.containsKey(function.substring(i,i+1)) || function.charAt(i) == '(' || function.charAt(i) == ')') {
                goodFunctionFormat.append(" ").append(function.charAt(i)).append(" ");
            } else {
                goodFunctionFormat.append(function.charAt(i));
            }
        }

        this.reversePolishOrder = getReversePolishOrder(goodFunctionFormat.toString()).toLowerCase().split(" ");
        ArrayList<String> orderNoEmpty = new ArrayList<>(Arrays.asList(this.reversePolishOrder));
        orderNoEmpty.removeAll(Arrays.asList("", null));
        this.reversePolishOrder =  orderNoEmpty.toArray(new String[orderNoEmpty.size()]);
    }

    //Get the reverse polish notation of an equation
    private static String getReversePolishOrder(String infix) {

        //Build an output string and a stack for the operations
        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<>();

        //Split the input equation by space
        for (String token : infix.split(" ")) {
            //If it is an operation (/,*,-,+)
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && hasHigherPrecedence(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);
            //Else if it's a (
            } else if (token.equals("(")) {
                stack.push(token);
            //Else if it's a )
            } else if (token.equals(")")) {
                while (!stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();
            //Otherwise it's a digit
            } else {
                output.append(token).append(' ');
            }
        }

        //Empty the rest of the operation remaining on the operation stack
        while (!stack.isEmpty())
            output.append(stack.pop()).append(' ');

        //Return the result
        return output.toString();

    }

    public double ACCURACYGRADIENTFACTOR = Math.pow(10, -8);

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    @Override
    public Vector3d gradient(Vector3d p) {
        double partialDerivativeX = partialDerivativeX(p.get_x(), p.get_z());
        double partialDerivativeY = partialDerivativeY(p.get_x(), p.get_z());
        return new Vector3d(partialDerivativeX, -1, partialDerivativeY);
    }

    public Vector3d gradient(double x, double y) {
        return new Vector3d(partialDerivativeX(x, y), -1, partialDerivativeY(x, y));
    }

    public double partialDerivativeX(double x, double y) {
        double h = ACCURACYGRADIENTFACTOR;
        double z1 = evaluate(x - 2*h, y);
        double z2 = evaluate(x - h, y);
        double z3 = evaluate(x + h, y);
        double z4 = evaluate(x + 2*h, y);
        return (z1 - 8*z2 + 8*z3 - z4)/(12*h);
    }

    public double partialDerivativeY(double x, double y) {
        double h = ACCURACYGRADIENTFACTOR;
        double z1 = evaluate(x, y - 2*h);
        double z2 = evaluate(x, y - h);
        double z3 = evaluate(x, y + h);
        double z4 = evaluate(x, y + 2*h);
        return (z1 - 8*z2 + 8*z3 - z4)/(12*h);
    }


    //Check if the current operator has a higher precedence
    private static boolean hasHigherPrecedence(String operator, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(operator).precedence);
    }

    /**
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    @Override
    public double evaluate(Vector3d p) {
        Stack<String> stack = new Stack<>();
        for (String op : reversePolishOrder) {
            if (op.equals("sin") || op.equals("cos") || op.equals("tan")) {
                double x = getValue(stack.pop(), p);
                String result = String.valueOf(executeOp(op, x, 0));
                stack.push(result);
            } else if (ops.containsKey(op)) {
                double y = getValue(stack.pop(), p);
                double x = getValue(stack.pop(), p);
                String result = String.valueOf(executeOp(op, x, y));
                stack.push(result);
            } else {
                stack.push(op);
            }
        }
        return Double.parseDouble(stack.pop());
    }

    @Override
    public double evaluate(double funcX, double funcY) {
        Stack<String> stack = new Stack<>();
        for (String op : reversePolishOrder) {
            if (op.equals("sin") || op.equals("cos") || op.equals("tan")) {
                double x = getValue(stack.pop(), funcX, funcY);
                String result = String.valueOf(executeOp(op, x, 0));
                stack.push(result);
            } else if (ops.containsKey(op)) {
                double y = getValue(stack.pop(), funcX, funcY);
                double x = getValue(stack.pop(), funcX, funcY);
                String result = String.valueOf(executeOp(op, x, y));
                stack.push(result);
            } else {
                stack.push(op);
            }
        }
        return Double.parseDouble(stack.pop());
    }

    //Executes the operation
    private static double executeOp(String operation, double v1, double v2) {
        double val = 0;
        switch (operation) {
            case "sin":
                val = Math.sin(v1);
                break;
            case "cos":
                val = Math.cos(v1);
                break;
            case "tan":
                val = Math.tan(v1);
                break;
            case "^":
                val = Math.pow(v1, v2);
                break;
            case "/":
                val = v1 / v2;
                break;
            case "*":
                val = v1 * v2;
                break;
            case "-":
                val = v1 - v2;
                break;
            case "+":
                val = v1 + v2;
                break;
        }
        return val;
    }

    //Calculate the value of a stack number (no operation)
    private double getValue(String stackVal, Vector3d p) {
        double x;
        switch (stackVal) {
            case "e":
                x = Math.E;
                break;
            case "x":
                x = p.get_x();
                break;
            case "y":
                x = p.get_z();
                break;
            default:
                x = Double.parseDouble(stackVal);
                break;
        }
        return x;
    }

    private double getValue(String stackVal, double xVal, double yVal) {
        double x;
        switch (stackVal) {
            case "e":
                x = Math.E;
                break;
            case "x":
                x = xVal;
                break;
            case "y":
                x = yVal;
                break;
            default:
                x = Double.parseDouble(stackVal);
                break;
        }
        return x;
    }

    public static void main(String[] args) {
        FunctionParserRPN p = new FunctionParserRPN("e^x");
        System.out.println(p.evaluate(new Vector3d(0,1)));
    }


    /**
     * Returns the string representation of the function
     * @return String representation of the function
     */
    @Override
    public String toString() {
        return function;
    }
}