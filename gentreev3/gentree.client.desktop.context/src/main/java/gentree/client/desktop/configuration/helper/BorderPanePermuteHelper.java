package gentree.client.desktop.configuration.helper;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.Comparator;

/**
 * Created by vanilka on 11/01/2018.
 */
public class BorderPanePermuteHelper {

    public static void permute(BorderPane pane) {

        Node top = pane.getTop();
        if (top != null) {
            pane.setTop(null);
            pane.setTop(top);
        }

        Node bottom = pane.getBottom();
        if (bottom != null) {
            pane.setBottom(null);
            pane.setBottom(bottom);
        }
    }
}

