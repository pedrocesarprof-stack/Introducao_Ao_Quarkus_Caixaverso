package com.ada.emprestimo.repository;
import com.ada.emprestimo.model.Emprestimo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class EmprestimoRepository implements PanacheRepositoryBase<Emprestimo, UUID> {
}
