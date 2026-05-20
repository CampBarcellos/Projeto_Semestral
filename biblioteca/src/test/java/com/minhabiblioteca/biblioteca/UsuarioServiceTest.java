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
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        Usuario usuario = new Usuario("Maria Silva", "maria@email.com", "senha123");
        usuarioService.adicionarUsuario(usuario);

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertFalse(usuarios.isEmpty(), "A lista não deve estar vazia");
        assertEquals("Maria Silva", usuarios.get(0).getNome());
        assertEquals("maria@email.com", usuarios.get(0).getEmail());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario("João Souza", "joao@email.com", "senha456");
        Usuario salvo = usuarioService.adicionarUsuario(usuario);

        Optional<Usuario> encontrado = usuarioService.buscarPorId(salvo.getId());

        assertTrue(encontrado.isPresent(), "Usuário deve ser encontrado");
        assertEquals("João Souza", encontrado.get().getNome());
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario usuario = new Usuario("Nome Antigo", "antigo@email.com", "senha");
        Usuario salvo = usuarioService.adicionarUsuario(usuario);

        salvo.setNome("Nome Atualizado");
        salvo.setEmail("novo@email.com");
        usuarioService.atualizarUsuario(salvo);

        Optional<Usuario> atualizado = usuarioService.buscarPorId(salvo.getId());
        assertTrue(atualizado.isPresent());
        assertEquals("Nome Atualizado", atualizado.get().getNome());
        assertEquals("novo@email.com", atualizado.get().getEmail());
    }

    @Test
    void deveDeletarUsuario() {
        Usuario usuario = new Usuario("Para Deletar", "deletar@email.com", "senha");
        Usuario salvo = usuarioService.adicionarUsuario(usuario);

        usuarioService.deletarUsuario(salvo.getId());

        Optional<Usuario> deletado = usuarioService.buscarPorId(salvo.getId());
        assertFalse(deletado.isPresent(), "Usuário deletado não deve ser encontrado");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        assertTrue(usuarios.isEmpty(), "Lista deve estar vazia");
    }

    @ParameterizedTest
    @CsvSource({
        "Ana Lima,    ana@email.com,    senha111",
        "Bruno Dias,  bruno@email.com,  senha222",
        "Carla Melo,  carla@email.com,  senha333",
        "Diego Cruz,  diego@email.com,  senha444"
    })
    void deveCadastrarUsuarioParametrizado(String nome, String email, String senha) {
        Usuario usuario = new Usuario(nome.trim(), email.trim(), senha.trim());
        Usuario salvo = usuarioService.adicionarUsuario(usuario);

        assertNotNull(salvo.getId(), "ID não deve ser nulo");
        assertEquals(nome.trim(), salvo.getNome());
        assertEquals(email.trim(), salvo.getEmail());
    }
}