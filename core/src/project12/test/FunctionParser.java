package project12.test;

import java.util.*;

public class FunctionParser {

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
    private static HashMap<String, Operator> ops = new HashMap<>() {{
        put("sin", Operator.function);
        put("cos", Operator.function);
        put("tan", Operator.function);
        put("^", Operator.power);
        put("/", Operator.division);
        put("*", Operator.multiplication);
        put("-", Operator.subtraction);
        put("+", Operator.addition);
    }};

    //Check if the current operator has a higher precedence
    private static boolean hasHigherPrecedence(String operator, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(operator).precedence);
    }

    //Get the reverse polish notation of an equation
    public static String postfix(String infix) {

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

    public static void main(String[] args) {
        String[] polish = postfix("sin ( 2 + x ^ 2 ) * cos ( y / 3 )").toLowerCase().split(" ");
        System.out.println(Arrays.toString(polish));
        System.out.println(eval(polish, 0, 5));
    }

    public static double eval(String[] polish, double xVal, double yVal) {
        double result = 0;
        ArrayList<String> pol = new ArrayList<>(Arrays.asList(polish));
        int i = polish.length-1;
        while(i >= 0) {

            //If the operation is finished
            if (pol.size() == 1) {
                result = Double.parseDouble(pol.get(0));
                break;
            }

            // If the value is an operator sine, cosine or tangent, and there is a number before it, execute it
            if (ops.containsKey(pol.get(i)) && !ops.containsKey(pol.get(i-1)) && (pol.get(i).equals("sin") || pol.get(i).equals("cos") || pol.get(i).equals("tan"))) {
                if (pol.get(i-1).equals("x")) pol.set(i-1, String.valueOf(xVal));
                else if (pol.get(i-1).equals("y")) pol.set(i-1, String.valueOf(yVal));
                double valToEval = Double.parseDouble(pol.get(i-1));
                double value = executeOp(pol.get(i), valToEval, 0);
                pol.remove(i);
                pol.remove(i-1);
                pol.add(i-1, String.valueOf(value));

                while(i < pol.size() && !ops.containsKey(pol.get(i))) i++;

//                System.out.println(pol + " : " + i);

                // Else if it's an operator, but not one of the previous ones and there are two number in front, execute it
            } else if (ops.containsKey(pol.get(i)) && !ops.containsKey(pol.get(i-1)) && !ops.containsKey(pol.get(i-2))) {

                if (pol.get(i-1).equals("x")) pol.set(i-1, String.valueOf(xVal));
                else if (pol.get(i-1).equals("y")) pol.set(i-1, String.valueOf(yVal));
                if (pol.get(i-2).equals("x")) pol.set(i-2, String.valueOf(xVal));
                else if (pol.get(i-2).equals("y")) pol.set(i-2, String.valueOf(yVal));
                double valToEval1 = Double.parseDouble(pol.get(i-1));
                double valToEval2 = Double.parseDouble(pol.get(i-2));
                double value = executeOp(pol.get(i), valToEval2, valToEval1);
                pol.remove(i);
                pol.remove(i-1);
                pol.remove(i-2);
                pol.add(i-2, String.valueOf(value));

                i--;
                while(i < pol.size() && !ops.containsKey(pol.get(i))) i++;

//                System.out.println(pol + " : " + i);

                //Otherwise continue our search
            } else {
                i--;
            }

        }

        return result;

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

}