package banking.service;

import banking.domain.Account;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface für den Banking-Service, der die Geschäftslogik für Bankkonten bereitstellt.
 */
public interface AccountService {

    /**
     * Erstellt ein neues Bankkonto mit einer zufälligen Kartennummer und PIN.
     *
     * @return Das neu erstellte Konto
     */
    Account createAccount();
    
    /**
     * Schließt ein bestehendes Bankkonto.
     * 
     * @param cardNumber die Kartennummer des zu schließenden Kontos
     * @throws IllegalArgumentException wenn das Konto null ist oder nicht existiert
     */
    void closeAccount(String cardNumber);
    
    /**
     * Führt den Login-Vorgang durch.
     * 
     * @param cardNumber Die Kartennummer
     * @param pin Die PIN
     * @return Das Konto, wenn die Anmeldedaten korrekt sind
     * @throws IllegalArgumentException wenn die Anmeldedaten ungültig sind
     */
    Account login(String cardNumber, String pin);
    
    /**
     * Gibt das Guthaben eines Kontos zurück.
     * 
     * @param cardNumber Kartennummer des vorhandenen Kontos
     * @return Das Guthaben
     * @throws IllegalArgumentException wenn das Konto null ist
     */
    double getAccountBalance(String cardNumber);
    
    /**
     * Führt eine Überweisung zwischen zwei Konten durch.
     * 
     * @param senderCardNumber Die Kartennummer des Senders
     * @param receiverCardNumber Die Kartennummer des Empfängers
     * @param amount Der zu überweisende Betrag
     * @throws IllegalArgumentException wenn eine der Kartennummern ungültig ist oder die Konten nicht existieren
     * @throws IllegalStateException wenn nicht genügend Geld vorhanden ist
     */
    void transfer(String senderCardNumber, String receiverCardNumber, double amount) throws SQLException;
    
    /**
     * Fügt Geld zu einem Konto hinzu.
     * 
     * @param cardNumber Kartennummer für das Konto
     * @param amount Der Betrag
     * @throws IllegalArgumentException wenn das Konto null ist oder der Betrag ungültig ist
     */
    void addIncome(String cardNumber, double amount);
    
    /**
     * Sucht ein Konto anhand der Kartennummer.
     * 
     * @param cardNumber Die Kartennummer
     * @return Optional mit dem Konto, wenn gefunden
     * @throws IllegalStateException wenn das Konto existieren sollte, aber nicht gefunden wurde
     */
    Optional<Account> findAccountByCardNumber(String cardNumber);
    
    /**
     * Gibt alle Konten zurück.
     * 
     * @return Eine Liste aller Konten
     */
    List<Account> findAllAccounts();
}