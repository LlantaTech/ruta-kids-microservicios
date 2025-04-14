package org.pe.llantatech.parent.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.pe.llantatech.parent.model.Parent;

@ApplicationScoped
public class ParentKafkaProducer {

    @Inject
    @Channel("parent-created")
    Emitter<Parent> emitter;

    public void send(Parent parent) {
        emitter.send(parent);
    }
}
