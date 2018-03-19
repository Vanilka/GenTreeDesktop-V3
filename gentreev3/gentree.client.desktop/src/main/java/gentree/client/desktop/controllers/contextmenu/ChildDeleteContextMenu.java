package gentree.client.desktop.controllers.contextmenu;

import gentree.client.desktop.configuration.enums.FilesFXML;
import gentree.client.desktop.configuration.messages.Keys;
import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.desktop.service.GenTreeContext;
import gentree.client.desktop.service.ScreenManager;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

import java.util.ResourceBundle;

/**
 * Created by vanilka on 11/03/2018.
 */
public class ChildDeleteContextMenu extends ContextMenu {

    private GenTreeContext context = GenTreeContext.INSTANCE;
    private ScreenManager sm = ScreenManager.INSTANCE;
    private Member member;
    private Relation relation;

    private MenuItem itemDeleteChild = new MenuItem(getValueFromKey(Keys.MENU_ITEM_DELETE_CHILD));


    public ChildDeleteContextMenu() {
        super();
        initItems();
        this.getItems().addAll(itemDeleteChild);
    }

    public void show(Member m, Relation r, Node node, ContextMenuEvent event) {
        member = m;
        relation = r;

        show(node, event.getScreenX(), event.getScreenY());

    }

    private void initItems() {
        initListener();
        initItemDelete();
    }

    private void initListener() {
        context.getBundle().addListener(this::languageChange);
    }

    private void languageChange(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        reloadLabels();
    }

    private void reloadLabels() {

    }


    private void initItemDelete() {
        itemDeleteChild.setOnAction(event -> context.getService().moveChildFromRelation(member, relation, null));
    }

    private String getValueFromKey(String key) {
        return context.getBundle().getValue().getString(key);
    }
}
