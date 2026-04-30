package com.ada.cliente.model;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends PanacheEntityBase {
    @Id
    public UUID id;
    public String nome;
    public String documento;
    @OneToOne(cascade = CascadeType.ALL)
    public Endereco endereco;
}
