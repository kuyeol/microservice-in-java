package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.quarkus.qute.Template;


@Path("/hello")
public class GreetingResource {


    @Inject
    Template index2;


    private String greeting;
    private  String greeting1;
    public int n;
    public void GreetingR(String greeting1 , int n)
    {
        this.greeting1=greeting1;
        this.n=n;
    }


    public GreetingResource(@ConfigProperty(name="greeting.name") String greeting)
    {
        this.greeting=greeting;
    }
    public GreetingResource(@ConfigProperty(name="greeting.id") int greeting1)
    {
        this.greeting1= String.valueOf(greeting1);
    }

    GreetingR greetingR;

    public GreetingR getGreetingR(String greeting1,int n) {
        return getGreetingR("asdf",10);
    }

//    @GET
//    @Path("a")
//    @Produces(MediaType.TEXT_HTML)
//    public TemplateInstance get(String greeting) {
//        return get(greeting);
//    }

    @GET
    @Path("AA")
    @Produces(MediaType.TEXT_HTML)
    public String get(@ConfigProperty(name="greeting.name") String greeting) {

        return this.greeting;
    }

    @GET
    @Path("A")
    @Produces(MediaType.TEXT_HTML)
    public String get1(@ConfigProperty(name="greeting.id") int greeting1) {

        return this.greeting1;
    }


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String hello1() {
        return this.greeting;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return this.greeting;
    }

    private class GreetingR {
    }
}
