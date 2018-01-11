package gentree.client.desktop.controllers.screen;

import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 01/07/2017.
 * Show window with choice of mode of application :  <br>
 * <p>
 * a) Online <br>
 * b) OffLine <br>
 */
@Log4j2
public class ScreenWelcomeController implements Initializable, FXMLController, FXMLAnchorPane {

    @FXML
    private Pane LOCAL_PROJECT_PANE;

    @FXML
    private Pane ONLINE_PROJECT_PANE;

    @FXML
    private AnchorPane MAIN_ANCHOR_PANE;

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();
    private ChangeListener<? super Number> mainAnchorPaneHeightListener = this::heightChange;
    private ChangeListener<? super ResourceBundle> languageListener = this::languageChange;
    private ChangeListener<? super Parent> parentListener = this::ParentChange;


    private void ParentChange(ObservableValue<? extends Parent> observableValue, Parent oldValue, Parent newValue) {
        if (newValue == null) {
            MAIN_ANCHOR_PANE.parentProperty().removeListener(parentListener);
            parentListener  = null;
            clean();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        initListener();
        this.languageBundle.setValue(resources);
        initOnlineProjectPane();
        initLocalProjectPane();
        setListeners();
        addTopOffsetListener();
        LOCAL_PROJECT_PANE.resize(300, 400);
        ONLINE_PROJECT_PANE.resize(300, 400);
        MAIN_ANCHOR_PANE.parentProperty().addListener(parentListener);
        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }

    private void initListener() {
        MAIN_ANCHOR_PANE.parentProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Parent changes for screen Welcome");
            System.out.println("Paren is : " + newValue);
        });
    }


    public void initLocalProjectPane() {
        sm.loadFxml(LOCAL_PROJECT_PANE, FilesFXML.LOCAL_APP_MODE);
    }

    public void initOnlineProjectPane() {
        sm.loadFxml(ONLINE_PROJECT_PANE, FilesFXML.ONLINE_APP_MODE);
    }

    public void addTopOffsetListener() {
        this.MAIN_ANCHOR_PANE.heightProperty().addListener(mainAnchorPaneHeightListener);
    }

    private void setListeners() {
        this.languageBundle.addListener(languageListener);
    }

    private void heightChange(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double y = (newValue.doubleValue() - LOCAL_PROJECT_PANE.getHeight()) / 2;
        LOCAL_PROJECT_PANE.setLayoutY(y);
        ONLINE_PROJECT_PANE.setLayoutY(y);
    }


    private void languageChange(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadElements();
    }

    /*
     * LISTEN LANGUAGE CHANGES
     */

    private String getValueFromKey(String key) {
        return this.languageBundle.getValue().getString(key);
    }

    private void reloadElements() {

    }

    @Override
    public void clean() {
        cleanListeners();
    }

    private void cleanListeners() {
        this.MAIN_ANCHOR_PANE.heightProperty().removeListener(mainAnchorPaneHeightListener);
        this.languageBundle.removeListener(languageListener);
    }


    /*
     ON CLICK ACTIONS
     */


}
