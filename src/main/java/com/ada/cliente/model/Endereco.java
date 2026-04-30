package com.ada.cliente.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco extends PanacheEntityBase {

    @Id
    public UUID id;

    public String cep;
    public String logradouro;
    public String bairro;
    public String numero;
    public String complemento;
}

