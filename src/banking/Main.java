package banking;


import banking.controller.AccountController;
import banking.controller.ConsoleAccountController;
import banking.repository.AccountRepository;
import banking.repository.SqliteAccountRepository;
import banking.service.AccountService;
import banking.service.AccountServiceImpl;

public class Main {
    public static void main(String[] args) {
        AccountRepository repository = new SqliteAccountRepository(args[0]);
        AccountService service = new AccountServiceImpl(repository);
        AccountController controller = new ConsoleAccountController(service);

        controller.run();
    }
}