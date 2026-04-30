package com.ada.emprestimo.resource;

import com.ada.emprestimo.dto.EmprestimoRequest;
import com.ada.emprestimo.dto.EmprestimoResponse;
import com.ada.emprestimo.service.EmprestimoService;
import com.ada.reguladora.client.ReguladoraClient;
import com.ada.reguladora.dto.TaxaRequest;
import com.ada.reguladora.dto.TaxaResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@Path("/emprestimos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprestimoResource {

    @Inject
    EmprestimoService emprestimoService;

    @RestClient
    ReguladoraClient reguladoraClient;

    @POST
    @RolesAllowed({"Gerente", "Usuario"})
    public Response cadastrar(@Valid EmprestimoRequest request) {
        TaxaRequest taxaRequest = TaxaRequest.builder()
                .clienteId(request.getClienteId())
                .valorSolicitado(request.getValorTotal().doubleValue())
                .prazoMeses(request.getQuantidadeParcelas())
                .build();

        TaxaResponse taxaResponse = reguladoraClient.simularTaxa(taxaRequest);

        if (taxaResponse == null || !taxaResponse.isAprovado()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\": \"Crédito não aprovado pela avaliadora externa.\"}")
                    .build();
        }

        EmprestimoResponse response = emprestimoService.contratar(request, taxaResponse.getTaxaJurosMensal());
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed({"Gerente", "Usuario"})
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoService.listarTodos();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Gerente")
    public Response cancelar(@PathParam("id") UUID id) {
        emprestimoService.cancelar(id);
        return Response.noContent().build();
    }
}
