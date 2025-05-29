package banking.repository;

import banking.domain.Account;

import java.sql.*;
import java.util.*;

public class SqliteAccountRepository implements AccountRepository {
    final String url;

    private final Map<String, Account> identityMap = new HashMap<>();

    public SqliteAccountRepository(String dbName) {
        this.url = "jdbc:sqlite:" + dbName;
        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "number TEXT, " +
                    "pin TEXT, " +
                    "balance REAL DEFAULT 0)");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Initialisieren: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(Account account) {
        String sql = "INSERT OR REPLACE INTO card (number, pin, balance) VALUES (?,?,?)";

        try (Connection con = DriverManager.getConnection(url)) {
            var statement = con.prepareStatement(sql);
            statement.setString(1, account.getCardNumber());
            statement.setString(2, account.getPin());
            statement.setDouble(3,  account.getBalance());

            statement.executeUpdate();
            // In die Identity Map speichern
            identityMap.put(account.getCardNumber(), account);
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Speichern: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(String cardNumber) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection con = DriverManager.getConnection(url)) {
            var statement = con.prepareStatement(sql);
            statement.setString(1, cardNumber);
            statement.executeUpdate();
            // Aus der Identity Map entfernen
            identityMap.remove(cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Löschen: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Account> findByCardNumber(String cardNumber) {
        if (identityMap.containsKey(cardNumber)) {
            return Optional.of(identityMap.get(cardNumber));
        }

        String sql = "SELECT number, pin, balance FROM card WHERE number = ?";

        try (Connection con = DriverManager.getConnection(url)) {
            var statement = con.prepareStatement(sql);
            statement.setString(1, cardNumber);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Account account = new Account(
                        result.getString("number"),
                        result.getString("pin"),
                        result.getDouble("balance")
                    );
                    // In die Identity Map speichern
                    identityMap.put(account.getCardNumber(), account);
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Suchen: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT number, pin, balance FROM card";
        List<Account> accounts = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url)) {
            var statement = con.createStatement();

            try (ResultSet result = statement.executeQuery(sql)) {
                while (result.next()) {
                    String cardNumber = result.getString("number");

                    // Prüfen, ob das Konto bereits in der Identity Map ist
                    if (identityMap.containsKey(cardNumber)) {
                        accounts.add(identityMap.get(cardNumber));
                    } else {
                        Account account = new Account(
                                cardNumber,
                                result.getString("pin"),
                                result.getDouble("balance")
                        );
                        identityMap.put(cardNumber, account);
                        accounts.add(account);
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Datenbankfehler beim Abrufen aller Konten: " + e.getMessage(), e);
        }
        return accounts;
    }
}