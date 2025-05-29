package banking.service;

import banking.domain.Account;
import banking.repository.AccountRepository;
import banking.util.CardGenerator;
import banking.util.CardValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AccountServiceImpl implements AccountService {
    private static final String BIN = "400000";
    private static final Random RND = new Random();

    AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account createAccount() {
        String card = CardGenerator.generateCardNumber();
        String pin = CardGenerator.generatePin();
        Account account = new Account(card, pin);
        repository.save(account);
        return account;
    }

    @Override
    public void closeAccount(String cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Card number cannot be null");
        }

        if (repository.findByCardNumber(cardNumber).isEmpty()) {
            throw new IllegalArgumentException("Account does not exist");
        }

        repository.remove(cardNumber);

        // Optional verification step: check if the account was actually removed
        if (repository.findByCardNumber(cardNumber).isPresent()) {
            throw new IllegalStateException("Failed to close account");
        }
    }

    @Override
    public Account login(String cardNumber, String pin) {
        Optional<Account> optional = repository.findByCardNumber(cardNumber);
        if (optional.isPresent()) {
            Account account = optional.get();
            if (account.isCorrectPin(pin)) {
                return account;
            }
        }
        throw new IllegalArgumentException("Wrong card number or PIN!");
    }

    @Override
    public double getAccountBalance(String cardNumber) {
        Account account = repository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found!"));
        return account.getBalance();
    }

    @Override
    public void transfer(String senderCardNumber, String receiverCardNumber, double transferAmount) throws SQLException {
        if (!CardValidator.isValidLuhn(receiverCardNumber)) {
            throw new IllegalArgumentException("Card number validation failed");
        }

        if (senderCardNumber.equals(receiverCardNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account sender = repository.findByCardNumber(senderCardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        Account receiver = repository.findByCardNumber(receiverCardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        Connection connection = null;
        try {
            // Verbindung öffnen und Transaktion starten
            connection = DriverManager.getConnection("jdbc:sqlite:banking.db");
            connection.setAutoCommit(false);

            sender.withdraw(transferAmount);
            receiver.deposit(transferAmount);

            // Transaktion bestätigen
            connection.commit();

            repository.save(sender);
            repository.save(receiver);

        } catch (Exception e) {
            // Fehler: Rollback
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // Rollback-Fehler ignorieren
                }
            }
            // Original-Exception weiterleiten
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else if (e instanceof IllegalStateException) {
                throw (IllegalStateException) e;
            } else {
                throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
            }
        } finally {
            // Verbindung schließen
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Schließen-Fehler ignorieren
                }
            }
        }

    }

    @Override
    public void addIncome(String cardNumber, double amount) {
        Account account = repository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found!"));

        try {
            account.deposit(amount);
            repository.save(account);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to deposit: " + e.getMessage());
        }
    }

    @Override
    public Optional<Account> findAccountByCardNumber(String cardNumber) {
        if (!CardValidator.isValidLuhn(cardNumber)) {
            throw new IllegalArgumentException("Probably you made a mistake in the card number. Please try again!");
        }

        Optional<Account> card = repository.findByCardNumber(cardNumber);
        if (card.isEmpty()) {
            throw new IllegalStateException("Such a card does not exist.");
        }

        return card;
    }

    @Override
    public List<Account> findAllAccounts() {
        return repository.findAll();
    }

}