package com.ada.emprestimo.dto;

import com.ada.emprestimo.model.StatusContrato;
import com.ada.emprestimo.model.TipoAmortizacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponse {
    private UUID id;
    private UUID clienteId;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    private TipoAmortizacao tipoAmortizacao;
    private StatusContrato status;
    private Integer taxaJurosMensal;
    private List<ParcelaResponse> parcelas;
}

