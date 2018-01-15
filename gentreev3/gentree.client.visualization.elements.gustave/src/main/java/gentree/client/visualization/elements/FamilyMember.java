package gentree.client.visualization.elements;

import gentree.client.desktop.domain.Member;
import gentree.client.visualization.controls.CircleEmbleme;
import gentree.client.visualization.elements.configuration.AutoCleanable;
import gentree.client.visualization.elements.configuration.ElementsConfig;
import gentree.client.visualization.elements.configuration.ManagerProvider;
import gentree.common.configuration.enums.Age;
import gentree.common.configuration.enums.Gender;
import gentree.common.configuration.enums.Race;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Created by Martyna SZYMKOWIAK on 20/07/2017.
 */
public class FamilyMember extends FamilyMemberCard implements AutoCleanable {

    private final static int MEMBER_WIDTH = 140;
    private final static int MEMBER_HEIGHT = 220;
    private final static String pathfxml = "/layout/elements/family.member.fxml";

    private ManagerProvider provider = ManagerProvider.INSTANCE;
    private EventHandler<? super ContextMenuEvent> contextMenuEvent = (EventHandler<ContextMenuEvent>) this::contextMenuHandle;
    private EventHandler<? super MouseEvent> mouseClickEvent = (EventHandler<MouseEvent>) this::mouseEventHandle;


    @FXML
    private CircleEmbleme GENDER_IMG;

    @FXML
    private CircleEmbleme RACE_IMG;

    @FXML
    private CircleEmbleme AGE_IMG;


    public FamilyMember(Member member) {
        super(member, pathfxml);
        initialize();
        resize(MEMBER_WIDTH, MEMBER_HEIGHT);
    }

    public FamilyMember() {
        this(null);
    }

    private void initialize() {

        this.setOnMouseEntered(this::mouseEnterEvent);
        this.setOnMouseExited(this::mouseExitedEvent);

        this.setOnContextMenuRequested(contextMenuEvent);
        this.setOnMouseClicked(mouseClickEvent);
    }


    @Override
    protected void fillComponents(Member member) {
        super.fillComponents(member);
        if (member == null) {
            GENDER_IMG.setImgPath(ec.getFlePathOfGender(Gender.M));
            RACE_IMG.setImgPath(ec.getFilePathOfRace(Race.HUMAIN));
            AGE_IMG.setImgPath(ec.getFilePathOfAge(Age.YOUNG_ADULT));
        } else {
            GENDER_IMG.setImgPath(ec.getFlePathOfGender(member.getGender()));
            RACE_IMG.setImgPath(ec.getFilePathOfRace(member.getRace()));
            AGE_IMG.setImgPath(ec.getFilePathOfAge(member.getAge()));
        }
    }

    @Override
    public void clean() {
        super.clean();
        getChildren().clear();
        this.setOnContextMenuRequested(null);
        this.setOnMouseClicked(null);

        this.provider = null;
        this.ec = null;
        this.mouseClickEvent = null;
        this.contextMenuEvent = null;


        GENDER_IMG.clean();
        GENDER_IMG = null;

        RACE_IMG.clean();
        RACE_IMG = null;

        AGE_IMG.clean();
        AGE_IMG = null;


    }

    private FamilyMember returnThis() {
        return this;
    }

    private void mouseEnterEvent(MouseEvent t) {
       // setStrokeColor(Color.valueOf("#0188AE"));
        setColorOnBackground(Color.valueOf("#0B4F6C"));
    }

    private void mouseExitedEvent(MouseEvent t) {
       // setStrokeColor(Color.WHITE);
        setColorOnBackground(Color.valueOf("#7f8d8a"));
    }

    private void setColorOnBackground(Color color) {
        rectangleFond.setFill(color);
       // rectanglePhotoFond.setFill(color);
    }

    private void setStrokeColor(Color color) {
        rectangleFond.setStroke(color);
        rectanglePhotoFond.setStroke(color);
    }

    private void mouseEventHandle(MouseEvent event) {
        if (event.getClickCount() == 2 && getMember() != null) {
            provider.showInfoSim(getMember());
        }
    }

    private void contextMenuHandle(ContextMenuEvent event) {
        provider.showSimContextMenu(returnThis(), event);
    }
}
