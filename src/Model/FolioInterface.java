package Model;//package folio_tracker;

import java.util.ArrayList;

public interface FolioInterface {


    void refreshPrices();
    double getTotalHolding();
    boolean addHolding(String name, String tickerSymbol, int shares);
    void deleteStock(String tickerSymbol);
    HoldingInterface getStock(String tickerSymbol);
    String getFolioName();
    String setFolioID();
    String getFolioID();

    ArrayList<HoldingInterface> getStocks();
}
