package QLNV;
import java.util.regex.Pattern;

public class TextFieldValidator {
    
    private static final String NUMBER_REGEX = "^-?\\d+(\\.\\d+)?$";
    private static final String CHAR_REGEX = "^[a-zA-ZÀ-ỹ\\s]+$";
    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String PHONE_REGEX = "^\\d{1,11}$";
    private static final String ALPHANUMERIC_REGEX = "^[a-zA-ZÀ-ỹ0-9\\s]+$";
    
    public static boolean isNumber(String input) {
        return Pattern.matches(NUMBER_REGEX, input);
    }
    
    public static boolean isChar(String input) {
        return Pattern.matches(CHAR_REGEX, input);
    }
    
    public static boolean isDate(String input) {
        return Pattern.matches(DATE_REGEX, input);
    }
    
    public static boolean isPhone(String input) {
        return Pattern.matches(PHONE_REGEX, input);
    }
    
    public static boolean isAlphanumeric(String input) {
        return Pattern.matches(ALPHANUMERIC_REGEX, input);
    }
    
    public static boolean charLimit(String input, int limit) {
        return input.length() <= limit;
    }
}