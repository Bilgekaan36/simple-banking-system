package banking.util;

import java.util.Random;

/**
 * Utility-Klasse zur Generierung von Kreditkartennummern und PINs.
 */
public class CardGenerator {

    private static final String BIN = "400000";
    private static final Random RND = new Random();

    /**
     * Generiert wird eine gültige Kreditkartennummer, die dem Luhn-Algorithmus entspricht.
     *
     * @return Eine gültige Kreditkartennummer
     */
    public static String generateCardNumber() {
        String accountIdentifier = String.format("%09d", RND.nextInt(1_000_000_000));
        String cardWithoutChecksum = BIN + accountIdentifier;
        int checksum = CardValidator.calculateLuhnChecksum(cardWithoutChecksum);
        return cardWithoutChecksum + checksum;
    }

    /**
     * Generiert wird eine zufällige 4-stellige PIN.
     *
     * @return Eine 4-stellige PIN als String
     */
    public static String generatePin() {
        return String.format("%04d", RND.nextInt(10000));
    }
}