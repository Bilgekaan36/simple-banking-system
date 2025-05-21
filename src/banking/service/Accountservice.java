package banking.service;

import banking.domain.Account;
import banking.repository.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Accountservice {
    private static final String BIN = "400000";
    private static final Random RND = new Random();

    AccountRepository repository;

    public Accountservice(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount() {
        String card = generateCardNumber();
        String pin = generatePin();
        Account account = new Account(card, pin);
        repository.save(account);
        return account;
    }

    public String generateCardNumber() {
        String accountIdentifier = String.format("%09d", RND.nextInt(1_000_000_000));
        String cardWithoutChecksum = BIN + accountIdentifier;
        int checksum = calculateLuhnChecksum(cardWithoutChecksum);
        return cardWithoutChecksum + checksum;
    }

    private int calculateLuhnChecksum(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if ((i % 2) == 0) digit *= 2;
            if (digit > 9) digit -= 9;
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }

    public String generatePin() {
        return String.format("%04d", RND.nextInt(10000));
    }

    public Account login(String cardNumber, String pin) {
        Optional<Account> optional = repository.findByCardNumber(cardNumber);
        if(optional.isPresent()) {
            Account account = optional.get();
            if(account.isCorrectPin(pin)){
                return account;
            }
        }
        throw new IllegalArgumentException("Wrong card number or PIN!");
    }

    public double getBalance(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is not logged in!");
        }
        return account.getBalance();
    }

    public Optional<Account> findAccountByCardNumber(String cardNumber) {
        return repository.findByCardNumber(cardNumber);
    }

    public List<Account> findAllAccounts() {
        return repository.findAll();
    }

    //TODO:
    // changePin
    // getBalance
}
