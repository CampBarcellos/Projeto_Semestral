package com.minhabiblioteca.biblioteca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Agora é um repositório JPA para MySQL
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Podemos adicionar consultas personalizadas depois, se precisar
    // Exemplo: List<Livro> findByAutor(String autor);
}