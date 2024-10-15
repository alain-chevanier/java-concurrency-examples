package unam.ciencias.computoconcurrente.introjava;

/**
 * Program with the implementation to perform
 * conversion between numeric bases
 */
public class BaseConversionUtils {

	
    public static void main(String[] args) {
        int num = 309;

        System.out.println("Let's transform " + num + " to different bases");
        // expected value: "100110101"
        System.out.println("to base 2 -> " + toBaseN(num, 2));
        // expected value: "465"
        System.out.println("to base 8 -> " + toBaseN(num, 8));
        // expected value: "135"
        System.out.println("to base 16 -> " + toBaseN(num, 16));

        String numInBase = "fa";
        int base = 16;
        System.out.println("Let's transform \"" + numInBase + "\" from base " + base + " to base 10");
        // expected value:
        System.out.println("to base 10 -> " + fromBaseN(numInBase, base));

        //
        System.out.println("Just verifying ech function is the inverse of the other: "
                           + fromBaseN(toBaseN(15, 16), 16));
    }

    static String toBaseN(int num, int base) {
        int div, reminder;
        String result = "";

        // do at least once
        do {
            // Note: integral division excludes the decimal part
            div = num / base;

            // Note: the module "%" operator calculates the reminder
            // of the division
            reminder = num % base;

            // we accumulate the current reminder in result
            // the "+" operator performs a string concatenation
            // when one of the operands is a string.
            // This add the new digit to the begining of the result
            result = valueToChar(reminder) + result;

            // we reassign num for the next iteration, i.e. repeat
            // the process with the following division result
            num = div;

            // repeat while the division result is greater or
            // equal to the base
        } while (div >= base);

        // finally add the last division result at the beginning
        // of the result, but only if it is not zero
        if (div != 0) {
            result = valueToChar(div) + result;
        }

        return result;
    }

    static int fromBaseN(String number, int base) {
        int baseValue = 1;
        int value = 0;
        char[] chars = new StringBuilder(number)
            .reverse().toString().toCharArray();
        for(char c : chars) {
            int currentValue = charToValue(c);
            value = value + (currentValue * baseValue);
            baseValue = baseValue * base;
        }
        return value;
    }

    static char valueToChar(int num) {
        return (char) (num < 10 ? (num + '0') : (num + 'a' - 10));
    }

    static int charToValue(char c) {
        return c > 'a' ? (c - 'a' + 10) : (c - '0');
    }
}
