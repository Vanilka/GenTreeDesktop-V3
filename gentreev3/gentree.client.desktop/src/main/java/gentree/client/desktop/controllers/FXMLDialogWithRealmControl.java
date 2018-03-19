package gentree.client.desktop.controllers;

import gentree.client.desktop.configuration.Realm;
import javafx.collections.ObservableList;

/**
 * Created by Martyna SZYMKOWIAK on 22/10/2017.
 */
public interface FXMLDialogWithRealmControl extends FXMLDialogController {

    void setRealm(Realm realm);
}
