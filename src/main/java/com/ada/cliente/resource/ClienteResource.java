package com.ada.cliente.resource;

import com.ada.cliente.dto.ClienteRequest;
import com.ada.cliente.dto.ClienteResponse;
import com.ada.cliente.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService clienteService;

    @GET
    public List<ClienteResponse> listarTodos() {
        return clienteService.listarTodos();
    }

    @POST
    public Response cadastrar(ClienteRequest request) {
        ClienteResponse response = clienteService.cadastrar(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{documento}")
    public Response buscarPorDocumento(@PathParam("documento") String documento) {
        ClienteResponse response = clienteService.buscarPorDocumento(documento);
        return Response.ok(response).build();
    }
}
