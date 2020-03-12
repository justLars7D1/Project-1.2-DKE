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
        this.reversePolishOrder = getReversePolishOrder(function).toLowerCase().split(" ");
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
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    @Override
    public double evaluate(Vector2d p) {
        double xVal = p.get_x();
        double yVal = p.get_y();
        //The polish notation in an arraylist, so we can manipulate it later
        ArrayList<String> pol = new ArrayList<>(Arrays.asList(reversePolishOrder));
        pol.removeAll(Arrays.asList("", null));

        int i = pol.size()-1;
        //While there are still calculations to compute
        while(pol.size() != 1) {

            // If the value is an operator sine, cosine or tangent, and there is a number before it, execute it
            if (ops.containsKey(pol.get(i)) && !ops.containsKey(pol.get(i-1)) && (pol.get(i).equals("sin") || pol.get(i).equals("cos") || pol.get(i).equals("tan"))) {

                //Parse the values correctly to doubles first and compute the value
                if (pol.get(i-1).equals("x")) pol.set(i-1, String.valueOf(xVal));
                else if (pol.get(i-1).equals("y")) pol.set(i-1, String.valueOf(yVal));
                double valToEval = (pol.get(i-1).equals("e")) ? Math.E: Double.parseDouble(pol.get(i-1));
                double value = executeOp(pol.get(i), valToEval, 0);

                //Remove the old values and place the new value in the correct position of the arraylist
                pol.remove(i);
                pol.remove(i-1);
                pol.add(i-1, String.valueOf(value));

                //Update the index for the next best operation to perform
                while(i < pol.size() && !ops.containsKey(pol.get(i))) i++;

//                System.out.println(pol + " : " + i);

            // Else if it's an operator, but not one of the previous ones and there are two number in front, execute it
            } else if (ops.containsKey(pol.get(i)) && !ops.containsKey(pol.get(i-1)) && !ops.containsKey(pol.get(i-2))) {

                //Parse the values correctly to doubles first and compute the value
                if (pol.get(i-1).equals("x")) pol.set(i-1, String.valueOf(xVal));
                else if (pol.get(i-1).equals("y")) pol.set(i-1, String.valueOf(yVal));
                if (pol.get(i-2).equals("x")) pol.set(i-2, String.valueOf(xVal));
                else if (pol.get(i-2).equals("y")) pol.set(i-2, String.valueOf(yVal));
                double valToEval1 = (pol.get(i-1).equals("e")) ? Math.E: Double.parseDouble(pol.get(i-1));
                double valToEval2 = (pol.get(i-2).equals("e")) ? Math.E: Double.parseDouble(pol.get(i-2));
                double value = executeOp(pol.get(i), valToEval2, valToEval1);

                //Update the index for the next best operation to perform
                pol.remove(i);
                pol.remove(i-1);
                pol.remove(i-2);
                pol.add(i-2, String.valueOf(value));

                //Update the index for the next best operation to perform
                i--;
                while(i < pol.size() && !ops.containsKey(pol.get(i))) i++;

//                System.out.println(pol + " : " + i);

                //Otherwise continue our search
            } else {
                i--;
            }

        }

        //Return the result
        return Double.parseDouble(pol.get(0));

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

    //Check if the current operator has a higher precedence
    private static boolean hasHigherPrecedence(String operator, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(operator).precedence);
    }

}