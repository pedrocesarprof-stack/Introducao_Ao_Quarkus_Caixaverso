package com.ada.emprestimo.resource;

import com.ada.emprestimo.dto.EmprestimoRequest;
import com.ada.emprestimo.dto.EmprestimoResponse;
import com.ada.emprestimo.dto.ParcelaResponse;
import com.ada.emprestimo.model.StatusContrato;
import com.ada.emprestimo.service.EmprestimoService;
import com.ada.reguladora.client.ReguladoraClient;
import com.ada.reguladora.dto.TaxaRequest;
import com.ada.reguladora.dto.TaxaResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
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

    private static final List<EmprestimoResponse> emprestimos = new ArrayList<>();

    @POST
    public Response cadastrar(EmprestimoRequest request) {
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

        List<ParcelaResponse> parcelas = emprestimoService.calcularParcelas(request, taxaResponse.getTaxaJurosMensal());

        EmprestimoResponse response = EmprestimoResponse.builder()
                .id(UUID.randomUUID())
                .clienteId(request.getClienteId())
                .valorTotal(request.getValorTotal())
                .quantidadeParcelas(request.getQuantidadeParcelas())
                .tipoAmortizacao(request.getTipoAmortizacao())
                .status(StatusContrato.PENDENTE)
                .taxaJurosMensal(taxaResponse.getTaxaJurosMensal())
                .parcelas(parcelas)
                .build();

        emprestimos.add(response);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public List<EmprestimoResponse> listarTodos() {
        return emprestimos;
    }

    @DELETE
    @Path("/{id}")
    public Response cancelar(@PathParam("id") UUID id) {
        boolean removido = emprestimos.removeIf(e -> e.getId().equals(id));
        if (removido) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
