package banking.controller;

import banking.domain.Account;
import banking.service.AccountService;

import java.util.Scanner;

public class ConsoleAccountController implements AccountController {
    private final AccountService service;
    private String loggedInCardNumber;
    private final Scanner scanner;
    private boolean isRunning;

    public ConsoleAccountController(AccountService service) {
        this.service = service;
        this.loggedInCardNumber = null;
        this.scanner = new Scanner(System.in);
        this.isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            // Deine bestehende Menülogik
            if (isLoggedIn()) {
                showLoggedInMenu();
            } else {
                showMainMenu();
            }
        }
    }

    @Override
    public void shutdown() {
        isRunning = false;
        scanner.close();
        System.out.println("Bye!");
    }

    @Override
    public boolean login(String cardNumber, String pin) {
        try {
            service.login(cardNumber, pin);
            loggedInCardNumber = cardNumber;
            System.out.println("You have successfully logged in!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void logout() {
        loggedInCardNumber = null;
        System.out.println("You have successfully logged out!");
    }

    @Override
    public String[] createNewAccount() {
        Account account = service.createAccount();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPin());

        return new String[]{account.getCardNumber(), account.getPin()};
    }

    @Override
    public boolean showBalance() {
        try {
            double balance = service.getAccountBalance(loggedInCardNumber);
            System.out.println("Balance: " + (int)balance);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addIncome(double amount) {
        try {
            service.addIncome(loggedInCardNumber, amount);
            System.out.println("Income was added!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean doTransfer(String receiverCardNumber, double amount) {
        try {
            service.transfer(loggedInCardNumber, receiverCardNumber, amount);
            System.out.println("Success!");
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean closeAccount() {
        try {
            service.closeAccount(loggedInCardNumber);
            logout();
            System.out.println("The account has been closed!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isLoggedIn() {
        return loggedInCardNumber != null;
    }

    @Override
    public String getCurrentCardNumber() {
        return loggedInCardNumber;
    }

    // Private Hilfsmethoden
    private void showMainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        int input = scanner.nextInt();
        processMainMenuInput(input);
    }

    private void showLoggedInMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        int input = scanner.nextInt();
        processLoggedInMenuInput(input);
    }

    private void processMainMenuInput(int input) {
        switch (input) {
            case 0:
                shutdown();
                System.exit(0);
                break;
            case 1:
                createNewAccount();
                break;
            case 2:
                System.out.println("Enter your card number: ");
                String cardNumber = scanner.next();
                System.out.println("Enter your PIN: ");
                String pin = scanner.next();
                login(cardNumber, pin);
                break;
            default:
                System.out.println("Wrong input!");
                break;
        }
    }

    private void processLoggedInMenuInput(int input) {
        switch (input) {
            case 0:
                shutdown();
                System.exit(0);
                break;
            case 1:
                showBalance();
                break;
            case 2:
                System.out.println("Enter income: ");
                double amount = scanner.nextDouble();
                addIncome(amount);
                break;
            case 3:
                System.out.println("Transfer");
                System.out.println("Enter card number: ");
                String receiverCardNumber = scanner.next();

                try {
                    service.findAccountByCardNumber(receiverCardNumber);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                System.out.println("Enter how much money you want to transfer: ");
                double transferAmount = scanner.nextDouble();
                doTransfer(receiverCardNumber, transferAmount);
                break;
            case 4:
                closeAccount();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Wrong input!");
                break;
        }
    }
}