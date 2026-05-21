package com.minhabiblioteca.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        usuarioRepository.deleteAll();
    }

    @Test
    void deveAdicionarUsuarioViaController() throws Exception {
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Maria Silva\",\"email\":\"maria@email.com\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void deveListarUsuariosViaController() throws Exception {
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Souza\",\"email\":\"joao@email.com\",\"senha\":\"senha456\"}"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveDeletarUsuarioViaController() throws Exception {
        String resposta = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Para Deletar\",\"email\":\"deletar@email.com\",\"senha\":\"senha\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String idStr = resposta.replaceAll(".*\"id\":(\\d+).*", "$1");
        Long id = Long.parseLong(idStr);

        mockMvc.perform(delete("/usuarios/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarUsuarioViaController() throws Exception {
        String resposta = mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nome Original\",\"email\":\"original@email.com\",\"senha\":\"senha\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String idStr = resposta.replaceAll(".*\"id\":(\\d+).*", "$1");
        Long id = Long.parseLong(idStr);

        mockMvc.perform(put("/usuarios/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nome Atualizado\",\"email\":\"atualizado@email.com\",\"senha\":\"senha\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }
}