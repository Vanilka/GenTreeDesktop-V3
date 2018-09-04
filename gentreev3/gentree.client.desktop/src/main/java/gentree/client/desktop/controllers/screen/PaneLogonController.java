package gentree.client.desktop.controllers.screen;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gentree.client.desktop.configuration.CellFactoryProvider;
import gentree.client.desktop.configuration.GenTreeProperties;
import gentree.client.desktop.configuration.Realm;
import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import gentree.client.desktop.service.ScreenManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 22/10/2017.
 */
@Log4j2
public class PaneLogonController implements Initializable, FXMLController, FXMLAnchorPane {


    @FXML
    private JFXPasswordField PASSWORD_FIELD;

    @FXML
    private JFXTextField LOGIN_FIELD;

    @FXML
    private ComboBox<Realm> REALM_BOX;

    @FXML
    private JFXButton CONNECT_BUTTON;

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @Override
    public void clean() {

    }

    @FXML
    private void login() {
       boolean result = false;
        if (result) {
            System.out.println("wull load Online FXML");
            sm.loadFxml( sm.getMainWindowBorderPane(), FilesFXML.SCREEN_OPEN_ONLINE_PROJECT_FXML, ScreenManager.Where.CENTER);
        }

    }

    @FXML
    private void openRegisterForm() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        this.languageBundle.setValue(resources);

        REALM_BOX.setCellFactory(CellFactoryProvider.CUSTOM_REALM_LIST_CELL);
        REALM_BOX.setButtonCell(CellFactoryProvider.CUSTOM_REALM_LIST_CELL.call(null));

        REALM_BOX.setItems(GenTreeProperties.INSTANCE.getRealmConfig().getRealms());


        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }
}
