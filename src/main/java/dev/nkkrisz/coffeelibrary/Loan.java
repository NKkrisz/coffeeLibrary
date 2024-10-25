package dev.nkkrisz.coffeelibrary;

import java.time.LocalDate;

public class Loan {
    private Borrower borrower;
    private Book book;
    private LocalDate startDate;
    private LocalDate endDate;

    public Loan(Borrower borrower, Book book, LocalDate startDate, LocalDate endDate) {
        this.borrower = borrower;
        this.book = book;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Borrower getBorrower() { return borrower; }
    public Book getBook() { return book; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    @Override
    public String toString() { return borrower + " borrowed " + book + " from " + startDate + " to " + endDate; }
}

