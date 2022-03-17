module hu.nagy_gabor.nagygabor_etlap {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.nagy_gabor.nagygabor_etlap to javafx.fxml;
    exports hu.nagy_gabor.nagygabor_etlap;
}