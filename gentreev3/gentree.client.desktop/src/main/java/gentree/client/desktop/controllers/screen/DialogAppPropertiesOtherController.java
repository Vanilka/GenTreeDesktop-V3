package gentree.client.desktop.controllers.screen;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import gentree.client.desktop.configuration.GenTreeProperties;
import gentree.client.desktop.configuration.enums.PropertiesKeys;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 30/07/2017.
 */

@Log4j2
public class DialogAppPropertiesOtherController implements Initializable, FXMLController, FXMLAnchorPane {

    @Getter
    private HashMap<String, String> properties;

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @FXML
    private AnchorPane PANE_ADMIN_MODE;

    @FXML
    private JFXToggleButton TOGGLE_ADMIN_MODE;

    @FXML
    private TextField FILE_PATH_FIELD;

    @FXML
    private JFXButton BUTTON_SELECT_LOG;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        this.languageBundle.setValue(resources);
        init();
        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }

    public void cleanListeners() {
    }

    private void  init() {
        initListeners();
        TOGGLE_ADMIN_MODE.setSelected(GenTreeProperties.INSTANCE.isAdminModeON());
    }


    private void  initListeners() {
        TOGGLE_ADMIN_MODE.textProperty().bind(Bindings.when(TOGGLE_ADMIN_MODE.selectedProperty()).then("ON").otherwise("OFF"));
    }

    public boolean getAdminModeOn() {
        return TOGGLE_ADMIN_MODE.isSelected();
    }

    public String getfilePathString() {
        return  FILE_PATH_FIELD.getText();
    }

    public void openFileName(ActionEvent event) {
        File file = sm.openLogFileChooser();
        if(file != null) FILE_PATH_FIELD.setText(file.getAbsolutePath());


    }

    @Override
    public void clean() {

    }

    public void setProperties(HashMap<String,String> properties) {
        this.properties = properties;
        populateFromProperties();
        FILE_PATH_FIELD.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && (!Files.exists(Paths.get(newValue)) || !Files.isRegularFile(Paths.get(newValue)))) {
                FILE_PATH_FIELD.setStyle("-fx-background-color: red;");
            } else {
                FILE_PATH_FIELD.setStyle("-fx-background-color: white;");
                properties.replace(PropertiesKeys.PARAM_PATH_MC_LOG, newValue);
            }
        });
    }

    private void populateFromProperties() {
     FILE_PATH_FIELD.setText(properties.get(PropertiesKeys.PARAM_PATH_MC_LOG));
    }
}