import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UsernameChecker {

    private final ConcurrentHashMap<String, String> usernameMap;
    private final ConcurrentHashMap<String, AtomicInteger> attemptCountMap;

    public UsernameChecker() {
        usernameMap = new ConcurrentHashMap<>();
        attemptCountMap = new ConcurrentHashMap<>();
    }

    public boolean registerUser(String username, String userId) {
        return usernameMap.putIfAbsent(username, userId) == null;
    }

    public boolean checkAvailability(String username) {
        attemptCountMap
                .computeIfAbsent(username, k -> new AtomicInteger(0))
                .incrementAndGet();
        return !usernameMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        if (!usernameMap.containsKey(username)) {
            suggestions.add(username);
            return suggestions;
        }

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        if (username.contains("_")) {
            String modified = username.replace("_", ".");
            if (!usernameMap.containsKey(modified)) {
                suggestions.add(modified);
            }
        }

        return suggestions;
    }

    public String getMostAttempted() {
        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, AtomicInteger> entry : attemptCountMap.entrySet()) {
            int count = entry.getValue().get();
            if (count > max) {
                max = count;
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted == null ? null : mostAttempted + " (" + max + " attempts)";
    }

    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();

        checker.registerUser("john_doe", "101");
        checker.registerUser("admin", "102");

        System.out.println(checker.checkAvailability("john_doe"));
        System.out.println(checker.checkAvailability("jane_smith"));
        System.out.println(checker.suggestAlternatives("john_doe"));

        for (int i = 0; i < 10543; i++) {
            checker.checkAvailability("admin");
        }

        System.out.println(checker.getMostAttempted());
    }
}

