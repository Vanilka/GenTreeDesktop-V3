package gentree.client.visualization.elements;

import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.configuration.AutoCleanable;
import gentree.client.visualization.elements.configuration.Manager;
import gentree.client.visualization.elements.configuration.ManagerProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * Created by Martyna SZYMKOWIAK on 31/08/2017.
 */
public class RelationReference extends StackPane implements AutoCleanable {

    private static final Color color1 = Color.web("#ED0C44");
    private static final Color border1 = Color.web("#aa0b33");
    private static final Color border2 = Color.web("#ff144f");
    private final Polygon etiquete = new Polygon();

    private Text text = new Text();

    private ManagerProvider provider = ManagerProvider.INSTANCE;

    private EventHandler<? super ContextMenuEvent> contextMenuEvent = (EventHandler<ContextMenuEvent>) this::contextMenuHandle;
    private EventHandler<? super MouseEvent> mouseClickEvent = (EventHandler<MouseEvent>) this::mouseEventHandle;


    private ObjectProperty<RelationReferenceType> relationReferenceType = new SimpleObjectProperty<>();
    private ObjectProperty<Relation> relation = new SimpleObjectProperty<>();
    private ChangeListener<? super RelationReferenceType> relationTypeListener = this::relationTypeChange;
    private ChangeListener<? super Relation> relationListener = this::relationChange;

    public RelationReference() {
        this(null);
    }

    public RelationReference(RelationReferenceType type) {
        init();
        this.relationReferenceType.setValue(type);
    }


    private void init() {
        initColors();
        initListeners();
        getChildren().addAll(etiquete, text);
        this.setVisible(false);

        // this.setOnMouseClicked(mouseClickEvent);

    }

    private void initListeners() {
        relation.addListener(relationListener);
        relationReferenceType.addListener(relationTypeListener);
    }

    private void cleanListeners() {
        relation.removeListener(relationListener);
        relationReferenceType.removeListener(relationTypeListener);

        this.setOnMouseClicked(null);

        text.textProperty().unbind();
        this.visibleProperty().unbind();
    }

    @Override
    public void clean() {
        cleanListeners();
        getChildren().clear();
        setRelation(null);
        relation = null;

        setRelationReferenceType(null);
        relationReferenceType = null;

        relationTypeListener = null;
        relationListener = null;

        contextMenuEvent = null;
        mouseClickEvent = null;

        provider = null;

    }

    private void relationChange(ObservableValue<? extends Relation> observable, Relation oldValue, Relation newValue) {


        if (newValue != null) {
            text.textProperty().bind(newValue.referenceNumberProperty().asString());
            this.visibleProperty().bind(newValue.referenceNumberProperty().greaterThan(0));
        } else {
            this.visibleProperty().unbind();
            this.setVisible(false);
            text.textProperty().unbind();
            text.setText("");
        }
    }

    private void relationTypeChange(ObservableValue<? extends RelationReferenceType> observable, RelationReferenceType oldValue, RelationReferenceType newValue) {
        switch (newValue) {
            case ASC:
                drawAsc(etiquete);
                break;
            case DSC:
                drawDesc(etiquete);
                break;
            default:
                etiquete.getPoints().clear();
        }
    }

    private void contextChange(ObservableValue<? extends Manager> observable, Manager oldValue, Manager newValue) {
        if (newValue == null) {
            this.setOnMouseClicked(null);
        } else {
            this.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && relation.get() != null) {
                    newValue.showInfoRelation(relation.get());
                }
            });
        }
    }

    private void drawDesc(Polygon polygon) {
        polygon.getPoints().clear();
        polygon.getPoints().addAll(
                0.0, 0.0,
                40.0, 0.0,
                40.0, 40.0,
                20.0, 60.0,
                0.0, 40.0
        );
    }

    private void drawAsc(Polygon polygon) {
        polygon.getPoints().clear();
        polygon.getPoints().addAll(
                20.0, 0.0,
                40.0, 20.0,
                40.0, 60.0,
                0.0, 60.0,
                0.0, 20.0
        );
    }


    /*
        Getters && Setters
     */

    private void initColors() {
        etiquete.setFill(color1);
        etiquete.setStrokeWidth(5);
        etiquete.setStroke(border2);
    }

    public RelationReferenceType getRelationReferenceType() {
        return relationReferenceType.get();
    }

    public void setRelationReferenceType(RelationReferenceType relationReferenceType) {
        this.relationReferenceType.set(relationReferenceType);
    }

    public Relation getRelation() {
        return relation.get();
    }

    public void setRelation(Relation relation) {
        this.relation.set(relation);
    }

    public ObjectProperty<Relation> relationProperty() {
        return relation;
    }

    private void mouseEventHandle(MouseEvent event) {
        if (event.getClickCount() == 2 && relation.get() != null) {
            provider.showInfoRelation(relation.get());
        }
    }

    private void contextMenuHandle(ContextMenuEvent event) {
        //  provider.showRelationContextMenu(returnThis(), event);
    }

    public enum RelationReferenceType {
        ASC, DSC
    }
}
