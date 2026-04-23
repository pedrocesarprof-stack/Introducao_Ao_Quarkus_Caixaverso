package com.ada.reguladora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxaRequest {
    private UUID clienteId;
    private Double valorSolicitado;
    private Integer prazoMeses;
}

