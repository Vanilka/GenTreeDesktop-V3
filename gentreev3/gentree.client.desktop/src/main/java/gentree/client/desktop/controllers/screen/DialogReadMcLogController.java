package gentree.client.desktop.controllers.screen;

import gentree.client.desktop.configuration.ErrorMessages;
import gentree.client.desktop.configuration.GenTreeProperties;
import gentree.client.desktop.configuration.enums.PropertiesKeys;
import gentree.client.desktop.controllers.FXMLController;
import gentree.client.desktop.controllers.FXMLDialogController;
import gentree.client.desktop.domain.Member;
import gentree.client.desktop.extservice.mclog.McLogParser;
import gentree.client.desktop.extservice.mclog.McLogReader;
import gentree.client.desktop.extservice.mclog.TemplatesAllowed;
import gentree.configuration.enums.Age;
import gentree.configuration.enums.RelationType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.Configuration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by vanilka on 22/04/2018.
 */
@Log4j2
public class DialogReadMcLogController implements Initializable, FXMLController, FXMLDialogController {


    private final McLogReader logreader = McLogReader.INSTANCE;

    private final Configuration config = GenTreeProperties.INSTANCE.getConfiguration();

    private final Callback<TableColumn<String, String>, TableCell<String, String>> logCellFactory = generateLogCellFactory();

    @FXML
    private ObjectProperty<ResourceBundle> languageBundle = new SimpleObjectProperty<>();

    @FXML
    private TableView<String> LOG_TABLE_VIEW;

    @FXML
    private TableColumn<String, String> LOG_TEXT_COLUMN;

    @FXML
    private TableColumn<String, String> LOG_ACTION_COLUMN;


    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.languageBundle.setValue(resources);
        logreader.setPath(config.getString(PropertiesKeys.PARAM_PATH_MC_LOG));
        LOG_TABLE_VIEW.setItems(logreader.getLogContent());
        LOG_TEXT_COLUMN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        LOG_ACTION_COLUMN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        LOG_ACTION_COLUMN.setCellFactory(logCellFactory);
        logreader.loadFile();
    }


    @Override
    public void clean() {

    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }


    private Callback<TableColumn<String, String>, TableCell<String, String>> generateLogCellFactory() {
        Callback<TableColumn<String, String>, TableCell<String, String>> cellFactory
                = //
                new Callback<TableColumn<String, String>, TableCell<String, String>>() {
                    @Override
                    public TableCell call(final TableColumn<String, String> param) {
                        final TableCell<String, String> cell = new TableCell<String, String>() {


                            final Button btn = new Button("Just Do It");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        if (item.contains(TemplatesAllowed.TEMPLATE_CHANGE_AGE)) {

                                            processAgeChangingLog(item, event);
                                        } else if (item.contains(TemplatesAllowed.TEMPLATE_MARRIED)) {
                                            processMarriedLog(item, event);
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }

                        };
                        return cell;
                    }
                };

        return cellFactory;

    }

    private void processMarriedLog(String item, ActionEvent event) {

        String householdname = McLogParser.takeHouseholdNameFromMarriedLog(item);
        System.out.println("householdname [" + householdname + "]");
        Member firstSim = findMemberFromString(McLogParser.takeFirstSimNameFromMarriedLog(item));
        if (firstSim == null) {
            sm.showError(ErrorMessages.TITLE_ERROR_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND);
            return;
        }


        Member secondSim = findMemberFromString(McLogParser.takeSecondSimNameFromMarriedLog(item));
        if (secondSim == null) {
            sm.showError(ErrorMessages.TITLE_ERROR_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND);
            return;
        }

        context.getService().addRelation(firstSim, secondSim, RelationType.MARRIED, true);

        if (householdname != null
                && !householdname.isEmpty()
                && (firstSim.getBornname().isEmpty()
                || firstSim.getSurname().equals(firstSim.getBornname()))) {
            firstSim.setBornname(firstSim.getSurname());
            firstSim.setSurname(householdname);
            context.getService().updateMember(firstSim);
        }

    }

    private void processAgeChangingLog(String item, ActionEvent event) {
        String ageString = item.substring(item.lastIndexOf(" "), item.length()).trim();
        Age age = McLogParser.getAgeFromString(ageString);

        if (age == null) {
            sm.showError(ErrorMessages.TITLE_AGE_NOT_FOUND,
                    ErrorMessages.HEADER_AGE_NOT_FOUND,
                    ErrorMessages.HEADER_AGE_NOT_FOUND);
            event.consume();
            return;
        }

        Member m = findMemberFromChangeAgeString(item);
        if (m == null) {
            sm.showError(ErrorMessages.TITLE_ERROR_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND,
                    ErrorMessages.HEADER_MEMBER_NOT_FOUND);
            event.consume();
            return;
        }
        if (!age.equals(m.getAge())) {
            m.setAge(age);
            context.getService().updateMember(m);
        }
    }

    private Member findMemberFromChangeAgeString(String item) {
        String nameAndSurname = McLogParser.takeNamSurnameFromChangeAgeLog(item);
        String name = nameAndSurname.substring(0, nameAndSurname.indexOf(" ")).trim();
        String surname = nameAndSurname.substring(nameAndSurname.indexOf(" "), nameAndSurname.length()).trim();
        return context.getService().findMemberByNameAndSurname(name, surname);
    }


    private Member findMemberFromString(String item) {
        if (item == null) return null;
        String name = item.substring(0, item.indexOf(" ")).trim();
        String surname = item.substring(item.indexOf(" "), item.length()).trim();
        return context.getService().findMemberByNameAndSurname(name, surname);
    }


}