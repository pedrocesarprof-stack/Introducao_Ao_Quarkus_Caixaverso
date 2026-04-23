package com.ada.cliente.service;

import com.ada.cliente.client.ViaCepClient;
import com.ada.cliente.dto.ClienteRequest;
import com.ada.cliente.dto.ClienteResponse;
import com.ada.cliente.dto.ViaCepResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ClienteService {

    @RestClient
    ViaCepClient viaCepClient;

    private static final List<ClienteResponse> clientes = new ArrayList<>();

    public List<ClienteResponse> listarTodos() {
        return clientes;
    }

    public ClienteResponse cadastrar(ClienteRequest request) {
        ViaCepResponse endereco = viaCepClient.buscarEnderecoPorCep(request.getCep());

        if (endereco == null || "true".equals(endereco.getErro())) {
            throw new BadRequestException("CEP não encontrado ou inválido.");
        }

        String complemento = (request.getComplemento() != null && !request.getComplemento().isBlank())
                ? request.getComplemento()
                : "N/A";

        ClienteResponse response = ClienteResponse.builder()
                .id(UUID.randomUUID())
                .nome(request.getNome())
                .documento(request.getDocumento())
                .cep(request.getCep())
                .logradouro(endereco.getLogradouro())
                .bairro(endereco.getBairro())
                .numero(request.getNumero())
                .complemento(complemento)
                .build();

        clientes.add(response);
        return response;
    }

    public ClienteResponse buscarPorDocumento(String documento) {
        return clientes.stream()
                .filter(c -> c.getDocumento().equals(documento))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
    }
}


