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
import javafx.scene.input.MouseEvent;

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
        int etelIndex = etlapTable.getSelectionModel().getSelectedIndex();
        int szazalek = 0;
        try {
            szazalek = (int) inputSzazalek.getValue();
        } catch (Exception e){
            alert("Kérjük válaszd ki, hogy hány százalékkal szeretnéd növelni az árat!");
            return;
        }
        if(szazalek < 5 || szazalek > 50){
            alert("5% és 50% között tudsz válsztani!");
            return;
        }
        if (!confirm("Biztos hogy emelni szeretné az árat " + szazalek +"%-al?")){
            return;
        }
        if(etelIndex == -1){
            try {
                db.szazalekEmelesMindenre(szazalek);
                alert("Az ételek árát sikeresen megnövelted "+ szazalek + "%-al.");
                etlapListaFeltolt();
            } catch (Exception e) {
                hibaKiir(e);
            }
        } else {
            Etlap kijeloltEtel = etlapTable.getSelectionModel().getSelectedItem();
            try {
                db.szazalekEmelesEgyEtelre(szazalek, kijeloltEtel.getId());
                alert("A " + kijeloltEtel.getNev() + " árát sikeresen megnövelted " +szazalek + "%-al.");
                etlapListaFeltolt();
            } catch (Exception e) {
                hibaKiir(e);
            }
        }
    }

    @FXML
    public void onButtonClickFtEmeles(ActionEvent actionEvent) {
        int etelIndex = etlapTable.getSelectionModel().getSelectedIndex();
        int ftEmeles = 0;
        try {
            ftEmeles = (int) inputFt.getValue();
        } catch (Exception e){
            alert("Kérjük adj meg egy érvényes összeget amennyivel emelni szeretnéd az étel(ek) árát.");
            return;
        }
        if (ftEmeles < 50 || ftEmeles > 3000){
            alert("Az ár emeléséhez 50Ft és 3000 Ft forint között tudsz választani.");
            return;
        }
        if(!confirm("Biztos, hogy növelni szeretnéd az árat " + ftEmeles + "Ft-al?")){
            return;
        }
        if(etelIndex == -1){
            try {
                db.ftEmelesMindenre(ftEmeles);
                alert("Az ételek árát sikeresen megnövelted "+ ftEmeles + "Ft-al.");
                etlapListaFeltolt();
            } catch (Exception e){
                hibaKiir(e);
            }
        } else {
            Etlap kijeloltEtel = etlapTable.getSelectionModel().getSelectedItem();
            try {
                db.ftEmelesEgyEtelre(ftEmeles, kijeloltEtel.getId());
                alert("Az étel árát sikeresen megnövelted "+ ftEmeles + "Ft-al.");
                etlapListaFeltolt();
            } catch (Exception e){
                hibaKiir(e);
            }
        }
    }

    @FXML
    public void onKategoriakbuttonClick(ActionEvent actionEvent) {
        try {
            Controller hozzaadas = ujAblak("kategoria-view.fxml", "Kategóriák", 400, 400);
            hozzaadas.getStage().setOnCloseRequest(event -> kategoriaListaFeltolt());
            hozzaadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    private void kategoriaListaFeltolt() {
    }

    @FXML
    public void onClicked(MouseEvent event) {
        Etlap kijeloltEtel = etlapTable.getSelectionModel().getSelectedItem();
        leirasTextField.setText(kijeloltEtel.getLeiras());
    }
}