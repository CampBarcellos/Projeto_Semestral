package com.minhabiblioteca.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LivroServiceTest {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void limparBanco() {
        livroRepository.deleteAll();
    }

    @Test
    void deveAdicionarEListarLivros() {
        Livro livro = new Livro("Dom Casmurro", "Machado de Assis", 1899, 256);
        livroService.adicionarLivro(livro);

        List<Livro> livros = livroService.listarLivros();

        assertFalse(livros.isEmpty(), "A lista não deve estar vazia");
        assertEquals("Dom Casmurro", livros.get(0).getTitulo());
        assertEquals("Machado de Assis", livros.get(0).getAutor());
    }

    @Test
    void deveBuscarLivroPorId() {
        Livro livro = new Livro("O Cortiço", "Aluísio Azevedo", 1890, 312);
        Livro salvo = livroService.adicionarLivro(livro);

        Optional<Livro> encontrado = livroService.buscarPorId(salvo.getId());

        assertTrue(encontrado.isPresent(), "Livro deve ser encontrado");
        assertEquals("O Cortiço", encontrado.get().getTitulo());
    }

    @Test
    void deveAtualizarLivro() {
        Livro livro = new Livro("Título Antigo", "Autor X", 2000, 100);
        Livro salvo = livroService.adicionarLivro(livro);

        salvo.setTitulo("Título Atualizado");
        salvo.setPaginas(200);
        livroService.atualizarLivro(salvo);

        Optional<Livro> atualizado = livroService.buscarPorId(salvo.getId());
        assertTrue(atualizado.isPresent());
        assertEquals("Título Atualizado", atualizado.get().getTitulo());
        assertEquals(200, atualizado.get().getPaginas());
    }

    @Test
    void deveDeletarLivro() {
        Livro livro = new Livro("Livro para Deletar", "Autor Y", 2020, 150);
        Livro salvo = livroService.adicionarLivro(livro);

        livroService.deletarLivro(salvo.getId());

        Optional<Livro> deletado = livroService.buscarPorId(salvo.getId());
        assertFalse(deletado.isPresent(), "Livro deletado não deve ser encontrado");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaLivros() {
        List<Livro> livros = livroService.listarLivros();
        assertTrue(livros.isEmpty(), "Lista deve estar vazia");
    }

    @ParameterizedTest
    @CsvSource({
        "Dom Casmurro,      Machado de Assis, 1899, 256",
        "O Alquimista,      Paulo Coelho,     1988, 208",
        "1984,              George Orwell,    1949, 328",
        "A Metamorfose,     Franz Kafka,      1915, 96"
    })
    void deveAdicionarLivroParametrizado(String titulo, String autor, int ano, int paginas) {
        Livro livro = new Livro(titulo.trim(), autor.trim(), ano, paginas);
        Livro salvo = livroService.adicionarLivro(livro);

        assertNotNull(salvo.getId(), "ID não deve ser nulo");
        assertEquals(titulo.trim(), salvo.getTitulo());
        assertEquals(autor.trim(), salvo.getAutor());
        assertEquals(ano, salvo.getAno());
        assertEquals(paginas, salvo.getPaginas());
    }
}