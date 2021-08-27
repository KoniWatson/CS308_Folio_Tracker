package Controller;

import Model.*;
import View.View;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class EditStockController {

    @FXML private AnchorPane editStock;
    @FXML private TextField noOfSharesBuy;
    @FXML private TextField noOfSharesSell;
    @FXML private Label folioName, tickerSymbol, stockName, holdingValue, comparePrice;
    @FXML private Label timeOfLastPrice, high, low, profit;

    private TableView<HoldingInterface> table = View.getCurrentTable();
    private FolioInterface folio = View.getCurrentFolio();
    private HoldingInterface stock;

    private String ts;

    /**
     * Initializer to populate the edit stock scene with the valid information of the selected stock.
     */
    @FXML
    private void initialize() {

        stock = table.getSelectionModel().getSelectedItem();
        LocalDateTime lp = stock.getLocalDateTime();
        ts = stock.getTickerSymbol();
        String n = stock.getName();
        //int s = stock.getNoOfShares();
        double hv = stock.getValue();
        double p = stock.getPrice();
        double h = stock.getHighest();
        double l = stock.getLowest();
        double pro = stock.getProfit();
        double cp = stock.getChangeOfPrice();

        stock.setPrice(p);

        try {
            folioName.setText(folio.getFolioName());
        }catch (NullPointerException ignored){

        }

        tickerSymbol.setText(ts);
        stockName.setText(n);
        holdingValue.setText(String.valueOf(hv));

        comparePrice.setText(String.valueOf(cp));
        noOfSharesBuy.setText("0");
        noOfSharesSell.setText("0");
        timeOfLastPrice.setText(String.valueOf(lp));
        high.setText(String.valueOf(h));
        low.setText(String.valueOf(l));
        profit.setText(String.valueOf(pro));
    }

    /**
     * This is a check upon the number entered by the user, i.e. the selling or buying of shares.
     *
     * @param s -> The number of shares that are to be bought or sold
     * @return
     */
    private boolean checkIfNumber(String s){
        boolean notNumber = false;
        for(int i = 0; i<s.length(); i++) {
            char character = s.trim().charAt(i);
            if (!(Character.isDigit(character))) {
                notNumber = true;
            } else {
                notNumber = false;
            }

        }
        return notNumber;
        
    }

    /**
     * Handles when the user wishes to save the edited stocks information. Making sure the information entered is valid,
     * then making the appropriate information.
     */
    @FXML
    void save(){
        boolean buySharesNotNumber = checkIfNumber(noOfSharesBuy.getText());
        boolean sellSharesNotNumber = checkIfNumber(noOfSharesSell.getText());
        boolean notNumber = false, set = true;
        String shares = "";

        if ((buySharesNotNumber) || (sellSharesNotNumber)) {
            notNumber = true;
        }
        if (notNumber){
            View.addStockSharesFormatError();
            try{
                notNumber = true;
            }catch (NumberFormatException ignored){

            }
        } else {
            shares = (noOfSharesBuy.getText());

        }
        if (shares == ""){
            set = false;
        }
        if(set) {
            int buy = Integer.parseInt(noOfSharesBuy.getText());
            int sell = Integer.parseInt(noOfSharesSell.getText());

            if (buy != 0) {
                stock.changeShares(buy);
            }

            if (sell != 0 && (stock.getNoOfShares() - sell) > 0) {
                stock.changeShares(-sell);
            } else if ((stock.getNoOfShares() - sell) <= 0 && View.areYouSure(4)) {
                folio.deleteStock(stock.getTickerSymbol());
                table.getItems().remove(stock);
            }

            View.editedStocks(stock, table, ts);
            Stage stage = (Stage) editStock.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * The close of the edit stocks scene. This is when the user wishes to stop editing a selected stock.
     */
    @FXML
    void cancel(){
        Stage stage = (Stage) editStock.getScene().getWindow();
        stage.close();
    }


}
