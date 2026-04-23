package com.ada.emprestimo.dto;

import com.ada.emprestimo.model.TipoAmortizacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequest {
    private UUID clienteId;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    private TipoAmortizacao tipoAmortizacao;
}
