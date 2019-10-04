/**
 * @author genVlad
 * Algorithm by:  @see <a href="http://www.blackwasp.co.uk/NumberToWords.aspx">http://www.blackwasp.co.uk/NumberToWords.aspx</a>
 */
public class NumberToWords {

    // Single-digit and small number names
    private static String[] _smallNumbers = new String[]{"nula", "jedna", "dva", "tri", "štyri", "päť", "šesť", "sedem", "osem", "deväť", "desať", "jedenásť", "dvanásť", "trinásť", "štrnásť", "pätnásť", "šestnásť", "sedemnásť", "osemnásť", "devätnásť"};

    // Tens number names from twenty upwards
    private static String[] _tens = new String[]{"", "", "dvadsať", "tridsať", "štyridsať", "päťdesiat", "šesťdesiat", "sedemdesiat", "osemdesiat", "deväťdesiat"};

    // Scale number names for use during recombination
    private static String[] _scaleNumbers = new String[]{"", "tisíc", "milión", "miliarda"};

    /**
     * Converts an amount into Slovak words in eur
     * @see <a href="https://jazykovaporadna.sme.sk/q/3743/">https://jazykovaporadna.sme.sk/q/3743/</a>
     * @param number value to convert
     * @return
     */
    public static String amountToWords(double number) {
        String text = "";
        long[] values = split(number);
        if(values[0] >= 0) {
            if(values[0] == 1) {
                text += "jedno euro";
            }
            else if(values[0] >= 2) {
                text += "dve eurá";
            }
            else if(values[0] == 3 || values[0] == 4) {
                text += numberToWords(values[0]) + "eurá";
            }
            else {
                text += numberToWords(values[0]) + " eur";
            }
        }
        if(values[1] > 0) {
            text += " a " + numberToWords(values[1]);
            String suffix;
            if(values[1] == 1) {
                suffix = " cent";
            }
            else if(values[1] >= 2 && values[1] <= 4) {
                suffix = " centy";
            }
            else {
                suffix = " centov";
            }
            text += suffix;
        }
        return text;
    }

    /**
     * Converts an double value into Slovak words
     * @see <a href="https://jazykovaporadna.sme.sk/q/704/">https://jazykovaporadna.sme.sk/q/704/</a>
     * @param number value to convert
     * @return
     */
    public static String decimalNumberToWords(double number) {
        String text = "";
        long[] values = split(number);
        if(values[0] == 1) {
            text += "jedna";
        }
        else if(values[0] >= 2) {
            text += "dve";
        }
        else {
            text += numberToWords(values[0]);
        }
        if(values[1] > 0) {
            String decimalSeparator;
            if(values[0] == 1) {
                decimalSeparator = " celá ";
            }
            else if(values[0] >= 2 && values[0] <= 4) {
                decimalSeparator = " celé ";
            }
            else {
                decimalSeparator = " celých ";
            }
            text += decimalSeparator + numberToWords(values[1]);
        }
        return text;
    }

    /**
     * Converts an long value into Slovak words
     * @param number value to convert
     * @return
     */
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

    // Converts a three-digit group into Slovak words
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

    private static long[] split(double number) {
        String doubleString = Double.toString(number);
        String longStrings[]=doubleString.split("\\.");
        return new long[]{Long.parseLong(longStrings[0]), Long.parseLong(longStrings[1])};
    }
}
