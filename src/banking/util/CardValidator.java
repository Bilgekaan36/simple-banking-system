package banking.util;

public class CardValidator {

    /**
     * Überprüft, ob eine Kartennummer dem Luhn-Algorithmus entspricht.
     *
     * @param cardNumber Die zu überprüfende Kartennummer
     * @return true, wenn die Kartennummer gültig ist, sonst false
     */
    public static boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    /**
     * Berechnet die Luhn-Prüfziffer für eine gegebene Nummer (ohne Prüfziffer).
     *
     * @param numberWithoutChecksum Die Nummer ohne Prüfziffer
     * @return Die berechnete Prüfziffer
     */
    public static int calculateLuhnChecksum(String numberWithoutChecksum) {
        int sum = 0;
        for (int i = 0; i < numberWithoutChecksum.length(); i++) {
            int digit = Character.getNumericValue(numberWithoutChecksum.charAt(i));
            if ((i % 2) == 0) digit *= 2;
            if (digit > 9) digit -= 9;
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }
}