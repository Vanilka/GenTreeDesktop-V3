package gentree.client.desktop.controllers.screen;

import com.jfoenix.controls.JFXToggleButton;
import gentree.client.desktop.configuration.GenTreeProperties;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import lombok.extern.log4j.Log4j2;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 30/07/2017.
 */

@Log4j2
public class DialogAppPropertiesOtherController implements Initializable, FXMLController, FXMLAnchorPane {


    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @FXML
    private AnchorPane PANE_ADMIN_MODE;

    @FXML
    private JFXToggleButton TOGGLE_ADMIN_MODE;

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

    @Override
    public void clean() {

    }
}