package View;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class View {

    private static TextInputDialog folioName;
    private static TextInputDialog folioID;
    private static TableView<HoldingInterface> currentTable;
    private static FolioInterface currentFolio;

    /**
     * Pop-Up WARNING for user -> Used when the user uses a negative number
     */
    public static void addStockSharesFormatError() {
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setContentText("Number of shares should be a positive integer!");
        a.showAndWait();
    }

    /**
     * Pop-Up WARNING for user -> Used when the user does not fill in every text field
     */
    public static void addStockError() {
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setContentText("All fields are required to add new stock to the folio!");
        a.showAndWait();
    }

    /**
     * Pop-Up WARNING for user -> Used when the user has entered a stock that does not exist
     */
    public static void stockNotFoundError() {
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setContentText("This stock does not exist on the NYSE!");
        a.showAndWait();
    }

    /**
     * Pop-Up WARNING for user -> Used when the user enters a folio name that is already taken
     */
    public static void takenNameError() {
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setContentText("This Folio name is taken!\nPlease try again!");
        a.showAndWait();
    }


    /**
     * Pop-Up INFORMATION for user -> Used to inform the user that their folio has been successfully saved
     */
    public static void successfulSave() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setContentText("Folio has been successfully save!");
        a.showAndWait();
    }

    /**
     * Pop-Up INFORMATION for user -> Used to inform the user that their folio has been successfully deleted
     */
    public static void fileDelete() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setContentText("Folio has been successfully deleted!");
        a.showAndWait();

    }

    /**
     * Pop-Up INFORMATION for user -> Used to inform the user that their folio has been unsuccessfully saved
     */
    public static void unsuccessfulFolioDelete() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setContentText("A folio with the given ID doesn't exist!");
        a.showAndWait();

    }

    /**
     * Pop-Up ARE YOU SURE? for user -> Used ensure the user is sure about their decision
     * Different cases for different situations
     */
    public static Boolean areYouSure(int num) {
        Alert a = new Alert(Alert.AlertType.NONE);
        final Boolean[] continued = {false};

        a.setHeaderText("Are You Sure?");
        a.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        switch (num) {
            case 1:
                a.setContentText("Are you sure you wish to close the Folio?");
                continued[0] = btnCheck(a, continued);
                break;
            case 2:
                a.setContentText("Are you sure you wish to delete the Folio?");
                continued[0] = btnCheck(a, continued);
                break;
            case 3:
                a.setContentText("Are you sure you want to delete the selected Stock?");
                continued[0] = btnCheck(a, continued);
                break;
            case 4:
                a.setContentText("Are you sure? Selling this amount will remove the stock entirely!");
                continued[0] = btnCheck(a, continued);
                break;
        }
        return continued[0];
    }

    /**
     * Button check upon the above decision so that the right action will occur
     */
    public static Boolean btnCheck(Alert a, Boolean[] continued) {
        a.showAndWait().ifPresent((btnType) -> {
            if (btnType == ButtonType.YES) {
                continued[0] = true;
            } else if (btnType == ButtonType.NO) {
                continued[0] = false;
            }
        });
        return continued[0];
    }


    /**
     * When the respective edit stock button is clicked the correct edit stock scene will be show via this method
     *
     * @param currentTable -> this being the table edited to ensure the edits are to that table
     * @throws IOException
     */
    public static void showEditStock(TableView<HoldingInterface> currentTable) throws IOException {
        setCurrentTable(currentTable);
        Parent editStock = FXMLLoader.load(View.class.getResource("../View/EditStockView.fxml"));
        Scene editStockScene = new Scene(editStock);

        Stage window = new Stage();
        window.setTitle("Edit Stock");

        window.setScene(editStockScene);
        window.showAndWait();
    }

    /**
     * Dialog box for user to enter the name of the folio they which to Create/Close
     *
     * @return -> String i.e. the name the user enters
     */
    public static String showFolio() {
        folioName = new TextInputDialog();
        folioName.setHeaderText("Enter the Folio name:");

        Optional<String> nameFolio = folioName.showAndWait();

        if (nameFolio.isPresent()) {
            return folioName.getEditor().getText();
        }
        return "";
    }

    /**
     * Dialog box for user to enter the ID of the folio they which to Save/Delete/Load
     *
     * @return -> String i.e. the folio ID entered
     */
    public static String showFolioID() {
        folioID = new TextInputDialog();
        folioID.setHeaderText("Enter the Folio ID:");

        Optional<String> idFolio = folioID.showAndWait();

        if (idFolio.isPresent()) {
            return folioID.getEditor().getText();
        }
        return "";
    }

    /**
     * Creation of table which will hold the Holdings
     *
     * @return -> the newly created table
     */
    public static TableView<HoldingInterface> createTable() {
        TableView<HoldingInterface> table = new TableView<>();

        TableColumn<HoldingInterface, String> tickerSymbol = new TableColumn<>("Ticker Symbol");
        TableColumn<HoldingInterface, String> stockName = new TableColumn<>("Stock Name");
        TableColumn<HoldingInterface, String> pricePerShare = new TableColumn<>("Price Per Share");
        TableColumn<HoldingInterface, String> noOfShares = new TableColumn<>("No. of Shares");
        TableColumn<HoldingInterface, String> valueOfHolding = new TableColumn<>("Value of Holding");
        TableColumn<HoldingInterface, String> stockPrice = new TableColumn<>("Stock Price");

        tickerSymbol.prefWidthProperty().bind(table.widthProperty().multiply(0.166));
        stockName.prefWidthProperty().bind(table.widthProperty().multiply(0.166));
        noOfShares.prefWidthProperty().bind(table.widthProperty().multiply(0.166));
        pricePerShare.prefWidthProperty().bind(table.widthProperty().multiply(0.166));
        valueOfHolding.prefWidthProperty().bind(table.widthProperty().multiply(0.166));
        stockPrice.prefWidthProperty().bind(table.widthProperty().multiply(0.166));

        table.getColumns().addAll(tickerSymbol, stockName, noOfShares, pricePerShare, valueOfHolding, stockPrice);

        tickerSymbol.setCellValueFactory(new PropertyValueFactory<>("tickerSymbol"));
        stockName.setCellValueFactory(new PropertyValueFactory<>("name"));
        noOfShares.setCellValueFactory(new PropertyValueFactory<>("noOfShares"));
        pricePerShare.setCellValueFactory(new PropertyValueFactory<>("price"));
        valueOfHolding.setCellValueFactory(new PropertyValueFactory<>("value"));
        stockPrice.setCellValueFactory(new PropertyValueFactory<>("upOrDown"));

        return table;
    }

    /**
     * Loads a specified folio anf populates the given table
     * Responds with WARNING Pop-Up if error occurs
     *
     * @param loaded       -> Method that indicates that the folio has loaded
     * @param table        -> Table to load the holdings stored in the loaded folio into
     * @param loadingFolio -> Allows for the holdings to be obtained
     */
    public static void loadFolio(Boolean loaded, TableView<HoldingInterface> table, FolioInterface loadingFolio) {
        if (loaded) {
            ArrayList<HoldingInterface> stocks = loadingFolio.getStocks();
            for (HoldingInterface stock : stocks) {
                table.getItems().add(stock);
            }
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);

            a.setContentText("No saved folios to load from...\nCreating new folio...");
            a.showAndWait();
        }
    }

    /**
     * Adds the users specified stock to the given table
     *
     * @param newStock -> The new Stock that is to be added
     * @param table    -> The currentTable that the stock is to be added too
     */
    public static void addStocks(HoldingInterface newStock, TableView<HoldingInterface> table) {

        //if(getCurrentFolio().addHolding(newStock.getName(), newStock.getTickerSymbol(), newStock.getNoOfShares())) {
        table.getItems().add(newStock);

    }

    /**
     * Edits the selected stock and updates the given table
     *
     * @param stock     -> Selected stock
     * @param table     -> CurrentTable
     * @param stockName -> Name of the edited stock
     */
    public static void editedStocks(HoldingInterface stock, TableView<HoldingInterface> table, String stockName) {
        if (table.getItems().remove(currentFolio.getStock(stockName))) {
            stock.setHoldingValue();

            table.getItems().add(stock);
        }
    }

    /**
     * Refreshes all the prices of all the stock within the given table i.e. folio
     *
     * @param table -> CurrentTable to refresh its current prices
     */
    public static void updateStockPrices(TableView<HoldingInterface> table) {
        for (int i = 0; i < table.getItems().size(); i++) {
            table.getItems().get(i).refreshPrices();
            table.getItems().get(i).refreshUpOrDown();
            HoldingInterface updated = table.getItems().get(i);
            table.getItems().remove(i);
            table.getItems().add(updated);
        }
    }

    /**
     * Sets the current table in use
     *
     * @param table -> currentTable
     */
    private static void setCurrentTable(TableView<HoldingInterface> table) {
        currentTable = table;
    }

    /**
     * Retrieves the current table
     *
     * @return -> currentTable
     */
    public static TableView<HoldingInterface> getCurrentTable() {
        return currentTable;
    }

    /**
     * Sets the current folio in use
     *
     * @param folio -> currentFolio
     */
    public static void setCurrentFolio(FolioInterface folio) {
        currentFolio = folio;
    }

    /**
     * Retrieves the currentFolio
     *
     * @return -> currentFolio
     */
    public static FolioInterface getCurrentFolio() {
        return currentFolio;
    }
}
