package gentree.client.visualization.gustave.panels;

import gentree.client.visualization.elements.configuration.AutoCleanable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Martyna SZYMKOWIAK on 04/09/2017.
 */
public abstract class SubRelationPane extends SubBorderPane {

    @Getter
    @Setter(AccessLevel.NONE)
    protected HBox childrenBox;

    SubRelationPane() {
        childrenBox = new HBox();
    }


    public Node getConnectionNode() {
        return null;
    }

    @Override
    public void clean() {
        super.clean();
        childrenBox.getChildren().forEach(child -> {
            if(child instanceof AutoCleanable) ((AutoCleanable) child).clean();
        });
        childrenBox.getChildren().clear();
    }
}
