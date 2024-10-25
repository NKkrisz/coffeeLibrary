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
    @FXML private TextField searchField;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
    private ObservableList<Loan> loans = FXCollections.observableArrayList();

    public void initialize() {
        bookListView.setItems(books);
        borrowerListView.setItems(borrowers);
        loanListView.setItems(loans);

        bookListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateBookFields(newValue);
            } else {
                clearBookFields();
            }
        });

        loadSampleBooks();
    }

    private void loadSampleBooks() {
        books.add(new Book("1984", "George Orwell", "9780451524935", 5));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", 3));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 4));
        books.add(new Book("Moby Dick", "Herman Melville", "9781503280786", 2));
        books.add(new Book("War and Peace", "Leo Tolstoy", "9781420954302", 1));
    }

    private void populateBookFields(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        copiesField.setText(String.valueOf(book.getCopies()));
    }

    @FXML
    private void addBook() {
        if (isBookInputValid()) {
            Book book = new Book(titleField.getText(), authorField.getText(), isbnField.getText(), Integer.parseInt(copiesField.getText()));
            books.add(book);
            clearBookFields();
        } else {
            showAlert("Invalid Input", "Please fill in all fields correctly.");
        }
    }

    private boolean isBookInputValid() {
        return !titleField.getText().isEmpty() &&
                !authorField.getText().isEmpty() &&
                !isbnField.getText().isEmpty() &&
                !copiesField.getText().isEmpty() &&
                isNumeric(copiesField.getText());
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void editBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null && isBookInputValid()) {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setIsbn(isbnField.getText());
            selectedBook.setCopies(Integer.parseInt(copiesField.getText()));
            bookListView.refresh();
            clearBookFields();
        } else {
            showAlert("Invalid Input", "Please fill in all fields correctly.");
        }
    }

    @FXML
    private void deleteBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            books.remove(selectedBook);
            clearBookFields();
        }
    }

    @FXML
    private void searchBookByTitle() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            bookListView.setItems(books);
        } else {
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList(
                    books.filtered(book -> book.getTitle().toLowerCase().contains(searchTerm))
            );
            bookListView.setItems(filteredBooks);
        }
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
