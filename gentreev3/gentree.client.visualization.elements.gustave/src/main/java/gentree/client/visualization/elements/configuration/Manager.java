package gentree.client.visualization.elements.configuration;

import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;
import gentree.client.visualization.elements.FamilyMember;
import gentree.client.visualization.elements.RelationTypeElement;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;

public interface Manager {

    void showRelationContextMenu(RelationTypeElement relationTypeElement, ContextMenuEvent event);

    void showSimContextMenu(FamilyMember familyMember, ContextMenuEvent event);

    void showInfoSim(Member member);

    void showInfoRelation(Relation relation);
}
