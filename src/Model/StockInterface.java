package Model;//package folio_tracker;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StockInterface {

    double getPrice();
    void setPrice(double p);
    String getTickerID();
    String getName();
    double getPriceLocally();
    LocalDateTime timeOfLastPrice();
    ArrayList<Double> getPriceHistory();
    double getLow();
    double getHigh();
    double currentProfitOnSingleUnit();
    double isPriceUpOrDown();

}
