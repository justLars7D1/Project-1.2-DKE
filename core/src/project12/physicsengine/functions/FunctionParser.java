package project12.physicsengine.functions;

import project12.physicsengine.Vector2d;

import java.util.*;

/**
 * Represents any function in string form that can be put in by the user. It will automatically converted to an
 * actual mathematical function
 */
public class FunctionParser implements Function2d {

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
    public FunctionParser(String function) {
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

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    @Override
    public Vector2d gradient(Vector2d p) {
        double z = evaluate(p);
        double zphx = evaluate(new Vector2d(p.get_x()+ACCURACYGRADIENTFACTOR, p.get_y()));
        double zphy = evaluate(new Vector2d(p.get_x(), p.get_y()+ACCURACYGRADIENTFACTOR));
        return new Vector2d((zphx-z)/ACCURACYGRADIENTFACTOR, (zphy-z)/ACCURACYGRADIENTFACTOR);
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
    public double evaluate(Vector2d p) {
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
    private double getValue(String stackVal, Vector2d p) {
        double x;
        switch (stackVal) {
            case "e":
                x = Math.E;
                break;
            case "x":
                x = p.get_x();
                break;
            case "y":
                x = p.get_y();
                break;
            default:
                x = Double.parseDouble(stackVal);
                break;
        }
        return x;
    }

    //For some testing
//    public static void main(String[] args) {
//        FunctionParser p = new FunctionParser("cos(x+y)");
//        System.out.println(p.evaluate(new Vector2d(0,1)));
//    }


}