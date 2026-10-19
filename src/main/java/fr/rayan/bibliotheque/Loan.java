package fr.rayan.bibliotheque;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Loan(Book book, Member member, LocalDate borrowedOn, LocalDate dueOn) {
    public boolean isOverdue(LocalDate today) {
        return today.isAfter(dueOn);
    }

    public long daysLate(LocalDate today) {
        return Math.max(0, ChronoUnit.DAYS.between(dueOn, today));
    }
}
