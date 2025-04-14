package org.pe.llantatech.parent.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.pe.llantatech.parent.model.Parent;
import org.pe.llantatech.parent.repository.ParentRepository;
import org.pe.llantatech.parent.kafka.ParentKafkaProducer;

@Path("/parents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParentResource {

    @Inject
    ParentRepository repository;

    @Inject
    ParentKafkaProducer producer;

    @GET
    public List<Parent> getAll() {
        return repository.listAll();
    }

    @POST
    @Transactional
    public Parent create(Parent parent) {
        repository.persist(parent);
        producer.send(parent);
        return parent;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        repository.deleteById(id);
    }
}
