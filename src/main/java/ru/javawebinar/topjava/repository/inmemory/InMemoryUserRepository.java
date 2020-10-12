package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger(0);


    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user != null) {
            user.setId(count.incrementAndGet());
            repository.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted(new Sort())
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.entrySet().stream()
                .filter(users -> users.getValue().getEmail().equals(email))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    private class Sort implements Comparator<User>{

        @Override
        public int compare(User user1, User user2) {
            if (user1.getName().equals(user2.getName())){
                return user1.getEmail().compareTo(user2.getEmail());
            }
            return user1.getName().compareTo(user2.getName());
        }
    }
}
