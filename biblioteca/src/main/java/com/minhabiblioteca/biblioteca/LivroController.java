package com.minhabiblioteca.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    // Adicionar um livro
    @PostMapping
    public Livro adicionarLivro(@RequestBody Livro livro) {
        return livroService.adicionarLivro(livro);
    }

    // Listar todos os livros
    @GetMapping
    public List<Livro> listarLivros() {
        return livroService.listarLivros();
    }

    // Buscar um livro pelo ID
    @GetMapping("/{id}")
    public Optional<Livro> buscarLivro(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    // Atualizar um livro
    @PutMapping("/{id}")
    public Livro atualizarLivro(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
        // Define o ID do livro atualizado para garantir que seja o mesmo
        livroAtualizado.setId(id);
        return livroService.atualizarLivro(livroAtualizado);
    }

    // Deletar um livro
    @DeleteMapping("/{id}")
    public void deletarLivro(@PathVariable Long id) {
        livroService.deletarLivro(id);
    }
}