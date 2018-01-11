package gentree.client.desktop.controllers.screen;

import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLBorderPane;
import gentree.client.desktop.controllers.FXMLController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 10/07/2017.
 * <p>
 * This is a Main BorderPane of the Application</br>
 * This Pane will contain<br>
 * <li>Top: MenuBar</li>
 * <li>Bottom: Footer</li>
 * <li>Center:  content of application</li>
 * </br>
 * Top and Bottom Panes should not be changed during Application work
 */
@Log4j2
public class RootWindowController implements Initializable, FXMLController, FXMLBorderPane {

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        this.languageBundle.setValue(resources);
        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }

    @Override
    public void clean() {

    }
}