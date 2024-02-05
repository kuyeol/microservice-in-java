package org.acme.entity;import io.quarkus.runtime.StartupEvent;import io.vertx.mutiny.pgclient.PgPool;import jakarta.enterprise.context.ApplicationScoped;import jakarta.enterprise.event.Observes;import org.eclipse.microprofile.config.inject.ConfigProperty;@ApplicationScopedpublic class DBInit {    private final PgPool pgPool;    private final boolean schemaCreate;    public DBInit(PgPool pgPool, @ConfigProperty(name = "user.schema.create", defaultValue = "true") boolean schemaCreate) {        this.pgPool = pgPool;        this.schemaCreate = schemaCreate;    }    void onStart(@Observes StartupEvent ev) {        if (schemaCreate) {            initdb();        }    }    private void initdb() {        pgPool.query("DROP TABLE IG EXITS user").execute()                .flatMap(r -> pgPool.query("CREATE TABLE users (id SERIAL PRIMARY KEY ,userId TEXT NOT NULL,userPw TEXT NOT NULL )").execute())                .flatMap(r -> pgPool.query("INSERT INTO users (userId,userPw) VALUES ('zimop','qwerty')").execute())                .await().indefinitely();    }}