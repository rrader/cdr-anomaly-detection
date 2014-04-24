package ua.kpi.rrader.cdr.source;

import ua.kpi.rrader.cdr.source.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class PhoneBook extends ArrayList<String> {
    public static final String prefix = "+38000";
    private static Random random = new Random();

    public static PhoneBook generatePhoneBook(int count) {
        PhoneBook phoneBook = new PhoneBook();
        for(int i=0; i<count; i++) {
            String number = String.format("%s%07d", prefix, random.nextInt(10000000));
            phoneBook.add(number);
        }
        return phoneBook;
    }

    /**
     * Generate random number from phone book
     * @return random number from phone book
     */
    public String nextRandomNumber() {
        return this.get(random.nextInt(size()));
    }

    /**
     * Generate pair of different numbers from phone book
     * @return pair of numbers
     */
    public Pair<String, String> nextRandomNumbersPair() {
        String num1 = nextRandomNumber();
        String num2 = nextRandomNumber();
        while(num1.compareTo(num2) == 0) {
            num2 = nextRandomNumber();
        }
        return new Pair<String, String>(num1, num2);
    }
}
