package banking.repository;

import banking.domain.Account;

import java.util.*;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> store = new HashMap<>();

    public void save(Account account) {
        store.put(account.getCardNumber(), account);
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return Optional.ofNullable(store.get(cardNumber));
    }

    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }

}
