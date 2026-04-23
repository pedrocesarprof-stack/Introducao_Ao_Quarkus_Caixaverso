package com.ada.reguladora.client;

import com.ada.reguladora.dto.TaxaRequest;
import com.ada.reguladora.dto.TaxaResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "reguladora-api")
@Path("/reguladora/taxas")
public interface ReguladoraClient {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TaxaResponse simularTaxa(TaxaRequest request);
}

