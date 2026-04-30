package com.ada.emprestimo.service;

import com.ada.emprestimo.dto.EmprestimoRequest;
import com.ada.emprestimo.dto.EmprestimoResponse;
import com.ada.emprestimo.dto.ParcelaResponse;
import com.ada.emprestimo.model.Emprestimo;
import com.ada.emprestimo.model.Parcela;
import com.ada.emprestimo.model.StatusContrato;
import com.ada.emprestimo.model.StatusParcela;
import com.ada.emprestimo.repository.EmprestimoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EmprestimoService {

    @Inject
    EmprestimoRepository emprestimoRepository;

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    @Transactional
    public EmprestimoResponse contratar(EmprestimoRequest request, Integer taxaJurosMensal) {
        List<Parcela> parcelas = calcularParcelas(request, taxaJurosMensal);

        Emprestimo emprestimo = Emprestimo.builder()
                .id(UUID.randomUUID())
                .clienteId(request.getClienteId())
                .valorTotal(request.getValorTotal())
                .quantidadeParcelas(request.getQuantidadeParcelas())
                .tipoAmortizacao(request.getTipoAmortizacao())
                .status(StatusContrato.PENDENTE)
                .taxaJurosMensal(taxaJurosMensal)
                .parcelas(parcelas)
                .build();

        // Link parcelas back to emprestimo
        parcelas.forEach(p -> p.setEmprestimo(emprestimo));

        emprestimoRepository.persist(emprestimo);
        return toResponse(emprestimo);
    }

    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancelar(UUID id) {
        Emprestimo emprestimo = emprestimoRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));
        emprestimoRepository.delete(emprestimo);
    }

    private List<Parcela> calcularParcelas(EmprestimoRequest request, Integer taxaJurosMensal) {
        return switch (request.getTipoAmortizacao()) {
            case SAC -> calcularSAC(request, taxaJurosMensal);
            case PRICE -> calcularPRICE(request, taxaJurosMensal);
        };
    }

    public List<ParcelaResponse> calcularParcelasResponse(EmprestimoRequest request, Integer taxaJurosMensal) {
        return calcularParcelas(request, taxaJurosMensal).stream()
                .map(this::toParcelaResponse)
                .toList();
    }

    private List<Parcela> calcularSAC(EmprestimoRequest request, Integer taxaJurosMensal) {
        List<Parcela> parcelas = new ArrayList<>();
        int n = request.getQuantidadeParcelas();
        BigDecimal taxa = BigDecimal.valueOf(taxaJurosMensal)
                .divide(BigDecimal.valueOf(100), 10, ROUNDING);
        BigDecimal amortizacao = request.getValorTotal()
                .divide(BigDecimal.valueOf(n), SCALE, ROUNDING);
        BigDecimal saldo = request.getValorTotal();

        for (int i = 1; i <= n; i++) {
            if (i == n) amortizacao = saldo.setScale(SCALE, ROUNDING);
            BigDecimal juros = saldo.multiply(taxa).setScale(SCALE, ROUNDING);
            BigDecimal prestacao = amortizacao.add(juros).setScale(SCALE, ROUNDING);
            saldo = saldo.subtract(amortizacao).setScale(SCALE, ROUNDING);

            parcelas.add(Parcela.builder()
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

    private List<Parcela> calcularPRICE(EmprestimoRequest request, Integer taxaJurosMensal) {
        List<Parcela> parcelas = new ArrayList<>();
        int n = request.getQuantidadeParcelas();
        BigDecimal taxa = BigDecimal.valueOf(taxaJurosMensal)
                .divide(BigDecimal.valueOf(100), 10, ROUNDING);
        BigDecimal valorTotal = request.getValorTotal();

        BigDecimal fator = taxa.add(BigDecimal.ONE).pow(n, new MathContext(15, ROUNDING));
        BigDecimal prestacao = valorTotal
                .multiply(taxa.multiply(fator))
                .divide(fator.subtract(BigDecimal.ONE), SCALE, ROUNDING);

        BigDecimal saldo = valorTotal;

        for (int i = 1; i <= n; i++) {
            BigDecimal juros = saldo.multiply(taxa).setScale(SCALE, ROUNDING);
            BigDecimal amortizacao = prestacao.subtract(juros).setScale(SCALE, ROUNDING);

            if (i == n) {
                amortizacao = saldo.setScale(SCALE, ROUNDING);
                prestacao = amortizacao.add(juros).setScale(SCALE, ROUNDING);
            }
            saldo = saldo.subtract(amortizacao).setScale(SCALE, ROUNDING);

            parcelas.add(Parcela.builder()
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

    private EmprestimoResponse toResponse(Emprestimo emprestimo) {
        List<ParcelaResponse> parcelasResponse = emprestimo.getParcelas().stream()
                .map(this::toParcelaResponse)
                .toList();

        return EmprestimoResponse.builder()
                .id(emprestimo.getId())
                .clienteId(emprestimo.getClienteId())
                .valorTotal(emprestimo.getValorTotal())
                .quantidadeParcelas(emprestimo.getQuantidadeParcelas())
                .tipoAmortizacao(emprestimo.getTipoAmortizacao())
                .status(emprestimo.getStatus())
                .taxaJurosMensal(emprestimo.getTaxaJurosMensal())
                .parcelas(parcelasResponse)
                .build();
    }

    private ParcelaResponse toParcelaResponse(Parcela parcela) {
        return ParcelaResponse.builder()
                .id(parcela.getId())
                .ordem(parcela.getOrdem())
                .dataVencimento(parcela.getDataVencimento())
                .valorAmortizacao(parcela.getValorAmortizacao())
                .valorJuros(parcela.getValorJuros())
                .valorPrestacao(parcela.getValorPrestacao())
                .status(parcela.getStatus())
                .saldoDevedor(parcela.getSaldoDevedor())
                .build();
    }
}
