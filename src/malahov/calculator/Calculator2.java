package malahov.calculator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class Calculator2 {

    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";

    String formula;
    HashMap<String, Double> variables = new HashMap<>();
    HashMap<String, Integer> priority = new HashMap<>();

    public static void main(String[] args) {
        Calculator2 calculator2 = new Calculator2();
        calculator2.startProgram(args);
    }

    /**
     * This method displays the result of the method calculate.
     * And also writes down all variables in a Hashmap.
     *
     * @param args args from main.
     * @return result of the expression.
     */
    public double startProgram(String[] args) {
        fillTheHaspMap(args);
        formula = args[0];
        fillThePriority();
        try {
            System.out.println(calculate(convertToRpn(parseFormula())));
        } catch (NumberFormatException exception) {
            System.out.println(RED + "Something went wrong!");
            System.out.println("Please check if you entered the correct formula.");
            exception.printStackTrace();
            return 0;
        } catch (EmptyStackException exception) {
            System.out.println(RED + "Something went wrong!");
            System.out.println(RED + "Please check if you've wrapped the unary minus in parentheses.");
            System.out.println(RED + "Example:(-1)");
            return 0;
        }

        return calculate(convertToRpn(parseFormula()));
    }

    /**
     * This method splits the variable string and writes
     * the variable to the hashmap.
     *
     * @param args args from main.
     */
    public void fillTheHaspMap(String[] args) {
        for (int i = 1; i < args.length; i++) {
            String stringVar = args[i];
            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();
            for (int k = 0; k < stringVar.length(); k++) {
                if (stringVar.charAt(k) == ' ') {
                    continue;
                }
                if (k < stringVar.indexOf("=")) {
                    key.append(stringVar.charAt(k));
                } else if (k > stringVar.indexOf("=")) {
                    value.append(stringVar.charAt(k));
                }
            }
            variables.put(key.toString(), Double.parseDouble(value.toString()));
        }
    }

    /**
     * This method splits the input string into elements and writes each element to arrayList.
     * Then all the variables are changed by their values.
     * Elements which with unary minuses are replaced by one element in arrayList. It was:({-,1}),became:({-1}).
     *
     * @return parsed formula.
     */
    public ArrayList<String> parseFormula() {
        ArrayList<String> arrayList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == ' ') {
                continue;
            }
            if (priority.containsKey(formula.charAt(i) + "")) {
                //If the element is not a number and the previous element was also not a number,
                //then the buffer variable does not need to be written.
                if (!buffer.toString().equals("")) arrayList.add(buffer.toString());
                arrayList.add(formula.charAt(i) + "");
                buffer = new StringBuilder();
            } else {
                buffer.append(formula.charAt(i));
            }
        }
        //Edge case if the number left in the buffer variable.
        if (!buffer.toString().equals("")) {
            arrayList.add(buffer.toString());
        }
        //Changed variables by their values.
        for (int i = 0; i < arrayList.size(); i++) {
            if (variables.containsKey(arrayList.get(i))) {
                arrayList.set(i, variables.get(arrayList.get(i)) + "");
            }
        }
        //Checking for unary minus and replacing it.
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals("(") && arrayList.get(i + 1).equals("-")) {
                arrayList.remove(i + 1);
                arrayList.set(i + 1, -1 * Double.parseDouble(arrayList.get(i + 1)) + "");
            }
        }
        return arrayList;
    }

    /**
     * This method fills in the hashmap of all math expressions
     * that are available with their priority.
     */
    public void fillThePriority() {
        priority.put("sqrt", 5);
        priority.put("log2", 5);
        priority.put("log10", 5);
        priority.put("atan", 5);
        priority.put("tan", 5);
        priority.put("cos", 5);
        priority.put("sin", 5);
        priority.put("^", 4);
        priority.put("*", 3);
        priority.put("/", 3);
        priority.put("+", 2);
        priority.put("-", 2);
        priority.put("(", 1);
        priority.put(")", 0);
    }

    /**
     * This method converts the formula from the usual notation
     * to the reverse Polish notation(Rpn - reverse Polish notation).
     *
     * @param formula Parsed formula.
     * @return formula written in reverse Polish notation.
     */
    public ArrayList<String> convertToRpn(ArrayList<String> formula) {
        ArrayList<String> result = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (String s : formula) {
            if (!priority.containsKey(s)) {
                result.add(s);
            } else if (priority.get(s) == 1) {
                stack.push(s);
            } else if (priority.get(s) == 0) {
                while (priority.get(stack.peek()) != 1) {
                    result.add(stack.pop());
                }
                stack.pop();//Remove the remaining parenthesis"(".
            } else if (priority.get(s) > 1) {
                //We cannot put to the stack an operation with a priority lower than what is in the stack.
                while (!stack.empty()) {
                    if (priority.get(s) <= priority.get(stack.peek())) {
                        result.add(stack.pop());
                    } else break;
                }
                stack.push(s);
            }
        }
        //Take all operations from the stack.
        while (!stack.empty()) {
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * This method finds the result of the mathematical formula.
     * Using a formula written in Rpn.
     *
     * @param listRpn formula written in reverse Polish notation.
     * @return the value of the formula expression.
     */
    public double calculate(ArrayList<String> listRpn) {
        Stack<String> stack = new Stack<>();
        for (String s : listRpn) {
            if (!priority.containsKey(s)) {
                stack.push(s);
            } else if (priority.get(s) > 1 && priority.get(s) < 5) {
                double a = Double.parseDouble(stack.pop());
                double b = Double.parseDouble(stack.pop());
                switch (s) {
                    case "+" -> stack.push(b + a + "");
                    case "-" -> stack.push(b - a + "");
                    case "/" -> stack.push(b / a + "");
                    case "*" -> stack.push(b * a + "");
                    case "^" -> stack.push(Math.pow(b, a) + "");
                }
            } else if (priority.get(s) == 5) {
                double a = Double.parseDouble(stack.pop());
                switch (s) {
                    case "sin" -> stack.push(Math.sin(a) + "");
                    case "cos" -> stack.push(Math.cos(a) + "");
                    case "tan" -> stack.push(Math.tan(a) + "");
                    case "atan" -> stack.push(Math.atan(a) + "");
                    case "log10" -> stack.push(Math.log10(a) + "");
                    case "log2" -> stack.push(Math.log(a) + "");
                    case "sqrt" -> stack.push(Math.sqrt(a) + "");
                }
            }
        }
        return Double.parseDouble(stack.pop());
    }
}
