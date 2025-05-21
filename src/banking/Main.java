package banking;


import banking.controller.AccountController;
import banking.repository.AccountRepository;
import banking.repository.InMemoryAccountRepository;
import banking.service.Accountservice;

public class Main {
    public static void main(String[] args) {
        AccountRepository repository = new InMemoryAccountRepository();
        Accountservice service = new Accountservice(repository);
        AccountController controller = new AccountController(service);

        controller.run();
    }
}