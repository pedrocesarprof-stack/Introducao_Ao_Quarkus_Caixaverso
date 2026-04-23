package com.ada.reguladora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxaResponse {
    private boolean aprovado;
    private Integer taxaJurosMensal;
}

