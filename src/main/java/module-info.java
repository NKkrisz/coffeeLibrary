module dev.nkkrisz.coffeelibrary {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.nkkrisz.coffeelibrary to javafx.fxml;
    exports dev.nkkrisz.coffeelibrary;
}