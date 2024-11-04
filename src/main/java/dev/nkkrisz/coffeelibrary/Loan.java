package dev.nkkrisz.coffeelibrary;

import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class Loan {
    private SimpleObjectProperty<Borrower> borrower;
    private SimpleObjectProperty<Book> book;
    private SimpleObjectProperty<LocalDate> startDate;
    private SimpleObjectProperty<LocalDate> endDate;

    public Loan(Borrower borrower, Book book, LocalDate startDate, LocalDate endDate) {
        this.borrower = new SimpleObjectProperty<>(borrower);
        this.book = new SimpleObjectProperty<>(book);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public Book getBook() {
        return book.get();
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    @Override
    public String toString() {
        return "Loan: " + getBook().getTitle() + " to " + getBorrower().getName() +
                " from " + getStartDate() + " to " + getEndDate();
    }
}
