package gentree.client.visualization.elements;

import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.configuration.ManagerProvider;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
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
        init();

    }

    public RelationTypeElement(Relation relation) {
        super(relation);
        init();

    }


    private void init() {
        this.setOnContextMenuRequested(contextMenuEvent);
        this.setOnMouseClicked(mouseClickEvent);
        embleme.setOnMouseClicked(mouseClickEvent);
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
        System.out.println("Im working");
        if (event.getClickCount() == 2 && relation.get() != null) {
            provider.showInfoRelation(relation.get());
        }
    }

    private void contextMenuHandle(ContextMenuEvent event) {
        System.out.println("Im working");
        provider.showRelationContextMenu(returnThis(), event);
    }
}
