package gentree.client.visualization.elements;

import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.configuration.ManagerProvider;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Martyna SZYMKOWIAK on 30/07/2017.
 */
@Getter
@Setter
public class RelationTypeElement extends RelationTypeCard {

    private ManagerProvider provider = ManagerProvider.INSTANCE;

    private EventHandler<? super ContextMenuEvent> contextMenuEvent = (EventHandler<ContextMenuEvent>) this::contextMenuHandle;
    private EventHandler<? super MouseEvent> mouseClickEvent = (EventHandler<MouseEvent>) this::mouseEventHandle;

    {
        init();
    }

    public RelationTypeElement() {
        super();
    }

    public RelationTypeElement(Relation relation) {
        super(relation);
    }


    private void init() {

        this.setOnMouseClicked(event -> {
            System.out.println("Relation " + this.getRelation().get() + " -> Has reference   " + this.getRelation().get().getReferenceNumber());
        });

            this.setOnContextMenuRequested(contextMenuEvent);
            this.setOnMouseClicked(mouseClickEvent);
    }


    private RelationTypeElement returnThis() {
        return this;
    }


    @Override
    public void clean() {
        super.clean();
        this.setOnMouseClicked(null);
        this.setOnContextMenuRequested(null);
        getChildren().clear();
        relation.setValue(null);
        relation = null;
        relationType.setValue(null);
        relationType = null;

        provider = null;

        contextMenuEvent = null;
        mouseClickEvent = null;

    }


    private void mouseEventHandle(MouseEvent event) {
        if (event.getClickCount() == 2 && relation.get() != null) {
            provider.showInfoRelation(relation.get());
        }
    }

    private void contextMenuHandle(ContextMenuEvent event) {
        provider.showRelationContextMenu(returnThis(), event);
    }
}
