package banking.domain;
import java.util.Random;

public class Account {
    private final String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = 0;
    }

    public void changePin(String newPin) {
        if(!newPin.matches("//d{4}")) {
            throw new IllegalArgumentException("Neue PIN muss genau 4 Ziffern lang sein!");
        }
        this.pin = newPin;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0 || amount > balance) throw new IllegalArgumentException("Invalid withdraw amount");
        this.balance -= amount;
    }


    public boolean isCorrectPin(String attempt) {
        return this.pin.equals(attempt);
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public double getBalance() {
        return this.balance;
    }

}
