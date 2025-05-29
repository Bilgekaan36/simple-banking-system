package banking.repository;

import banking.domain.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public void save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.put(account.getCardNumber(), account);
    }

    @Override
    public void remove(String cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Card number cannot be null");
        }
        accounts.remove(cardNumber);
    }

    @Override
    public Optional<Account> findByCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(accounts.get(cardNumber));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}