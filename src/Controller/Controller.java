package Controller;

import Model.*;
import View.View;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Controller {

    @FXML private TabPane tabPane;
    @FXML private TextField tickerID, noOfShares, stockName;
    @FXML private Label totalValue1, folioID;
    @FXML private Tab newTab, howTo;
    private FolioHolder folioHolder = new FolioHolder();

    /**
     * Initializer for listeners/events/timers. Activates listeners and events and begins the timer and handles the tasks
     * they are to do if activated/timer indicates.
     */
    @FXML
    private void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    try {
                        totalValue1.setText(Double.toString(folioHolder.getFolio(tabPane.getSelectionModel().getSelectedItem().getText()).getTotalHolding()));
                        folioID.setText(folioHolder.getFolio(tabPane.getSelectionModel().getSelectedItem().getText()).getFolioID());
                    } catch (NullPointerException ignored) {

                    }
                }
        );

        EventHandler<Event> event = e -> {
            if (newTab.isSelected()) {
                if(tabPane.getTabs().size() > 1) {
                    createFolio();
                }
            }
        };

        newTab.setOnSelectionChanged(event);

        Date currentDate = new Date();
        Timer refreshRate = new Timer();
        TimerTask autoRefresh = new TimerTask() {
            @Override
            public void run() {
                if (folioHolder.getExistingFolios().size() > 0) {
                    try {
                        View.updateStockPrices((TableView<HoldingInterface>) tabPane.getSelectionModel().getSelectedItem().getContent());
                    } catch (ClassCastException ignored) {

                    }
                }
            }
        };
        refreshRate.scheduleAtFixedRate(autoRefresh, currentDate, 30000);
    }

    /**
     * Handles when the user wishes to add stock by ensuring the information inputted by the user is valid and exits.
     * After all the checks are preformed the stock, if valid, is added.
     */
    @FXML
    void addNewStock() {
        int holdingSize = 0;
        boolean set = true;

        if (!noOfShares.getText().equals("") && !tickerID.getText().equals("") && !stockName.getText().equals("")) {
            TableView<HoldingInterface> currentTable = (TableView<HoldingInterface>) tabPane.getSelectionModel().getSelectedItem().getContent();
            String name = tabPane.getSelectionModel().getSelectedItem().getText();
            try {
                holdingSize = folioHolder.getFolio(name).getStocks().size();
            }catch(NullPointerException ignored){}

            boolean notNumber = false;
            String shares = " ";

            for (int i = 0; i < noOfShares.getText().length(); i++) {
                char character = noOfShares.getText().trim().charAt(i);
                notNumber = !(Character.isDigit(character));

            }
            if (notNumber) {
                View.addStockSharesFormatError();
                try {
                    notNumber = true;
                } catch (NumberFormatException ignored) {

                }
            } else {
                shares = noOfShares.getText();

            }

            if (shares == " ") {
                set = false;
            }

            if (set) {
                if (folioHolder.getFolio(name).addHolding(stockName.getText(), tickerID.getText(), Integer.parseInt(shares))) {
                    if (holdingSize != folioHolder.getFolio(name).getStocks().size()) {
                        View.addStocks(folioHolder.getFolio(name).getStock(tickerID.getText()), currentTable);
                    }

                    totalValue1.setText(Double.toString(folioHolder.getFolio(name).getTotalHolding()));

                    tickerID.setText("");
                    stockName.setText("");
                    noOfShares.setText("");
                } else {
                    View.stockNotFoundError();
                }
            }
        } else {
            View.addStockError();
        }

    }

    /**
     * Handles the process of closing a folio i.e. tab when the user wishes.
     */
    @FXML
    void closeFolio() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (View.areYouSure(1)) {
            tabPane.getTabs().remove(selectedTab);
        }
    }

    /**
     * Used when the user wishes to deleted a selected stock. A validation check is used to ensure the user
     * wishes to proceed with their action.
     */
    @FXML
    void deleteStock() {
        TableView<HoldingInterface> currentTable = new TableView<>();

        try {
            currentTable = (TableView<HoldingInterface>) tabPane.getSelectionModel().getSelectedItem().getContent();
        } catch (ClassCastException ignored) {

        }

        HoldingInterface selectedStock = currentTable.getSelectionModel().getSelectedItem();
        String name = tabPane.getSelectionModel().getSelectedItem().getText();

        if (View.areYouSure(3)) {
            currentTable.getItems().removeAll(selectedStock);
            try {
                totalValue1.setText(Double.toString(folioHolder.getFolio(name).getTotalHolding()));
                folioHolder.getFolio(tabPane.getSelectionModel().getSelectedItem().getText()).deleteStock(selectedStock.getTickerSymbol());
                totalValue1.setText(Double.toString(folioHolder.getFolio(name).getTotalHolding()));
            } catch (NullPointerException ignored) {

            }
        }
    }

    /**
     * Handles when a user wishes to edit a selected stock which in turn calls the correct view elements.
     *
     * @throws IOException
     */
    @FXML
    void editStock() throws IOException {
        String name = tabPane.getSelectionModel().getSelectedItem().getText();
        TableView<HoldingInterface> currentTable = null;

        try {
            currentTable = (TableView<HoldingInterface>) tabPane.getSelectionModel().getSelectedItem().getContent();
        } catch (ClassCastException ignored) {

        }

        View.setCurrentFolio(folioHolder.getFolio(name));
        View.showEditStock(currentTable);

        totalValue1.setText(Double.toString(folioHolder.getFolio(name).getTotalHolding()));
    }

    /**
     * Used when a user may of accidentally closed the "how to" tab and wishes to open it again.
     * This method therefore re-opens the tab if requested.
     */
    @FXML
    void help() {
        String name = "How To..";
        Tab newT = new Tab(name);
        for(int i =0; i<tabPane.getTabs().size(); i++){
            if(tabPane.getTabs().get(i).getText() == "How To.."){
                tabPane.getTabs().remove(i);
            }
        }
        newT.setContent(howTo.getContent());

        tabPane.getTabs().add(tabPane.getTabs().size() -1, newT);
        tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
    }

    /**
     * Closes the application and terminates the program. This is done when the user requests it,
     * i.e. the button has been selected
     */
    @FXML
    void closeWindow() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Handles the process of creating a now folio and therefore a new tab. If there is an input error the user will be informed.
     */
    @FXML
    void createFolio() {
        String name = View.showFolio();

        if (!name.equals("")) {
            int count = folioHolder.openFoliosWithSameName(name);

            if (count > 0) {
                name += "(" + count + ")";
            }

            Tab newTab = new Tab(name);
            newTab.setContent(View.createTable());
            folioHolder.createfolio(name);

            folioID.setText(folioHolder.getFolio(name).getFolioID());

            tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);
            tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
        } else {
            View.takenNameError();
        }
    }

    /**
     * Used when the user wishes to open a saved folio => creating a tab for the folio and populating the table with
     * the saved holdings within the saved folio.
     */
    @FXML
    void openFolio() {
        String id = View.showFolioID();

        if (!id.equals("")) {
            boolean loaded = folioHolder.loadFolio(id);
            String name = "Default";

            if (loaded) {
                name = folioHolder.getFolioByID(id).getFolioName();
            } else {
                folioHolder.createfolio(name);
                folioID.setText(folioHolder.getFolio(name).getFolioID());
                totalValue1.setText(String.valueOf(folioHolder.getFolio(name).getTotalHolding()));
            }

            Tab newTab = new Tab(name);
            newTab.setContent(View.createTable());
            newTab.setText(name);
            TableView<HoldingInterface> currentTable = (TableView<HoldingInterface>) newTab.getContent();
            View.loadFolio(loaded, currentTable, folioHolder.getFolioByID(id));
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);
            tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);

            try {
                totalValue1.setText(Double.toString(folioHolder.getFolioByID(id).getTotalHolding()));
            } catch (NullPointerException ignored) {

            }
        }
    }

    /**
     * Saving a user requested folio that exists in the system. Informs the user if successful.
     */
    @FXML
    void saveFolio() {
        String name = View.showFolio();
        ArrayList<FolioInterface> saved = folioHolder.getExistingFolios();
        boolean exists = false;

        for (FolioInterface folioInterface : saved) {
            if (folioInterface == folioHolder.getFolio(name)) {
                exists = true;
            }
        }

        if (!name.equals("") && exists) {
            folioHolder.saveFolio(folioHolder.getFolio(tabPane.getSelectionModel().getSelectedItem().getText()));
            View.successfulSave();
        }
    }

    /**
     * Handles the system wide deletion of a desired folio specified by the user.
     */
    @FXML
    void deleteFolio() {
        String id = View.showFolioID();

        if (View.areYouSure(2) && !id.equals("")) {
            File file = new File("SavedFolios/" + id + ".txt");
            if (file.delete()) {
                View.fileDelete();
            } else {
                View.unsuccessfulFolioDelete();
            }

            folioHolder.deleteFolio(id);
            Tab selectedTab = new Tab();

            for (Tab t : tabPane.getTabs()) {
                try {
                    if (t.getText().equals(folioHolder.getFolioByID(id).getFolioName())) {
                        selectedTab = t;
                    }
                } catch (NullPointerException ignored) {

                }
            }

            tabPane.getTabs().remove(selectedTab);
        }
    }
}
