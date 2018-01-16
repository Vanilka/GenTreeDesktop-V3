package gentree.client.desktop.controllers.contextmenu;

import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.controllers.screen.DialogAddChildrenController;
import gentree.client.desktop.domain.Relation;
import gentree.client.desktop.service.GenTreeContext;
import gentree.client.desktop.service.ScreenManager;
import gentree.client.visualization.elements.RelationTypeElement;
import gentree.common.configuration.enums.RelationType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Martyna SZYMKOWIAK on 08/09/2017.
 */
public class RelationContextMenu extends ContextMenu {

    GenTreeContext context = GenTreeContext.INSTANCE;
    ScreenManager sm = ScreenManager.INSTANCE;
    RelationTypeElement relationTypeElement;

    private MenuItem itemChangeType = new MenuItem("ChangeType");
    private MenuItem itemAddChildren = new MenuItem("AddChildren");
    private MenuItem itemRemove = new MenuItem("RemoveRelation");
    private Menu menuChangeType = new Menu("changeType");
    private Menu menuChangeActive = new Menu("ChangeActive");
    private MenuItem yes = new MenuItem("Active");
    private MenuItem no = new MenuItem("No Active");

    public RelationContextMenu() {
        super();
        initItems();
        this.getItems().addAll(menuChangeActive, menuChangeType, itemAddChildren, itemRemove);
    }

    public void show(RelationTypeElement r, ContextMenuEvent event) {
        this.relationTypeElement = r;

        boolean isActive = r.getRelation().get().getActive();
        yes.setDisable(isActive);
        no.setDisable(!isActive);

        show(r, event.getScreenX(), event.getScreenY());
    }

    private void initItems() {
        initMenuChangeActive();
        initItemAddChildren();
        initMenuChangeType();
        initRemoveRelation();
    }

    private void initMenuChangeActive() {

        yes.setOnAction(event -> updateActiveRelation(relationTypeElement.getRelation().get(), true));
        no.setOnAction(event -> updateActiveRelation(relationTypeElement.getRelation().get(), false));

        List<MenuItem> activeItems = new ArrayList<>(Arrays.asList(yes, no));
        menuChangeActive.getItems().addAll(activeItems);
    }

    private void initMenuChangeType() {

        List<MenuItem> changeTypeItems = new ArrayList<>();

        for (RelationType relationType : RelationType.values()) {
            changeTypeItems.add(createMenuItemFromRelationType(relationType));
        }

        menuChangeType.getItems().addAll(changeTypeItems);
    }


    private MenuItem createMenuItemFromRelationType(RelationType relationType) {
        MenuItem item = new MenuItem(relationType.name());
        item.setOnAction(event -> {
            Relation relation = relationTypeElement.getRelation().get();
            relation.setType(relationType);
            context.getService().updateRelation(relation);
        });
        item.setOnAction(event -> {
            relationTypeElement.getRelation().get().setType(relationType);
            context.getService().updateRelation(relationTypeElement.getRelation().get());

        });
        return item;
    }

    private void initItemAddChildren() {
        itemAddChildren.setOnAction(event -> sm.showNewDialog( relationTypeElement.getRelation().get(), FilesFXML.DIALOG_ADD_CHILDREN));
    }

    private void initRemoveRelation() {
        itemRemove.setOnAction(event -> context.getService().removeRelation(relationTypeElement.getRelation().get()));
    }

    private void updateActiveRelation(Relation r , boolean active){
        r.setActive(active);
        context.getService().updateRelation(r);
    }


}
