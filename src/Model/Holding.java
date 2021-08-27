package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Holding implements HoldingInterface, Serializable {

    Stock stock;
    int noOfShares;
    double value;


    public Holding(Stock stock, int shares) {
        this.stock = stock;
        this.noOfShares = shares;
        setHoldingValue();
    }

    /**
     * This method changes the current number of shares adding the passed in change value.
     *
     * @param change -> Value that the noOfShares should be changed by, i.e. the selling and buying shares.
     */
    @Override
    public void changeShares(int change) {
        noOfShares += change;
        setHoldingValue();
    }

    /**
     * Simple method to return the current number of shares associated to a stock -> for the GUI
     *
     * @return
     */
    @Override
    public int getNoOfShares() {
        return noOfShares;
    }

    /**
     * Calculates the holding value based of the current price per share and the number of shares bought
     */
    @Override
    public void setHoldingValue() {
        value = stock.getPriceLocally() * noOfShares;
    }

    /**
     * Method to obtain the value of the holding -> for the GUI
     *
     * @return
     */
    @Override
    public double getValue() {
        return value;
    }

    /**
     * Gets and then returns the stock ticker symbol -> for the GUI
     *
     * @return
     */
    @Override
    public String getTickerSymbol() { return stock.getTickerID(); }

    /**
     *  Simple method to obtain the stock name -> for the GUI
     *
     * @return
     */
    @Override
    public String getName() { return stock.getName(); }

    /**
     * Returns the price of a share for a stock -> for the GUI
     *
     * @return
     */
    @Override
    public double getPrice() {
        return stock.getPriceLocally();
    }

    /**
     * Calculates the profit made based off the original price at which the stock was bought at
     *
     * @return
     */
    @Override
    public double getProfit() {
        return noOfShares * stock.currentProfitOnSingleUnit();
    }

    /**
     * Returns the change of price of a stock if there is one.
     *
     * @return
     */
    @Override
    public double getChangeOfPrice() {
        return stock.isPriceUpOrDown();
    }

    /**
     * Refreshes the price of the stock
     */
    @Override
    public void refreshPrices() { stock.setPrice(stock.getPriceLocally()); }

    /**
     * Obtains the price history of a stock
     *
     * @return
     */
    @Override
    public ArrayList<Double> getPriceHistory() { return stock.getPriceHistory(); }

    /**
     * Checks the last time the price was received from the server.
     *
     * @return
     */
    @Override
    public LocalDateTime getLocalDateTime() {
        return stock.timeOfLastPrice();
    }

    /**
     * Method to set the price of a stock
     *
     * @param p -> price to override the current price
     */
    @Override
    public void setPrice(double p) { stock.setPrice(p); }

    /**
     * Obtains the highest price of a stock
     *
     * @return
     */
    @Override
    public double getHighest() { return stock.getHigh(); }

    /**
     * Obtains the highest price of a stock
     *
     * @return
     */
    @Override
    public double getLowest() {
        return stock.getLow();
    }

    /**
     * Uses the same methodology as the Stock isUpOrDown method but returns a String that will then be represented in
     * the table for each folio.
     *
     * @return
     */
    @Override
    public String getUpOrDown() {
        refreshPrices();
        if (stock.getPriceHistory().size() < 2) {
            return "↔ SAME";
        } else if (stock.updateInLastDay()) {
            if (stock.comparePrice(stock.getPriceOneDayAgo().price, stock.getPriceLocally()) > 0) {
                return "⬆ UP";
            } else if (stock.comparePrice(stock.getPriceOneDayAgo().price, stock.getPriceLocally()) < 0){
                return "⬇ DOWN";
            } else {
                return "↔ SAME";
            }
        }
        return "-";
    }

    /**
     * Used for the auto refreshing the Stock Price every 30 seconds
     */
    @Override
    public void refreshUpOrDown() {
        getUpOrDown();
    }
}
