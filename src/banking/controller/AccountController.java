package banking.controller;

import banking.domain.Account;
import banking.service.Accountservice;

import java.util.Optional;
import java.util.Scanner;

public class AccountController {
    Accountservice service;

    public AccountController(Accountservice service) {
        this.service = service;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        Optional<Account> loggedInUser = Optional.empty();
        while (true) {
            if (loggedInUser.isEmpty()) {
                System.out.println("1. Create an account");
                System.out.println("2. Log into account");
                System.out.println("0. Exit");

                int input = scanner.nextInt();
                switch (input) {
                    case 0:
                        System.out.println("Bye!");
                        System.exit(0);
                        scanner.close();
                        break;
                    case 1:
                        Account account = service.createAccount();
                        System.out.println("Your card has been created");
                        System.out.println("Your card number:");
                        System.out.println(account.getCardNumber());
                        System.out.println("Your card PIN:");
                        System.out.println(account.getPin());
                        break;
                    case 2:
                        // code block
                        System.out.println("Enter your card number: ");
                        String loginCardNumber = scanner.next();

                        System.out.println("Enter your PIN: ");
                        String loginPin = scanner.next();


                        try {
                            loggedInUser = Optional.of(service.login(loginCardNumber, loginPin));
                            System.out.println("You have successfully logged in!");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                }
            } else {
                System.out.println("1. Balance");
                System.out.println("2. Log out");
                System.out.println("0. Exit");

                int input = scanner.nextInt();
                switch (input) {
                    case 0:
                        System.out.println("Bye!");
                        System.exit(0);
                        scanner.close();
                        break;
                    case 1:
                        try {
                            double balance = service.getBalance(loggedInUser.get());
                            System.out.println("Balance: " + balance);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                    }
                        break;
                    case 2:

                        loggedInUser = Optional.empty();
                        System.out.println("You have successfully logged out!");
                        break;
                }
            }
        }
    }

}
