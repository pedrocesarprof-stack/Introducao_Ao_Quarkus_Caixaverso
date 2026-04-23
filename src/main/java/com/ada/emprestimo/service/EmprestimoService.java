package com.ada.emprestimo.service;

import com.ada.emprestimo.dto.EmprestimoRequest;
import com.ada.emprestimo.dto.ParcelaResponse;
import com.ada.emprestimo.model.StatusParcela;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EmprestimoService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public List<ParcelaResponse> calcularParcelas(EmprestimoRequest request, Integer taxaJurosMensal) {
        return switch (request.getTipoAmortizacao()) {
            case SAC -> calcularSAC(request, taxaJurosMensal);
            case PRICE -> calcularPRICE(request, taxaJurosMensal);
        };
    }

    private List<ParcelaResponse> calcularSAC(EmprestimoRequest request, Integer taxaJurosMensal) {
        List<ParcelaResponse> parcelas = new ArrayList<>();
        int n = request.getQuantidadeParcelas();
        BigDecimal taxa = BigDecimal.valueOf(taxaJurosMensal)
                .divide(BigDecimal.valueOf(100), 10, ROUNDING);
        BigDecimal amortizacao = request.getValorTotal()
                .divide(BigDecimal.valueOf(n), SCALE, ROUNDING);
        BigDecimal saldo = request.getValorTotal();

        for (int i = 1; i <= n; i++) {
            // Na última parcela, amortização = saldo restante (evita diferença de arredondamento)
            if (i == n) {
                amortizacao = saldo.setScale(SCALE, ROUNDING);
            }
            BigDecimal juros = saldo.multiply(taxa).setScale(SCALE, ROUNDING);
            BigDecimal prestacao = amortizacao.add(juros).setScale(SCALE, ROUNDING);
            saldo = saldo.subtract(amortizacao).setScale(SCALE, ROUNDING);

            parcelas.add(ParcelaResponse.builder()
                    .id(UUID.randomUUID())
                    .ordem(i)
                    .dataVencimento(LocalDate.now().plusMonths(i))
                    .valorAmortizacao(amortizacao)
                    .valorJuros(juros)
                    .valorPrestacao(prestacao)
                    .status(StatusParcela.PENDENTE)
                    .saldoDevedor(saldo)
                    .build());
        }
        return parcelas;
    }

    private List<ParcelaResponse> calcularPRICE(EmprestimoRequest request, Integer taxaJurosMensal) {
        List<ParcelaResponse> parcelas = new ArrayList<>();
        int n = request.getQuantidadeParcelas();
        BigDecimal taxa = BigDecimal.valueOf(taxaJurosMensal)
                .divide(BigDecimal.valueOf(100), 10, ROUNDING);
        BigDecimal valorTotal = request.getValorTotal();

        // prestacao = P * (i * (1+i)^n) / ((1+i)^n - 1)
        BigDecimal fator = taxa.add(BigDecimal.ONE).pow(n, new MathContext(15, ROUNDING));
        BigDecimal prestacao = valorTotal
                .multiply(taxa.multiply(fator))
                .divide(fator.subtract(BigDecimal.ONE), SCALE, ROUNDING);

        BigDecimal saldo = valorTotal;

        for (int i = 1; i <= n; i++) {
            BigDecimal juros = saldo.multiply(taxa).setScale(SCALE, ROUNDING);
            BigDecimal amortizacao = prestacao.subtract(juros).setScale(SCALE, ROUNDING);

            // Na última parcela, amortização = saldo restante
            if (i == n) {
                amortizacao = saldo.setScale(SCALE, ROUNDING);
                prestacao = amortizacao.add(juros).setScale(SCALE, ROUNDING);
            }
            saldo = saldo.subtract(amortizacao).setScale(SCALE, ROUNDING);

            parcelas.add(ParcelaResponse.builder()
                    .id(UUID.randomUUID())
                    .ordem(i)
                    .dataVencimento(LocalDate.now().plusMonths(i))
                    .valorAmortizacao(amortizacao)
                    .valorJuros(juros)
                    .valorPrestacao(prestacao)
                    .status(StatusParcela.PENDENTE)
                    .saldoDevedor(saldo)
                    .build());
        }
        return parcelas;
    }
}
