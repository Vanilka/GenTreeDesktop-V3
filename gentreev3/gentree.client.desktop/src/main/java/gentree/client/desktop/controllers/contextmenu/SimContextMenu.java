package gentree.client.desktop.controllers.contextmenu;

import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.Keys;
import gentree.client.desktop.domain.Member;
import gentree.client.desktop.service.GenTreeContext;
import gentree.client.desktop.service.ScreenManager;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

import java.util.ResourceBundle;

/**
 * Created by Martyna SZYMKOWIAK on 20/07/2017.
 */
public class SimContextMenu extends ContextMenu {

    private GenTreeContext context = GenTreeContext.INSTANCE;
    private ScreenManager sm = ScreenManager.INSTANCE;
    private Member member;

    private MenuItem itemAddParents = new MenuItem(getValueFromKey(Keys.MENU_ITEM_ADD_PARENTS));
    private MenuItem itemAddSiblings = new MenuItem(getValueFromKey(Keys.MENU_ITEM_ADD_SPOUX));
    private MenuItem itemDelete = new MenuItem(getValueFromKey(Keys.MENU_ITEM_REMOVE));


    public SimContextMenu() {
        super();
        initItems();
        this.getItems().addAll(itemAddParents, itemAddSiblings, itemDelete);
    }

    public void show(Member m, Node node, ContextMenuEvent event) {
        member = m;
        show(node, event.getScreenX(), event.getScreenY());

    }

    private void initItems() {
        initListener();
        initItemAddParents();
        initItemAddSpouse();
        initItemDelete();
    }

    private void initListener() {
        context.getBundle().addListener(this::languageChange);
    }

    private void languageChange(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadLabels();
    }

    private void reloadLabels() {
        itemAddParents.setText(getValueFromKey(Keys.MENU_ITEM_ADD_PARENTS));
        itemAddSiblings.setText(getValueFromKey(Keys.MENU_ITEM_ADD_SPOUX));
        itemDelete.setText(getValueFromKey(Keys.MENU_ITEM_REMOVE));
    }


    private void initItemAddParents() {

        itemAddParents.setOnAction(event -> sm.showNewDialog(member, FilesFXML.DIALOG_ADD_PARENTS_TO_MEMBER));
    }

    private void initItemAddSpouse() {
        itemAddSiblings.setOnAction(event -> sm.showNewDialog(member, FilesFXML.DIALOG_ADD_SPOUSE_TO_MEMBER));
    }


    private void initItemDelete() {
        itemDelete.setOnAction(event -> context.getService().deleteMember(member));
    }

    private String getValueFromKey(String key) {
        return context.getBundle().getValue().getString(key);
    }
}
