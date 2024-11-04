package dev.nkkrisz.coffeelibrary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.time.LocalDate;

public class Controller {
    @FXML private TextField titleField, authorField, isbnField, copiesField;
    @FXML private TextField borrowerNameField, borrowerContactField;
    @FXML private DatePicker loanStartDate, loanEndDate;
    @FXML private ListView<Book> bookListView;
    @FXML private ListView<Borrower> borrowerListView;
    @FXML private ListView<Loan> loanListView;
    @FXML private TextField searchField;
    @FXML private ComboBox<Book> bookComboBox;
    @FXML private ComboBox<Borrower> borrowerComboBox;
    @FXML private Button editLoanButton;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Borrower> borrowers = FXCollections.observableArrayList();
    private ObservableList<Loan> loans = FXCollections.observableArrayList();

    public void initialize() {
        bookListView.setItems(books);
        borrowerListView.setItems(borrowers);
        loanListView.setItems(loans);
        bookComboBox.setItems(books);
        borrowerComboBox.setItems(borrowers);

        bookListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) populateBookFields(newValue);
            else clearBookFields();
        });

        borrowerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) populateBorrowerFields(newValue);
            else clearBorrowerFields();
        });

        loanListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) populateLoanFields(newValue);
            else clearLoanFields();
        });

        loadSampleBooks();
        loadSampleBorrowers();

        // Disable Edit Loan button initially
        editLoanButton.setDisable(true);

        // Enable the Edit button only when a loan is selected
        loanListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editLoanButton.setDisable(newValue == null);
        });
    }

    private void loadSampleBooks() {
        books.addAll(
                new Book("1984", "George Orwell", "9780451524935", 5),
                new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", 3)
                // Additional books if needed
        );
    }

    private void loadSampleBorrowers() {
        borrowers.addAll(
                new Borrower("Alice", "alice@example.com"),
                new Borrower("Bob", "bob@example.com")
                // Additional borrowers if needed
        );
    }

    private void populateBookFields(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        copiesField.setText(String.valueOf(book.getCopies()));
    }

    private void populateBorrowerFields(Borrower borrower) {
        borrowerNameField.setText(borrower.getName());
        borrowerContactField.setText(borrower.getContact());
    }

    private void populateLoanFields(Loan loan) {
        bookComboBox.getSelectionModel().select(loan.getBook());
        borrowerComboBox.getSelectionModel().select(loan.getBorrower());
        loanStartDate.setValue(loan.getStartDate());
        loanEndDate.setValue(loan.getEndDate());
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
    private void searchBookByTitle(KeyEvent event) {
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
        Book selectedBook = bookComboBox.getSelectionModel().getSelectedItem();
        Borrower selectedBorrower = borrowerComboBox.getSelectionModel().getSelectedItem();

        if (selectedBook != null && selectedBorrower != null && isLoanDateValid()) {
            if (selectedBook.getCopies() > 0) {
                Loan loan = new Loan(selectedBorrower, selectedBook, loanStartDate.getValue(), loanEndDate.getValue());
                loans.add(loan);
                selectedBook.setCopies(selectedBook.getCopies() - 1);
                bookListView.refresh();
                clearLoanFields();
            } else {
                showAlert("Unavailable", "No copies of the selected book are available.");
            }
        } else {
            showAlert("Invalid Input", "Please select a book, a borrower, and provide valid loan dates.");
        }
    }

    @FXML
    private void editLoan() {
        Loan selectedLoan = loanListView.getSelectionModel().getSelectedItem();
        Book selectedBook = bookComboBox.getSelectionModel().getSelectedItem();
        Borrower selectedBorrower = borrowerComboBox.getSelectionModel().getSelectedItem();

        if (selectedLoan != null && selectedBook != null && selectedBorrower != null && isLoanDateValid()) {
            // Restore the book's available copies before updating the loan
            selectedLoan.getBook().setCopies(selectedLoan.getBook().getCopies() + 1); // Return the previous book copy

            // Update loan properties
            selectedLoan.setBook(selectedBook);
            selectedLoan.setBorrower(selectedBorrower);
            selectedLoan.setStartDate(loanStartDate.getValue());
            selectedLoan.setEndDate(loanEndDate.getValue());

            // Deduct a copy from the new selected book
            selectedBook.setCopies(selectedBook.getCopies() - 1);
            loanListView.refresh(); // Refresh loan list view
            bookListView.refresh();  // Refresh book list view
            clearLoanFields();
            showAlert("Success", "Loan edited successfully.");
        } else {
            showAlert("Invalid Input", "Please select a loan, book, borrower, and valid dates to edit.");
        }
    }

    @FXML
    private void deleteLoan() {
        Loan selectedLoan = loanListView.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            selectedLoan.getBook().setCopies(selectedLoan.getBook().getCopies() + 1); // Return book copy
            loans.remove(selectedLoan);
            bookListView.refresh(); // Refresh the book list
            clearLoanFields();
        }
    }

    private boolean isLoanDateValid() {
        if (loanStartDate.getValue() == null || loanEndDate.getValue() == null) return false;
        if (loanEndDate.getValue().isBefore(loanStartDate.getValue())) {
            showAlert("Invalid Date", "End date cannot be earlier than start date.");
            return false;
        }
        return true;
    }

    private boolean isBookInputValid() {
        return !titleField.getText().isEmpty() &&
                !authorField.getText().isEmpty() &&
                !isbnField.getText().isEmpty() &&
                !copiesField.getText().isEmpty();
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
        bookComboBox.getSelectionModel().clearSelection();
        borrowerComboBox.getSelectionModel().clearSelection();
        loanStartDate.setValue(null);
        loanEndDate.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
