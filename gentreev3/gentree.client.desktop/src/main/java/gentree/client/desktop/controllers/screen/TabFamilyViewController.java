package gentree.client.desktop.controllers.screen;

import com.jfoenix.controls.JFXTabPane;
import gentree.client.desktop.configuration.CellFactoryProvider;
import gentree.client.desktop.configuration.messages.Keys;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.controllers.FXMLAnchorPane;
import gentree.client.desktop.controllers.FXMLController;
import gentree.client.desktop.controllers.FXMLTab;
import gentree.client.desktop.domain.Family;
import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.configuration.ImageFiles;
import gentree.common.configuration.enums.RelationType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 03/07/2017.
 */
@Getter
@Setter
@Log4j2
public class TabFamilyViewController implements Initializable, FXMLController, FXMLAnchorPane, FXMLTab {

    public static final String TABLE_RELATION_SIM_RIGHT = "RIGHT";
    public static final String TABLE_RELATION_SIM_LEFT = "LEFT";
    private final ToggleGroup buttonsTableGroup;

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @FXML
    private JFXTabPane MAIN_TAB_PANE;
    @FXML
    private TableView<Member> FAMILY_MEMBER_TABLE;
    @FXML
    private TableColumn<Member, String> SIM_NAME_COLUMN;
    @FXML
    private TableColumn<Member, String> SIM_SURNAME_COLUMN;
    @FXML
    private TableColumn<Member, String> SIM_PHOTO_COLUMN;
    @FXML
    private TableView<Relation> FAMILY_RELATION_TABLE;
    @FXML
    private TableColumn<Relation, Member> RELATION_SIM_LEFT_COLUMN;
    @FXML
    private TableColumn<Relation, RelationType> RELATION_TYPE_COLUMN;
    @FXML
    private TableColumn<Relation, Member> RELATION_SIM_RIGHT_COLUMN;
    @FXML
    private ToggleButton BUTTON_SHOW_MEMBERS_TABLE;
    @FXML
    private ToggleButton BUTTON_SHOW_RELATIONS_TABLE;

    private ScreenMainLeftController screenMainLeft;

    private Tab tab;
    private JFXTabPane tabPane;
    private ChangeListener<? super ResourceBundle> languageListener = this::languageChanged;
    private ChangeListener<? super Family> familyListener = this::familyChange;
    private ChangeListener<? super Toggle> selectedButtonListener = this::selectedButtonChange;

    {
        buttonsTableGroup = new ToggleGroup();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.trace(LogMessages.MSG_CTRL_INITIALIZATION);
        this.languageBundle.setValue(resources);
        this.languageBundle.bind(context.getBundle());
        addLanguageListener();

        FAMILY_MEMBER_TABLE.setVisible(true);
        FAMILY_RELATION_TABLE.setVisible(false);

        setButtonToToggleGroup();

        setCellValueFactory();
        setCellFactory();

        context.getService().currentFamilyPropertyI().addListener(this::familyChange);
        populateWithFamily(context.getService().getCurrentFamily());

        log.trace(LogMessages.MSG_CTRL_INITIALIZED);

    }

    private void setCellFactory() {
        this.RELATION_SIM_LEFT_COLUMN.setCellFactory(CellFactoryProvider.MEMBER_LEFT_CELL_FACTORY);
        this.RELATION_SIM_RIGHT_COLUMN.setCellFactory(CellFactoryProvider.MEMBER_RIGHT_CELL_FACTORY);
        this.RELATION_TYPE_COLUMN.setCellFactory(CellFactoryProvider.RELATION_TYPE_CELL_FACTORY);
        this.SIM_PHOTO_COLUMN.setCellFactory(CellFactoryProvider.SIM_PHOTO_CELL_FACTORY);
    }

    private void setButtonToToggleGroup() {
        BUTTON_SHOW_MEMBERS_TABLE.setToggleGroup(buttonsTableGroup);
        BUTTON_SHOW_RELATIONS_TABLE.setToggleGroup(buttonsTableGroup);

        buttonsTableGroup.selectToggle(BUTTON_SHOW_MEMBERS_TABLE);

        buttonsTableGroup.selectedToggleProperty().addListener(selectedButtonListener);
    }

    /*
        LISTENERS
     */
    private void cleanListeners() {
        buttonsTableGroup.selectedToggleProperty().removeListener(selectedButtonListener);
        context.getService().currentFamilyPropertyI().removeListener(familyListener);
        this.languageBundle.removeListener(languageListener);
    }


    private void languageChanged(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadElements();
    }

    private void familyChange(ObservableValue<? extends Family> observable, Family oldValue, Family newValue) {
        if (newValue != null) {
            populateWithFamily(newValue);
        }
    }

    private void selectedButtonChange(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            buttonsTableGroup.selectToggle(oldValue);
        }
        if (buttonsTableGroup.getSelectedToggle().equals(BUTTON_SHOW_MEMBERS_TABLE)) {
            FAMILY_MEMBER_TABLE.setVisible(true);
            FAMILY_RELATION_TABLE.setVisible(false);
        } else {
            FAMILY_MEMBER_TABLE.setVisible(false);
            FAMILY_RELATION_TABLE.setVisible(true);
        }
    }

    /*
     * LISTEN LANGUAGE CHANGES
     */
    private void addLanguageListener() {
        this.languageBundle.addListener(languageListener);
    }

    private String getValueFromKey(String key) {
        return this.languageBundle.getValue().getString(key);
    }

    private void reloadElements() {
        // Nothing to do
        BUTTON_SHOW_MEMBERS_TABLE.setText(getValueFromKey(Keys.BUTTON_MEMBERS));
        BUTTON_SHOW_RELATIONS_TABLE.setText(getValueFromKey(Keys.BUTTON_MEMBERS));
    }

    @FXML
    private void showSimContextMenu(ContextMenuEvent event) {
        Member m = FAMILY_MEMBER_TABLE.getSelectionModel().getSelectedItem();
        if (m != null) sm.showSimContextMenu(m, FAMILY_MEMBER_TABLE, event);
    }

    @FXML
    private void showRelationContextMenu(ContextMenuEvent event) {

    }


    @FXML
    public void showInfoMember(MouseEvent event) {
        Member selected = FAMILY_MEMBER_TABLE.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && selected != null) {
            sm.getScreenMainController().showInfoSim(selected);
        }
    }

    @FXML
    public void showInfoRelation(MouseEvent event) {
        Relation selected = FAMILY_RELATION_TABLE.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && selected != null) {
            sm.getScreenMainController().showInfoRelation(selected);
        }
    }

    private void setCellValueFactory() {
        this.SIM_NAME_COLUMN.setCellValueFactory(data -> data.getValue().nameProperty());
        this.SIM_SURNAME_COLUMN.setCellValueFactory(data -> data.getValue().surnameProperty());
        this.SIM_PHOTO_COLUMN.setCellValueFactory(CellFactoryProvider.PHOTO_VALUE_FACTORY);

        this.RELATION_SIM_LEFT_COLUMN.setCellValueFactory(data -> data.getValue().leftProperty());
        this.RELATION_SIM_RIGHT_COLUMN.setCellValueFactory(data -> data.getValue().rightProperty());
        this.RELATION_TYPE_COLUMN.setCellValueFactory(data -> data.getValue().typeProperty());

    }


    private void setMemberList(ObservableList<Member> list) {
        this.FAMILY_MEMBER_TABLE.setItems(list);
    }

    private void setRelationList(ObservableList<Relation> list) {
        this.FAMILY_RELATION_TABLE.setItems(list);
    }

    private void populateWithFamily(Family f) {
        setMemberList(f.getMembers());
        setRelationList(f.getRelations());
    }


    @Override
    public void setTabAndTPane(JFXTabPane tabPane, Tab tab) {
        this.tab = tab;
        this.tabPane = tabPane;
    }


    @Override
    public void clean() {

    }
}