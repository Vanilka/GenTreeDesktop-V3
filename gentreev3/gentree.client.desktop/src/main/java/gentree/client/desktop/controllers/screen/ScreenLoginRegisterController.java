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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 22/10/2017.
 */
@Log4j2
public class ScreenLoginRegisterController implements Initializable, FXMLController, FXMLAnchorPane {

    @FXML
    private AnchorPane SCREEN_LOGON_REGISTER;

    @FXML
    private Pane LOGON_FORM;

    @FXML
    private Pane REGISTER_FORM;

    private PaneLogonController logonController;

    private ChangeListener<? super Number> mainAnchorPaneHeightListener = this::heightChange;


    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);

        this.languageBundle.setValue(resources);
        initLogonForm();
        addTopOffsetListener();
        this.LOGON_FORM.resize(300, 400);
        this.REGISTER_FORM.resize(400, 500);

        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }


    @Override
    public void clean() {

    }

    private void initLogonForm() {
        logonController = (PaneLogonController) sm.loadFxml( LOGON_FORM, FilesFXML.PANE_LOGON_FXML);
    }

    public void addTopOffsetListener() {
        this.SCREEN_LOGON_REGISTER.heightProperty().addListener(mainAnchorPaneHeightListener); }


    public void cleanListeners() {
        cleanBinding(this.LOGON_FORM);
        cleanBinding(this.REGISTER_FORM);
    }

    public void cleanBinding(Pane pane) {
       SCREEN_LOGON_REGISTER.heightProperty().removeListener(mainAnchorPaneHeightListener);
       mainAnchorPaneHeightListener = null;
    }

    private void heightChange(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double y = (newValue.doubleValue() - LOGON_FORM.getHeight()) / 2;
        System.out.println();
        LOGON_FORM.setLayoutY(y);
    }
}
