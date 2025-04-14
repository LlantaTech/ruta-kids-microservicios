package org.pe.llantatech.parent.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.pe.llantatech.parent.model.Parent;

@ApplicationScoped
public class ParentRepository implements PanacheRepository<Parent> {
}
