package com.ada.reguladora.resource;

import com.ada.reguladora.dto.TaxaRequest;
import com.ada.reguladora.dto.TaxaResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.ThreadLocalRandom;

@Path("/reguladora/taxas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReguladoraResource {

    @POST
    public TaxaResponse simularTaxa(TaxaRequest request) {
        int taxa = ThreadLocalRandom.current().nextInt(9, 17); // 9 a 16 inclusive
        return TaxaResponse.builder()
                .aprovado(true)
                .taxaJurosMensal(taxa)
                .build();
    }
}

