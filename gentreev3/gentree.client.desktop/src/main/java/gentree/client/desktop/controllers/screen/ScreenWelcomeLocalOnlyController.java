package gentree.client.desktop.controllers.screen;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.Keys;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import gentree.client.desktop.domain.Family;
import gentree.client.desktop.service.ScreenManager;
import gentree.client.desktop.service.implementation.GenTreeLocalService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import lombok.extern.log4j.Log4j2;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * Created by: mszymkowiak
 * Created: 29/06/2018
 **/
@Log4j2
public class ScreenWelcomeLocalOnlyController implements Initializable, FXMLController, FXMLAnchorPane {


    @FXML
    private JFXTabPane TAB_PANE_OPEN_PROJECT;

    @FXML
    private AnchorPane CONTENT;

    @FXML
    private JFXButton BUTTON_CONFIRM;

    @FXML
    private AnchorPane MAIN_ANCHOR_PANE;

    @FXML
    private Tab tabOpenNew;

    @FXML
    private TabOpenNewProjectController tabONPController;

    @FXML
    private TabOpenExistingProjectController tabENPController;

    @FXML
    private Tab tabOpenExisting;

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    private ChangeListener<? super Parent> parentListener = this::ParentChange;

    private ChangeListener<? super Tab> selectedTabListener = this::selectedTabChanged;

    private ChangeListener<? super ResourceBundle> languageListener = this::languageChange;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        this.languageBundle.setValue(resources);
        context.setService(new GenTreeLocalService());

        addSelectedTabListener();
        addDisableButtonListener();
        addLanguageListener();

        log.trace(LogMessages.MSG_CTRL_INITIALIZED);
    }

    private void ParentChange(ObservableValue<? extends Parent> observableValue, Parent oldValue, Parent newValue) {
        if (newValue == null) {
            MAIN_ANCHOR_PANE.parentProperty().removeListener(parentListener);
            parentListener  = null;
            clean();
        }
    }

    private void selectedTabChanged(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
        if (newValue.equals(tabOpenNew)) {
            BUTTON_CONFIRM.setText(getValueFromKey(Keys.CREATE));
        } else if (newValue.equals(tabOpenExisting)) {
            BUTTON_CONFIRM.setText(getValueFromKey(Keys.OPEN));
        }
    }


    private void actionNewProject() {

        context.getService().createFamily(new Family(tabONPController.getFAMILY_NAME_FIELD().getText().trim()));

        if (context.getService() instanceof GenTreeLocalService) {
            sm.loadFxml( sm.getMainWindowBorderPane(), FilesFXML.SCREEN_MAIN_FXML, ScreenManager.Where.CENTER);
        }
    }

    private void actionOpenProject() {
        Path path = tabENPController.getSelectedFile();
        Family family = readFamilyFromXML(path.toFile());

        ((GenTreeLocalService) context.getService()).openProject(family, path.toFile().getName());
        sm.loadFxml( sm.getMainWindowBorderPane(), FilesFXML.SCREEN_MAIN_FXML, ScreenManager.Where.CENTER);
    }


    private Family readFamilyFromXML(File file) {
        Family customer = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Family.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            customer = (Family) jaxbUnmarshaller.unmarshal(file);
        } catch (Exception e) {
            log.error("ERROR");
            e.printStackTrace();
        }

        return customer;
    }




    //Listeners

    private void addSelectedTabListener() {
        TAB_PANE_OPEN_PROJECT.getSelectionModel().selectedItemProperty().addListener(selectedTabListener);
    }

    private void addDisableButtonListener() {
        BooleanBinding disableBinding;

        if (context.getService() instanceof GenTreeLocalService) {
            disableBinding = Bindings.createBooleanBinding(
                    () -> ((TAB_PANE_OPEN_PROJECT.getSelectionModel().getSelectedItem().equals(tabOpenNew)
                            && tabONPController.getFAMILY_NAME_FIELD().getText().isEmpty())
                            ||
                            (TAB_PANE_OPEN_PROJECT.getSelectionModel().getSelectedItem().equals(tabOpenExisting)
                                    && tabENPController.getPROJECT_CHOICE_COMBOBOX().getSelectionModel().getSelectedItem() == null)),
                    tabONPController.getFAMILY_NAME_FIELD().textProperty(),
                    tabENPController.getPROJECT_CHOICE_COMBOBOX().getSelectionModel().selectedIndexProperty(),
                    TAB_PANE_OPEN_PROJECT.getSelectionModel().selectedItemProperty());
        } else {
            disableBinding = Bindings.createBooleanBinding(
                    () -> (tabONPController.getFAMILY_NAME_FIELD().getText().isEmpty()),
                    tabONPController.getFAMILY_NAME_FIELD().textProperty());
        }
        this.BUTTON_CONFIRM.disableProperty().bind(disableBinding);
    }

    private void addLanguageListener() {
        this.languageBundle.addListener(languageListener);
    }

    private void languageChange(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadElements();
    }
    private void reloadElements() {
        // Nothing to do
    }


    private String getValueFromKey(String key) {
        return this.languageBundle.getValue().getString(key);
    }

    @Override
    public void clean() {

    }


    public void confirm(ActionEvent actionEvent) {

        if (TAB_PANE_OPEN_PROJECT.getSelectionModel().getSelectedItem().equals(tabOpenNew)) {
            actionNewProject();

        } else if (TAB_PANE_OPEN_PROJECT.getSelectionModel().getSelectedItem().equals(tabOpenExisting)) {
            actionOpenProject();
        } else {

            //To to show error
        }
    }


}
