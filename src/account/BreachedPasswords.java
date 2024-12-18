package account;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BreachedPasswords {
    private ArrayList<String> breachedPasswords;

    public BreachedPasswords() {
        this.breachedPasswords = new ArrayList<>(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));
    }

    public List<String> listOfBreachedPasswords() {

        List<String> breachedPasswords = new ArrayList<>(this.breachedPasswords);

        return breachedPasswords;
    }

    @Override
    public String toString() {
        return "BreachedPasswords{" +
                "breachedPasswords=" + breachedPasswords +
                '}';
    }
}
