package gentree.client.desktop.service;

import com.jfoenix.controls.JFXTabPane;
import gentree.client.desktop.configuration.ErrorMessages;
import gentree.client.desktop.configuration.GenTreeProperties;
import gentree.client.desktop.configuration.Realm;
import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.helper.BorderPanePermuteHelper;
import gentree.client.desktop.configuration.messages.AppTitles;
import gentree.client.desktop.configuration.messages.LogMessages;
import gentree.client.desktop.configuration.wrappers.PhotoMarshaller;
import gentree.client.desktop.controllers.*;
import gentree.client.desktop.controllers.contextmenu.ChildDeleteContextMenu;
import gentree.client.desktop.controllers.contextmenu.RelationContextMenu;
import gentree.client.desktop.controllers.contextmenu.SimContextMenu;
import gentree.client.desktop.controllers.screen.*;
import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.FamilyMember;
import gentree.client.visualization.elements.RelationReference;
import gentree.client.visualization.elements.RelationTypeElement;
import gentree.client.visualization.elements.configuration.ImageFiles;
import gentree.client.visualization.elements.configuration.Manager;
import gentree.client.visualization.elements.configuration.ManagerProvider;
import gentree.client.visualization.service.implementation.GenTreeDrawingServiceImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Martyna SZYMKOWIAK on 01/07/2017.
 */
@Getter
@Setter
@Log4j2
public class ScreenManager implements Manager {

    public static final ScreenManager INSTANCE = new ScreenManager();
    public static final GenTreeContext context = GenTreeContext.INSTANCE;
    public static final GenTreeProperties properties = GenTreeProperties.INSTANCE;
    private static final String PREFIX_FILE_LOAD = "file://";
    private static final FileChooser imgFileChooser = new FileChooser();
    private static final FileChooser xmlFileChooser = new FileChooser();
    private static String LAST_PATH;

    /*
        Commons controllers
     */
    private ScreenMainController screenMainController;
    private MainMenuController mainMenuController;
    private MainFooterController mainFooterController;

    private ScreenWelcomeController screenWelcomeController;

    private RootWindowController rootWindowController;
    private GenTreeDrawingService genTreeDrawingService;
    /*
        Commons Panes
     */
    private ScreenMainRightController screenMainRightController;
    private BorderPane mainWindowBorderPane;
    private Stage stage;
    private Scene scene;


    /*
        Context Menus
     */

    private SimContextMenu simContextMenu;
    private RelationContextMenu relationContextMenu;
    private ChildDeleteContextMenu childDeleteContextMenu;


    private ScreenManager() {
        LAST_PATH = System.getProperty("user.home");
    }

    private synchronized static void initStage(Stage stage) {
        stage.setTitle(AppTitles.APP_TITLE);
        stage.setHeight(750);
        stage.setWidth(1200);
        stage.setMinHeight(750);
        stage.setMinWidth(1200);
        stage.show();
    }

    public void init(Stage stage) {
        this.stage = stage;
        simContextMenu = new SimContextMenu();
        relationContextMenu = new RelationContextMenu();
        childDeleteContextMenu = new ChildDeleteContextMenu();
        configureStatics();
        initRoot();
    }

    /**
     * Configure Singletons
     */
    private void configureStatics() {
        ManagerProvider.INSTANCE.setManager(this);

        PhotoMarshaller.addIgnoredPaths(ImageFiles.GENERIC_FEMALE.toString(),
                ImageFiles.GENERIC_FEMALE.toString());
        Member.setDefaultFemaleLocation(ImageFiles.GENERIC_FEMALE.toString());
        Member.setDefaultMaleLocation(ImageFiles.GENERIC_MALE.toString());


        GenTreeDrawingServiceImpl.setContext(context);
    }

    /**
     * This function initialize Root BorderPane with his children
     */
    private void initRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FilesFXML.ROOT_WINDOW_FXML.toString()));
        try {
            this.mainWindowBorderPane = ((BorderPane) loader.load());
            this.rootWindowController = loader.getController();
            initDefaultControllers();
            this.scene = new Scene(mainWindowBorderPane);
            stage.setScene(scene);
            initStage(this.stage);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            showError(ErrorMessages.TITLE_ERROR_INIT_ROOT, ErrorMessages.HEADER_ERROR_INIT_ROOT, ex.getMessage());
        }

    }

    /**
     * This method load default view of application after Launch. </br>
     * Set Top, Bottom and Center Panes info Root BorderPane
     */
    private void initDefaultControllers() {
        mainFooterController = (MainFooterController) loadFxml(this.mainWindowBorderPane, FilesFXML.MAIN_FOOTER_FXML, Where.BOTTOM);
        mainMenuController = (MainMenuController) loadFxml(this.mainWindowBorderPane, FilesFXML.MAIN_MENU_FXML, Where.TOP);
        screenWelcomeController = (ScreenWelcomeController) loadFxml(this.mainWindowBorderPane, FilesFXML.SCREEN_WELCOME_FXML, Where.CENTER);
    }

    public FXMLPane loadFxml(BorderPane border, FilesFXML fxml, Where where) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            AnchorPane temp = (AnchorPane) loader.load();
            FXMLPane controller = loader.getController();
            switch (where) {
                case TOP:
                    border.setTop(temp);
                    break;
                case CENTER:
                    border.setCenter(temp);
                    BorderPanePermuteHelper.permute(border);
                    break;
                case BOTTOM:
                    border.setBottom(temp);
                    break;
            }

        } catch (IOException ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
        return null;

    }

    public void showNewDialog(FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());

        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogController controller = loader.getController();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.getStage());
            Scene scene = new Scene(dialogwindow);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
    }

    public void showNewDialog(ObservableList<Realm> list, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogWithRealmListControl controller = loader.getController();
            controller.setList(list);
            Scene scene = new Scene(dialogwindow);
            initDialogProperties(dialogStage, Modality.WINDOW_MODAL, this.getStage(), scene, false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
    }

    public void showNewDialog(Realm realm, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogWithRealmControl controller = loader.getController();
            controller.setRealm(realm);
            Scene scene = new Scene(dialogwindow);
            initDialogProperties(dialogStage, Modality.WINDOW_MODAL, this.getStage(), scene, false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
    }

    public FXMLDialogWithMemberController showNewDialog(Member member, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogWithMemberController controller = loader.getController();
            controller.setFather(member);
            Scene scene = new Scene(dialogwindow);
            initDialogProperties(dialogStage, Modality.WINDOW_MODAL, this.getStage(), scene, false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            return controller;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }

        return null;
    }

    public void showNewDialog(Relation r, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogWithRelationController controller = loader.getController();
            controller.setRelation(r);
            Scene scene = new Scene(dialogwindow);
            initDialogProperties(dialogStage, Modality.WINDOW_MODAL, this.getStage(), scene, false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }

    }

    public Member showNewDialog(Member member, List<Member> list, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            Stage dialogStage = new Stage();
            AnchorPane dialogwindow = (AnchorPane) loader.load();
            FXMLDialogReturningMember controller = loader.getController();
            controller.setMember(member);
            controller.setMemberList(list);
            Scene scene = new Scene(dialogwindow);
            initDialogProperties(dialogStage, Modality.WINDOW_MODAL, this.getStage(), scene, false);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();
            return controller.getResult();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
        return null;
    }

    private void initDialogProperties(Stage dialogStage, Modality modality, Window owner, Scene scene, boolean resizeable) {
        dialogStage.initModality(modality);
        dialogStage.initOwner(owner);
        dialogStage.setScene(scene);
        dialogStage.setResizable(resizeable);
    }

    public FXMLPane loadFxml(Pane pane, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            pane.getChildren().clear();
            pane.getChildren().addAll((Pane) loader.load());
            FXMLPane controller = loader.getController();
            return controller;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
        }
        return null;
    }


    public FXMLPane loadFxml(AnchorPane anchor, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            anchor.getChildren().clear();
            anchor.getChildren().addAll((AnchorPane) loader.load());
            return loader.getController();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
        return null;
    }

    public FXMLTab loadFxml(JFXTabPane tabPane, Tab tab, FilesFXML fxml, String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            tab.setContent(loader.load());
            tab.setText(title);
            FXMLTab controller = loader.getController();
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            controller.setTabAndTPane(tabPane, tab);
            return controller;

        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            log.error(ex.getCause());


        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
        }

        return null;
    }

    public FXMLTab loadFxml(JFXTabPane tabPane, Tab tab, String fxml) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml), context.getBundleValue());
        try {
            tab.setContent(loader.load());
            FXMLTab controller = loader.getController();
            controller.setTabAndTPane(tabPane, tab);
            return controller;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }

        return null;
    }

    public FXMLPane loadAdditionalFxmlToAnchorPane(AnchorPane anchor, FilesFXML fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.toString()), context.getBundleValue());
        try {
            anchor.getChildren().addAll((AnchorPane) loader.load());
            return loader.getController();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(ex.getCause());
            ex.printStackTrace();
        }
        return null;
    }

    public void showNotification(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(10))
                .darkStyle()
                .position(Pos.BOTTOM_RIGHT);
        notificationBuilder.show();
    }

    public String setImageIntoShape(Shape shape) {
        File file = openImageFileChooser();
        String path = "";
        if (file != null) {
            try {
                path = PREFIX_FILE_LOAD.concat(file.getCanonicalPath());
                shape.setFill(new ImagePattern(new Image(path)));
            } catch (Exception e) {
                log.error(LogMessages.MSG_ERROR_LOAD_IMAGE);
                e.printStackTrace();
                shape.setFill(new ImagePattern(new Image(ImageFiles.NO_NAME_MALE.toString())));
            }
        }
        return path;
    }

    public String setImageIntoImageView(ImageView imv) {
        File file = openImageFileChooser();
        String path = "";
        if (file != null) {
            try {
                path = PREFIX_FILE_LOAD.concat(file.getCanonicalPath());
                imv.setImage(new Image(path));
            } catch (Exception e) {
                log.error(LogMessages.MSG_ERROR_LOAD_IMAGE);
                e.printStackTrace();
                imv.setImage(new Image(ImageFiles.NO_NAME_MALE.toString()));
            }
        }
        return path;
    }


    public File openImageFileChooser() {
        configureImageFileChooser(imgFileChooser);
        File file = imgFileChooser.showOpenDialog(stage);
        LAST_PATH = file == null ? LAST_PATH : file.getParent();
        return file;

    }

    public File openXMLFileChooser() {
        configureXmlFileChooser(xmlFileChooser);
        File file = xmlFileChooser.showOpenDialog(stage);
        LAST_PATH = file == null ? LAST_PATH : file.getParent();
        return file;
    }

    public File saveXMLFileChooser() {
        configureXmlFileChooser(xmlFileChooser);
        File file = xmlFileChooser.showSaveDialog(stage);
        LAST_PATH = file == null ? LAST_PATH : file.getParent();
        return file;
    }

    private void configureImageFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle(context.getBundleValue().getString("select_image_dialog"));
        fileChooser.setInitialDirectory(
                new File(LAST_PATH)
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void configureXmlFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle(context.getBundleValue().getString("select_xml_dialog"));
        fileChooser.setInitialDirectory(
                new File(LAST_PATH)
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
    }

    public void reloadScreenWelcomeController() {
        screenWelcomeController = (ScreenWelcomeController) loadFxml(this.mainWindowBorderPane, FilesFXML.SCREEN_WELCOME_FXML, Where.CENTER);
        context.setService(null);
    }

    @Override
    public void showInfoSim(Member member) {
        getScreenMainController().showInfoSim(member);
    }

    @Override
    public void showInfoRelation(Relation relation) {
        getScreenMainController().showInfoRelation(relation);
    }

    @Override
    public void showSimContextMenu(FamilyMember familyMember, ContextMenuEvent event) {
        simContextMenu.show(familyMember.getMember(), familyMember, event);
    }

    public void showSimContextMenu(Member m, Node node, ContextMenuEvent event) {
        simContextMenu.show(m, node, event);
    }

    @Override
    public void showRelationContextMenu(RelationTypeElement relationTypeElement, ContextMenuEvent event) {
        relationContextMenu.show(relationTypeElement, event);
    }

    public void showRelationContextMenu(Relation r, Node node, ContextMenuEvent event) {
        relationContextMenu.show(r, node, event);
    }

    public void showSimDeleteContextMenu(Member member, Relation fromRelation, Node n, ContextMenuEvent event) {
        childDeleteContextMenu.show(member, fromRelation, n, event);
    }

    /*
            SHOW WARNINGS
     */

    public void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void showErrorAndQuit(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

        reloadScreenWelcomeController();
    }

    /*
        Register Screen
     */

    public void register(ScreenMainRightController controller) {
        this.screenMainRightController = controller;
    }

    public void register(ScreenMainController controller) {
        this.screenMainController = controller;
    }

    public void register(GenTreeDrawingService drawingService) {
        this.genTreeDrawingService = drawingService;
    }


    public enum Where {
        TOP,
        CENTER,
        BOTTOM
    }

}
