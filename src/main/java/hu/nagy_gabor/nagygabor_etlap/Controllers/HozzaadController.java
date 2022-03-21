package hu.nagy_gabor.nagygabor_etlap.Controllers;

import hu.nagy_gabor.nagygabor_etlap.Controller;
import hu.nagy_gabor.nagygabor_etlap.EtlapDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class HozzaadController extends Controller {
    @FXML
    private TextField inputEtelNev;
    @FXML
    private TextArea inputEtelLeiras;
    @FXML
    private ChoiceBox inputEtelKategoria;
    @FXML
    private Spinner<Integer> inputEtelAr;

    @FXML
    public void onHozzaadButtonClick(ActionEvent actionEvent) {
        String nev = inputEtelNev.getText().trim();
        String leiras = inputEtelLeiras.getText().trim();
        String kategoriaIndex = String.valueOf(inputEtelKategoria.getSelectionModel().getSelectedIndex());
        int ar = 0;
        if(nev.isEmpty()){
            alert("Cím megadása kötelező!");
            return;
        }
        if (leiras.isEmpty()){
            alert("Leírás megadása kötelező!");
            return;
        }

        if(kategoriaIndex == null){
            alert("Kategória kiválasztása kötelező!");
            return;
        }
        String kategoria = String.valueOf(inputEtelKategoria.getValue());

        try {
            ar = inputEtelAr.getValue();
        } catch (NullPointerException ex){
            alert("Az ár megadása kötelező!");
            return;
        } catch (Exception ex){
            alert("Az ár csak 500 és 100.000 közötti szám lehet!");
            return;
        }
        if(ar < 500 || ar > 100000){
            alert("Az ár csak 500 és 100.000 közötti szám lehet!");
            return;
        }

        try {
            EtlapDb db = new EtlapDb();
            int siker = db.etelHozzaadas(nev,leiras, ar, kategoria);
            if(siker == 1){
                alert("Étel hozzáadása sikeres!");
            }
            else{
                alert("Étel hozzáadása sikertelen!");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }


    }
}
