package ua.kpi.rrader.cdr.source;

import ua.kpi.rrader.cdr.source.util.Pair;

import java.util.Random;

public class CallGenerator {
    private PhoneBook phoneBook;
    private int time;
    private static Random random = new Random();

    public CallGenerator(PhoneBook book) {
        this.time = (int) (System.currentTimeMillis() / 1000L);
        this.phoneBook = book;
    }

    public CDR nextRecord() {
        CDR cdr = new CDR();
        int answer = random.nextInt(60 + 30);  // 1:30 avg answer
        // 4:25 avg total duration - 1:30 answer = 2:55
        int duration = random.nextInt(2*60 + 55);  // 2:55 avg talk time

        if (random.nextDouble() > 0.097)  // 9.7 % abandonment rate
            if (random.nextDouble() > 0.1)  // 10 % busy rate
                cdr.disposition = CDR.DISPOSITION_ANSWERED;
            else
                cdr.disposition = CDR.DISPOSITION_BUSY;
        else
            cdr.disposition = CDR.DISPOSITION_ABANDONED;

        cdr.start = this.time;
        cdr.answer = cdr.start + answer;
        cdr.end = cdr.start + duration;

        Pair<String, String> pair = phoneBook.nextRandomNumbersPair();
        cdr.src = pair.getLeft();
        cdr.dst = pair.getRight();
        return cdr;
    }
}
