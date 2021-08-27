package Model;

import java.io.*;
import java.util.ArrayList;

public class FolioHolder implements FolioHolderInterface {

    public ArrayList<FolioInterface> folios = new ArrayList<>();

    /**
     * Method to get a folio
     *
     * @param name is the name of the folio to be fetched
     * @return is the folio with the matching name
     */
    @Override
    public FolioInterface getFolio(String name) {
        for (FolioInterface f : folios) {
            if (f.getFolioName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Method to get a folio
     *
     * @param id is the name of the folio to be fetched
     * @return is the folio with the matching name
     */
    @Override
    public FolioInterface getFolioByID(String id) {
        for (FolioInterface f : folios) {
            if (f.getFolioID().equals(id)) {
                return f;
            }
        }
        return null;
    }


    /**
     * Method to create a folio
     *
     * @param name is the name of the folio to be created
     */
    @Override
    public void createfolio(String name) {
            folios.add(new Folio(name));
            System.out.println("Model.Folio '" + getFolio(name).getFolioName() + "' added!");
    }


    /**
     * Method to delete a folio
     *
     * @param id is the ID of the folio to be deleted
     * @return
     */
    @Override
    public boolean deleteFolio(String id) {
        id = id.toUpperCase();

        for(FolioInterface f: getExistingFolios()){

            if(f.getFolioID()==id){
                folios.remove(getFolio(f.getFolioName()));
                System.out.println("Folio removed!");
                return true;
            }
        }
        System.out.println("Folio not found");
        return false;
    }

    /**
    * checks to see if any folio is open with the same name as the current one
    * will set the folio name to include a digit to distinguish between folios\
    *
    * @param name the name of the folio
    * */
    @Override
    public int openFoliosWithSameName(String name){
        int i = 0;

        for(FolioInterface f: getExistingFolios()){
            String trimmedName="";

            if(f.getFolioName().charAt(f.getFolioName().length()-1) == ')' && f.getFolioName().charAt(f.getFolioName().length()-3) == '('
            && Character.isDigit(f.getFolioName().charAt(f.getFolioName().length()-2))){

                for(int j =0;j<f.getFolioName().length()-3;j++){
                    trimmedName += f.getFolioName().charAt(j);
                }

            }else{
                trimmedName = f.getFolioName();
            }

            if(name.equals(trimmedName)){
                i++;
            }
        }
        return i;

    }



    /**
     * This method will be used to save a user portfolio to the created file
     *
     * @param folioID The portfolio to be saved to the file
     * @return
     */
    @Override
    public boolean loadFolio(String folioID) {
        System.out.println("The loadFolio() in FolioHolder has been accessed.");
        try {

            FileInputStream fileIn = new FileInputStream("SavedFolios/" + folioID + ".txt");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Folio folio = (Folio) objectIn.readObject();
            System.out.println(folio.getFolioID()+" has been read from the file");
            objectIn.close();

            folios.add(folio);
            folios.get(folios.indexOf(folio)).refreshPrices();
            return true;
        } catch (Exception e) {

            return false;

        }
    }


    /**
     * This method will be used to save a user portfolio to the created file
     *
     * @param folio The portfolio to be saved to the file
     */
    @Override
    public void saveFolio(FolioInterface folio) {

        try {

            FileOutputStream fileOut = new FileOutputStream("SavedFolios/" + folio.getFolioID() + ".txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(folio);
            objectOut.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public ArrayList<FolioInterface> getExistingFolios() {
        return folios;
    }

}
