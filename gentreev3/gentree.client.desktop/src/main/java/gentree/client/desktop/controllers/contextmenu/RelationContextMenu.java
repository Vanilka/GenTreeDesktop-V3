package gentree.client.desktop.controllers.contextmenu;

import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.Keys;
import gentree.client.desktop.domain.Relation;
import gentree.client.desktop.service.GenTreeContext;
import gentree.client.desktop.service.ScreenManager;
import gentree.client.visualization.elements.RelationTypeElement;
import gentree.configuration.enums.RelationType;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 08/09/2017.
 */
public class RelationContextMenu extends ContextMenu {

    GenTreeContext context = GenTreeContext.INSTANCE;
    ScreenManager sm = ScreenManager.INSTANCE;
    Relation relation;

    //RelationTypeElement relationTypeElement;

    private MenuItem itemChangeType = new MenuItem(getValueFromKey(Keys.MENU_ITEM_CHANGE_TYPE));
    private MenuItem itemAddChildren = new MenuItem(getValueFromKey(Keys.MENU_ITEM_ADD_CHILDREN));
    private MenuItem itemRemove = new MenuItem(getValueFromKey(Keys.MENU_ITEM_REMOVE));
    private Menu menuChangeType = new Menu(getValueFromKey(Keys.MENU_ITEM_CHANGE_TYPE));
    private Menu menuChangeActive = new Menu(getValueFromKey(Keys.MENU_ITEM_CHANGE_ACTIVE));
/*    private MenuItem yes = new MenuItem(getValueFromKey(Keys.YES));
    private MenuItem no = new MenuItem(getValueFromKey(Keys.NO));*/
    private MenuItem yes = new MenuItem(getValueFromKey(Keys.YES));
    private MenuItem no = new MenuItem(getValueFromKey(Keys.NO));


    public RelationContextMenu() {
        super();
        initItems();
        this.getItems().addAll(menuChangeActive, menuChangeType, itemAddChildren, itemRemove);
    }

    /**
     * Showing from Relation type element in GenTree
     * @param r
     * @param event
     */
    public void show(RelationTypeElement r, ContextMenuEvent event) {
       // this.relationTypeElement = r;
        this.relation = r.getRelation().get();

        boolean isActive = r.getRelation().get().getActive();
        yes.setDisable(isActive);
        no.setDisable(!isActive);

        show(r, event.getScreenX(), event.getScreenY());
    }


    /**
     * Showing from relation in the lst
     * @param r
     * @param n
     * @param event
     */
    public void show(Relation r, Node n, ContextMenuEvent event) {

        this.relation = r;
        boolean isActive = r.getActive();
        yes.setDisable(isActive);
        no.setDisable(!isActive);

        show(n, event.getScreenX(), event.getScreenY());
    }


    private void initItems() {
        initListener();
        initMenuChangeActive();
        initItemAddChildren();
        initMenuChangeType();
        initRemoveRelation();
    }

    private void initListener() {
        context.getBundle().addListener(this::languageChange);
    }

    private void languageChange(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadLabels();
    }

    private void reloadLabels() {
        itemChangeType.setText(getValueFromKey(Keys.MENU_ITEM_CHANGE_TYPE));
        itemAddChildren.setText(getValueFromKey(Keys.MENU_ITEM_ADD_CHILDREN));
        itemRemove.setText(getValueFromKey(Keys.MENU_ITEM_REMOVE));
        menuChangeType.setText(getValueFromKey(Keys.MENU_ITEM_CHANGE_TYPE));
        menuChangeActive.setText(getValueFromKey(Keys.MENU_ITEM_CHANGE_ACTIVE));
        yes.setText(getValueFromKey(Keys.YES));
        no.setText(getValueFromKey(Keys.NO));
    }

    private void initMenuChangeActive() {

/*
        yes.setOnAction(event -> updateActiveRelation(relationTypeElement.getRelation().get(), true));
        no.setOnAction(event -> updateActiveRelation(relationTypeElement.getRelation().get(), false));
*/

        yes.setOnAction(event -> updateActiveRelation(relation, true));
        no.setOnAction(event -> updateActiveRelation(relation, false));

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
            //Relation relation = relationTypeElement.getRelation().get();
            //relation.setType(relationType);

            context.getService().updateRelation(relation);
        });
        item.setOnAction(event -> {
           // relationTypeElement.getRelation().get().setType(relationType);
           // context.getService().updateRelation(relationTypeElement.getRelation().get());
            relation.setType(relationType);
            context.getService().updateRelation(relation);

        });
        return item;
    }

    private void initItemAddChildren() {
      //  itemAddChildren.setOnAction(event -> sm.showNewDialog(relationTypeElement.getRelation().get(), FilesFXML.DIALOG_ADD_CHILDREN));
        itemAddChildren.setOnAction(event -> sm.showNewDialog(relation, FilesFXML.DIALOG_ADD_CHILDREN));
    }

    private void initRemoveRelation() {
      //  itemRemove.setOnAction(event -> context.getService().removeRelation(relationTypeElement.getRelation().get()));
        itemRemove.setOnAction(event -> context.getService().removeRelation(relation));

    }

    private void updateActiveRelation(Relation r, boolean active) {
        r.setActive(active);
        context.getService().updateRelation(r);
    }


    private String getValueFromKey(String key) {
        return context.getBundle().getValue().getString(key);
    }


}
