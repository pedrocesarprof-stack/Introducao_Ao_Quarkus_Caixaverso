package com.ada.emprestimo.dto;

import com.ada.emprestimo.model.StatusParcela;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaResponse {
    private UUID id;
    private Integer ordem;
    private LocalDate dataVencimento;
    private BigDecimal valorAmortizacao;
    private BigDecimal valorJuros;
    private BigDecimal valorPrestacao;
    private StatusParcela status;
    private BigDecimal saldoDevedor;
}

