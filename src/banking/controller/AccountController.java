package banking.controller;

/**
 * Interface für Banking-Controller.
 * Definiert die grundlegenden Interaktionen des Controllers mit dem Benutzer.
 */
public interface AccountController {

    /**
     * Startet die Controller-Ausführung und steuert die Benutzerinteraktionen.
     * Diese Methode enthält typischerweise die Hauptschleife des Programms.
     */
    void run();

    /**
     * Beendet die Controller-Ausführung sauber und gibt Ressourcen frei.
     */
    void shutdown();

    /**
     * Verarbeitet den Anmeldevorgang eines Benutzers.
     *
     * @param cardNumber Die Kartennummer des Benutzers
     * @param pin        Die PIN des Benutzers
     * @return true wenn der Login erfolgreich war, sonst false
     */
    boolean login(String cardNumber, String pin);

    /**
     * Meldet den aktuellen Benutzer ab.
     */
    void logout();

    /**
     * Initiiert den Kontoerstellungsprozess im Controller.
     *
     * @return Array mit [cardNumber, pin] oder null im Fehlerfall
     */
    String[] createNewAccount();

    /**
     * Zeigt den Kontostand für den aktuell angemeldeten Benutzer an.
     *
     * @return true wenn die Anzeige erfolgreich war, sonst false
     */
    boolean showBalance();

    /**
     * Initiiert den Prozess zum Hinzufügen von Guthaben.
     *
     * @param amount Der hinzuzufügende Betrag
     * @return true wenn die Einzahlung erfolgreich war, sonst false
     */
    boolean addIncome(double amount);

    /**
     * Initiiert den Überweisungsprozess.
     *
     * @param receiverCardNumber Die Kartennummer des Empfängers
     * @param amount             Der zu überweisende Betrag
     * @return true wenn die Überweisung erfolgreich war, sonst false
     */
    boolean doTransfer(String receiverCardNumber, double amount);

    /**
     * Initiiert den Prozess zum Schließen des aktuellen Kontos.
     *
     * @return true wenn das Konto erfolgreich geschlossen wurde, sonst false
     */
    boolean closeAccount();

    /**
     * Prüft, ob ein Benutzer derzeit angemeldet ist.
     *
     * @return true wenn ein Benutzer angemeldet ist, sonst false
     */
    boolean isLoggedIn();

    /**
     * Gibt die Kartennummer des aktuell angemeldeten Benutzers zurück.
     *
     * @return Die Kartennummer oder null, wenn kein Benutzer angemeldet ist
     */
    String getCurrentCardNumber();
}