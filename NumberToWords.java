package sk.axonpro.itms3.client.common.todelete;

/**
 * @author Vladimir Kubinda
 * Algorithm by:  @see <a href="http://www.blackwasp.co.uk/NumberToWords.aspx">http://www.blackwasp.co.uk/NumberToWords.aspx</a>
 */
public class NumberToWords {
    public static void main(String[] args) {
        System.out.println(numberToWords((12345678985225L)));
    }

    // Single-digit and small number names
    private static String[] _smallNumbers = new String[]{"nula", "jedna", "dva", "tri", "štyri", "päť", "šesť", "sedem", "osem", "deväť", "desať", "jedenásť", "dvanásť", "trinásť", "štrnásť", "pätnásť", "šestnásť", "sedemnásť", "osemnásť", "devätnásť"};

    // Tens number names from twenty upwards
    private static String[] _tens = new String[]{"", "", "dvadsať", "tridsať", "štyridsať", "päťdesiat", "šesťdesiat", "sedemdesiat", "osemdesiat", "deväťdesiat"};

    // Scale number names for use during recombination
    private static String[] _scaleNumbers = new String[]{"", "tisíc", "milión", "miliarda"};

    public static String amountToWords(double number) {
        //TODO
        return null;
    }

    // Converts an integer value into English words
    public static String numberToWords(long number) {
        // Ensure a positive number to extract from
        long positive = Math.abs(number);

        if(getDigitsCount(positive) > 12) {
            throw new RuntimeException("Max digits count 12.");
        }

        // Zero rule
        if (number == 0)
            return _smallNumbers[0];

        // Array to hold four three-digit groups
        long[] digitGroups = new long[4];

        // Extract the three-digit groups
        for (int i = 0; i < 4; i++) {
            digitGroups[i] = positive % 1000;
            positive /= 1000;
        }

        // Convert each three-digit group to words
        String[] groupText = new String[4];

        for (int i = 0; i < 4; i++)
            groupText[i] = threeDigitGroupToWords(digitGroups[i]);

        // Recombine the three-digit groups
        String combined = groupText[0];
        // Process the remaining groups in turn, smallest to largest
        for (int i = 1; i < 4; i++) {
            // Only add non-zero items
            if (digitGroups[i] != 0) {
                // Build the string to add as a prefix
                String prefix;
                if(digitGroups[i] == 1) {
                    prefix = "";
                }
                else if(digitGroups[i] == 2 && i != 2) {
                    prefix = "dve";
                }
                else {
                    prefix = groupText[i];
                }

                if(i == 2) {
                    String suffix;
                    if(digitGroups[i] >= 2 && digitGroups[i] <= 4) {
                        suffix = "y";
                    }
                    else if(digitGroups[i] >= 5) {
                        suffix = "ov";
                    }
                    else {
                        suffix = "";
                    }
                    prefix += _scaleNumbers[i] + suffix;
                }
                else if(i == 3) {
                    if(digitGroups[i] >= 2 && digitGroups[i] <= 4) {
                        prefix += "miliardy";
                    }
                    else if(digitGroups[i] >= 5) {
                        prefix += "miliárd";
                    }
                    else {
                        prefix += _scaleNumbers[i];
                    }
                }
                else {
                    prefix += _scaleNumbers[i];
                }

                // Add the three-digit group to the combined string
                combined = prefix + combined;
            }
        }

        // Negative rule
        if (number < 0)
            combined = "mínus " + combined;

        return combined;
    }

    // Converts a three-digit group into English words
    private static String threeDigitGroupToWords(long threeDigits) {
        // Initialise the return text
        String groupText = "";

        // Determine the hundreds and the remainder
        long hundreds = threeDigits / 100;
        long tensUnits = threeDigits % 100;

        // Hundreds rules
        if (hundreds != 0) {
            if(hundreds == 2) {
                groupText += "dve";
            }
            else if(hundreds != 1) {
                groupText += _smallNumbers[(int)hundreds];
            }
            groupText += "sto";
        }

        // Determine the tens and units
        long tens = tensUnits / 10;
        long units = tensUnits % 10;

        // Tens rules
        if (tens >= 2) {
            groupText += _tens[(int)tens];
            if (units != 0)
                groupText += _smallNumbers[(int)units];
        } else if (tensUnits != 0)
            groupText += _smallNumbers[(int)tensUnits];

        return groupText;
    }

    private static int getDigitsCount(long number) {
        if(number == 0) {
            return 1;
        }
        else {
            return (int)(Math.log10(number)+1);
        }
    }
}
