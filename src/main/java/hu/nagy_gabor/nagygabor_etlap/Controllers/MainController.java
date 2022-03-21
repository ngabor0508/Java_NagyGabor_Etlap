package hu.nagy_gabor.nagygabor_etlap.Controllers;

import hu.nagy_gabor.nagygabor_etlap.Controller;
import hu.nagy_gabor.nagygabor_etlap.Etlap;
import hu.nagy_gabor.nagygabor_etlap.EtlapDb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import java.sql.SQLException;
import java.util.List;

public class MainController extends Controller {
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn<Etlap, String> colNev;
    @FXML
    private TableColumn<Etlap, Integer> colAr;
    @FXML
    private TableColumn<Etlap, String> colKategoria;
    private EtlapDb db;
    @FXML
    private Spinner inputSzazalek;
    @FXML
    private Spinner inputFt;

    public void initialize(){
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));

        try {
            db = new EtlapDb();
            etlapListaFeltolt();
        } catch (SQLException e) {
            System.out.println("HIBA!");
        }
    }

    public TableColumn<Etlap, String> getColNev() {
        return colNev;
    }

    private void etlapListaFeltolt() {
        try {
            List<Etlap> etlapList = db.getEtlap();
            etlapTable.getItems().clear();
            for(Etlap etlap: etlapList){
                etlapTable.getItems().add(etlap);
            }
        } catch (SQLException e) {
            System.out.println("Ez egy másik hiba.");
        }
    }

    @FXML
    public void onTorlesbuttonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onUjFelvetelButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonClickSzazalekEmeles(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonClickFtEmeles(ActionEvent actionEvent) {
    }
}