package malahov.calculator;

public class Calculator2Test extends Calculator2 {

    public static void main(String[] args) {
        Calculator2Test test = new Calculator2Test();
        test.check(new String[]{"1 + ( 2 + 3 * ( 4 + 5 - sin ( arr ) ) )", "arr = 2"},27.272107719522957);
        test.check(new String[]{"1+(-a)","a =1"},0);
        test.check(new String[]{"(((1+2) +((3+4) + 5) +6) + (7))"},28);
        test.check(new String[]{"sqrt(36)"},6);
        test.check(new String[]{"2+4*2^(2+2)"},66);
        test.check(new String[]{"2+3*(sqrt(3*(1+2)))/2"},6.5);
        test.check(new String[]{"1+(1+(5+2)^2/7)"},9);
        test.check(new String[]{"1+2*(sqrt(25)-5)+(-1)"},0);
        test.check(new String[]{"sin(30*0.0175"},0.5012130046737979);
        test.check(new String[]{"1*(2-arr)-cos(b)+(45^4)","arr=2","b=4"},4100625.6536436207);
        test.check(new String[]{"1+(2+3*(4+5-sin(45*cos(a))))/7","a=3"},5.373176543474313);
        test.check(new String[]{"a*2*cos(4)-sin(5)","a=2"},-1.6556502087913092);
        test.check(new String[]{"a^2^2^3","a=2"},4096.0);
        test.check(new String[]{"cos(sin(tan(-a)))","a=2"},0.684258693300413);
        //test.check(new String[]{"sin(-a) - cos((a - b)) + (-a)","a=30","b=90"},0.684258693300413);
    }

    public void check(String[] stringArgs, double expected) {
        Calculator2Test object = new Calculator2Test();
        double result = object.startProgram(stringArgs);
        if ( result == expected) {
            System.out.println(GREEN + "Test passed. Result: " + result + " Expected result: " + expected);
        } else {
            System.out.println(RED + "Test failed. Result: " + result + " Expected result: " + expected);
        }
    }

    @Override
    public double startProgram(String[] args) {
        fillTheHaspMap(args);
        formula = args[0];
        fillThePriority();
        return calculate(convertToRpn(parseFormula()));
    }
}
