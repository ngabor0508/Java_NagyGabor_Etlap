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

import java.io.IOException;
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
    @FXML
    private TextField leirasTextField;

    public void initialize(){
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));

        try {
            db = new EtlapDb();
            etlapListaFeltolt();
        } catch (SQLException e) {
            hibaKiir(e);
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
            hibaKiir(e);
        }
    }

    @FXML
    public void onTorlesbuttonClick(ActionEvent actionEvent) {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Etlap torlendoEtel = etlapTable.getSelectionModel().getSelectedItem();
        if(!confirm("Biztos, hogy törölni szeretné az alábbi ételt: " + torlendoEtel.getNev())){
            return;
        }
        try {
            db.etelTorles(torlendoEtel.getId());
            alert("Sikeres a törlés");
            etlapListaFeltolt();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    private void kategoriaListaFeltolt(){
    }
    @FXML
    public void onUjFelvetelButtonClick(ActionEvent actionEvent) {
        try {
            Controller hozzaadas = ujAblak("hozzaad-view.fxml", "Új étel felvétele", 320, 300);
            hozzaadas.getStage().setOnCloseRequest(event -> etlapListaFeltolt());
            hozzaadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }


    @FXML
    public void onButtonClickSzazalekEmeles(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonClickFtEmeles(ActionEvent actionEvent) {
    }

    @FXML
    public void onKategoriabuttonClick(ActionEvent actionEvent) {
        try {
            Controller kategoriak = ujAblak("kategoria-view.fxml", "Kategóriák", 400, 400);
            //kategoriak.getStage().setOnCloseRequest(event -> );
            kategoriak.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }

    }
}