package gentree.client.visualization.elements.configuration;

import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.FamilyMember;
import gentree.client.visualization.elements.RelationTypeElement;
import javafx.scene.input.ContextMenuEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by vanilka on 13/01/2018.
 */
public class ManagerProvider implements Manager {

    public static final ManagerProvider INSTANCE = new ManagerProvider();

    @Setter
    @Getter
    private Manager manager;

    private ManagerProvider() {
    }

    @Override
    public void showRelationContextMenu(RelationTypeElement relationTypeElement, ContextMenuEvent event) {
        if (manager == null) return;
        manager.showRelationContextMenu(relationTypeElement, event);
    }

    @Override
    public void showSimContextMenu(FamilyMember familyMember, ContextMenuEvent event) {
        if (manager == null) return;
        manager.showSimContextMenu(familyMember, event);
    }

    @Override
    public void showInfoSim(Member member) {
        if (manager == null) return;
        manager.showInfoSim(member);
    }

    @Override
    public void showInfoRelation(Relation relation) {
        if (manager == null) return;
        manager.showInfoRelation(relation);
    }
}
