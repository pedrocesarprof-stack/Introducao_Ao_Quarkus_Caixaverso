package com.ada.cliente.service;

import com.ada.cliente.client.ViaCepClient;
import com.ada.cliente.dto.ClienteRequest;
import com.ada.cliente.dto.ClienteResponse;
import com.ada.cliente.dto.ViaCepResponse;
import com.ada.cliente.model.Cliente;
import com.ada.cliente.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ClienteService {

    @RestClient
    ViaCepClient viaCepClient;

    public List<ClienteResponse> listarTodos() {
        return Cliente.<Cliente>listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) {
        boolean documentoJaExiste = Cliente.count("documento", request.getDocumento()) > 0;
        if (documentoJaExiste) {
            throw new BadRequestException("Já existe um cliente cadastrado com o documento informado.");
        }

        ViaCepResponse enderecoVia = viaCepClient.buscarEnderecoPorCep(request.getCep());

        if (enderecoVia == null || "true".equals(enderecoVia.getErro())) {
            throw new BadRequestException("CEP não encontrado ou inválido.");
        }

        String complemento = (request.getComplemento() != null && !request.getComplemento().isBlank())
                ? request.getComplemento()
                : "N/A";

        Endereco endereco = Endereco.builder()
                .id(UUID.randomUUID())
                .cep(request.getCep())
                .logradouro(enderecoVia.getLogradouro())
                .bairro(enderecoVia.getBairro())
                .numero(request.getNumero())
                .complemento(complemento)
                .build();

        Cliente cliente = Cliente.builder()
                .id(UUID.randomUUID())
                .nome(request.getNome())
                .documento(request.getDocumento())
                .endereco(endereco)
                .build();

        Cliente.persist(cliente);
        return toResponse(cliente);
    }

    public ClienteResponse buscarPorDocumento(String documento) {
        Cliente cliente = Cliente.find("documento", documento).<Cliente>firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        return toResponse(cliente);
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.id)
                .nome(cliente.nome)
                .documento(cliente.documento)
                .cep(cliente.endereco.cep)
                .logradouro(cliente.endereco.logradouro)
                .bairro(cliente.endereco.bairro)
                .numero(cliente.endereco.numero)
                .complemento(cliente.endereco.complemento)
                .build();
    }
}
