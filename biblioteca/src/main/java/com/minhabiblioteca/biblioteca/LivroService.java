package com.minhabiblioteca.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    // Adicionar um livro
    public Livro adicionarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    // Listar todos os livros
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    // Buscar um livro pelo ID
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    // Atualizar um livro
    public Livro atualizarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    // Deletar um livro pelo ID
    public void deletarLivro(Long id) {
        livroRepository.deleteById(id);
    }
}