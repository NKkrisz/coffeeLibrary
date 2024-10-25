package dev.nkkrisz.coffeelibrary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML private TextField titleField, authorField, isbnField, copiesField;
    @FXML private TextField borrowerNameField, borrowerContactField;
    @FXML private DatePicker loanStartDate, loanEndDate;
    @FXML private ListView<Book> bookListView;
    @FXML private ListView<Borrower> borrowerListView;
    @FXML private ListView<Loan> loanListView;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
    private ObservableList<Loan> loans = FXCollections.observableArrayList();

    public void initialize() {
        bookListView.setItems(books);
        borrowerListView.setItems(borrowers);
        loanListView.setItems(loans);
    }

    @FXML
    private void addBook() {
        Book book = new Book(titleField.getText(), authorField.getText(), isbnField.getText(), Integer.parseInt(copiesField.getText()));
        books.add(book);
        clearBookFields();
    }

    @FXML
    private void editBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setIsbn(isbnField.getText());
            selectedBook.setCopies(Integer.parseInt(copiesField.getText()));
            bookListView.refresh();
            clearBookFields();
        }
    }

    @FXML
    private void deleteBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) books.remove(selectedBook);
    }

    @FXML
    private void searchBookByTitle() {
        String title = titleField.getText().toLowerCase();
        bookListView.setItems(FXCollections.observableArrayList(
                books.filtered(book -> book.getTitle().toLowerCase().contains(title))
        ));
    }

    @FXML
    private void addBorrower() {
        Borrower borrower = new Borrower(borrowerNameField.getText(), borrowerContactField.getText());
        borrowers.add(borrower);
        clearBorrowerFields();
    }

    @FXML
    private void editBorrower() {
        Borrower selectedBorrower = borrowerListView.getSelectionModel().getSelectedItem();
        if (selectedBorrower != null) {
            selectedBorrower.setName(borrowerNameField.getText());
            selectedBorrower.setContact(borrowerContactField.getText());
            borrowerListView.refresh();
            clearBorrowerFields();
        }
    }

    @FXML
    private void deleteBorrower() {
        Borrower selectedBorrower = borrowerListView.getSelectionModel().getSelectedItem();
        if (selectedBorrower != null) borrowers.remove(selectedBorrower);
    }

    @FXML
    private void addLoan() {
        Borrower selectedBorrower = borrowerListView.getSelectionModel().getSelectedItem();
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBorrower != null && selectedBook != null) {
            Loan loan = new Loan(selectedBorrower, selectedBook, loanStartDate.getValue(), loanEndDate.getValue());
            loans.add(loan);
            clearLoanFields();
        }
    }

    private void clearBookFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        copiesField.clear();
    }

    private void clearBorrowerFields() {
        borrowerNameField.clear();
        borrowerContactField.clear();
    }

    private void clearLoanFields() {
        loanStartDate.setValue(null);
        loanEndDate.setValue(null);
    }
}
