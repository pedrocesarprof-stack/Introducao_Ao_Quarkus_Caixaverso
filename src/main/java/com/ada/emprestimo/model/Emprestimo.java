package com.ada.emprestimo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {
    @Id
    private UUID id;
    private UUID clienteId;
    private BigDecimal valorTotal;
    private Integer quantidadeParcelas;
    @Enumerated(EnumType.STRING)
    private TipoAmortizacao tipoAmortizacao;
    @Enumerated(EnumType.STRING)
    private StatusContrato status;
    private Integer taxaJurosMensal;
    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas;
}
