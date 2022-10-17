package org.acme;

import com.redhat.jigawatts.Jigawatts;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class TodoResource
{
    @Inject
    TodoService todoService;

    @OPTIONS
    public Response opt()
    {
        return Response.ok().build();
    }

    @GET
    public List<Todo> getAll()
    {
        return todoService.listAll();
    }

    @GET
    @Path("/{id}")
    public Todo getOne(@PathParam("id") Long id)
    {
        Todo entity = todoService.findById(id);
        if (entity == null)
        {
            throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
        }
        return entity;
    }

    @POST
    public Response create(Todo item)
    {
        System.out.println("Create: " + item);
        final Todo created = todoService.create(item);
        System.out.println("Create return: " + created);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PATCH
    @Path("/{id}")
    public Response update(Todo todo, @PathParam("id") Long id)
    {
        todoService.update(todo, id);
        return Response.ok(todo).build();
    }

    @DELETE
    public Response deleteCompleted()
    {
        todoService.deleteCompleted();
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOne(@PathParam("id") Long id)
    {
        System.out.println("Delete " + id);
        todoService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/checkpoint")
    public Response checkpoint()
    {
        try {
            Jigawatts.saveTheWorld("./target/tmp"  + System.currentTimeMillis());
            return Response.ok().build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}