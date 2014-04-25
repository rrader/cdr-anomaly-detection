package ua.kpi.rrader.cdr.source;

import java.util.Random;

public class Caller implements CallGenerator {
    private String src;
    private PhoneBook phoneBook;
    private static Random random = new Random();
    private int startTime = 0;

    public Caller(String src, PhoneBook book) {
        this.phoneBook = book;
        this.src = src;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public CDR nextRecord() {
        return nextRecord(this.startTime);
    }

    public CDR nextRecord(int startTime) {
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

        cdr.start = startTime;
        cdr.answer = cdr.start + answer;
        cdr.end = cdr.start + answer + duration;

        cdr.src = this.src;
        cdr.dst = phoneBook.nextDestinationNumber(this.src);
        return cdr;
    }
}
