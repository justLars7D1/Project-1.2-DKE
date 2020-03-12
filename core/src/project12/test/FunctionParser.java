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
        System.out.println(postfix("sin ( 2 + x ^ 2 ) * cos ( 5 / 3 )"));
    }

}