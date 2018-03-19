package gentree.client.desktop.service.implementation.tasks;

import gentree.client.desktop.configuration.enums.ServerPaths;
import gentree.client.desktop.domain.Relation;
import gentree.client.desktop.responses.ServiceResponse;
import gentree.client.desktop.service.responses.ExceptionResponse;
import gentree.client.desktop.service.responses.RelationListResponse;
import gentree.exception.ExceptionBean;
import gentree.server.dto.FamilyInjectable;
import gentree.server.dto.RelationDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by vanilka on 17/12/2017.
 */
@Log4j2
public class RelationConnectionTask extends ConnectionTask {

    public RelationConnectionTask() {
        super();
    }

    public ServiceResponse addRelation(Relation relation) {
        RelationDTO dto = cmd.convert(relation);
        setFamilyToDto(dto);
        Response response = cs.doPost(ServerPaths.RELATION.concat(ServerPaths.ADD), Entity.json(dto));
        return attendRelationListResponse(response);

    }

    public ServiceResponse updateRelation(Relation relation) {
        RelationDTO dto = cmd.convertSimple(relation);
        setFamilyToDto(dto);
        Response response = cs.doPost(ServerPaths.RELATION.concat(ServerPaths.UPDATE), Entity.json(dto));
        return  attendRelationListResponse(response);
    }

    public ServiceResponse removeRelation(Relation r) {
        RelationDTO dto = cmd.convertLazy(r);
        setFamilyToDto(dto);
        Response response = cs.doPost(ServerPaths.RELATION.concat(ServerPaths.DELETE), Entity.json(dto));
        return attendRelationListResponse(response);

    }


    private ServiceResponse attendRelationListResponse(Response response) {
        ServiceResponse serviceResponse = null;
        if (response.getStatus() == 200) {
            try {
                List<RelationDTO> returnedDTO = response.readEntity(new GenericType<List<RelationDTO>>() {
                });
                List<Relation> relations = cdm.convertRelationList(returnedDTO, service.getCurrentFamily());
                serviceResponse = new RelationListResponse(relations);

            } catch (Exception e) {
                e.printStackTrace();
              //  serviceResponse = new ExceptionResponse(response.readEntity(ExceptionBean.class));
            }
        }
        return serviceResponse;
    }

    private void setFamilyToDto(FamilyInjectable injectable) {
        injectable.setFamily(cmd.convertLazy(service.getCurrentFamily()));
    }

}
