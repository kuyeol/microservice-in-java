package org.acme.entity;


import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class DBInit {

    private final PgPool client;
    private final boolean schemaCreate;


    public DBInit(PgPool client, @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate) {
        this.client = client;
        this.schemaCreate = schemaCreate;
    }

    void onStart(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS users").execute()
                .flatMap(r -> client.query("CREATE TABLE users (id SERIAL PRIMARY KEY ,userId TEXT NOT NULL,userPw TEXT NOT NULL,email TEXT NOT NULL )").execute())
                .flatMap(r -> client.query("INSERT INTO users (userId,userPw,email) VALUES ('zimop','qwerty','aaa@aaa.com')").execute())
                .await().indefinitely();

    }


}
