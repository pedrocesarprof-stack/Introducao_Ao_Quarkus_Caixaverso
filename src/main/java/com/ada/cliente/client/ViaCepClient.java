package com.ada.cliente.client;

import com.ada.cliente.dto.ViaCepResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "viacep-api")
@Path("/ws")
public interface ViaCepClient {

    @GET
    @Path("/{cep}/json")
    @Produces(MediaType.APPLICATION_JSON)
    ViaCepResponse buscarEnderecoPorCep(@PathParam("cep") String cep);
}

