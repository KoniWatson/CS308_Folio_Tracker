package Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface FolioHolderInterface {


	FolioInterface getFolio(String name);
    FolioInterface getFolioByID(String id);
    void createfolio(String name);
	boolean deleteFolio(String name);
    int openFoliosWithSameName(String name);
    boolean loadFolio(String folioID) throws FileNotFoundException;
    void saveFolio(FolioInterface folio);

    ArrayList<FolioInterface> getExistingFolios();
}
