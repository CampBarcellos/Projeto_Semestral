package com.minhabiblioteca.biblioteca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositório JPA para Usuários
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Podemos adicionar métodos extras depois se precisarmos
    // Exemplo: Usuario findByEmail(String email);
}