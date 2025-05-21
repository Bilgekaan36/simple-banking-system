package banking.repository;

import banking.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findByCardNumber(String cardNumber);
    List<Account> findAll();
}
