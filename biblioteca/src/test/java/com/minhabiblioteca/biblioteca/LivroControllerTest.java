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
public class LivroControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        livroRepository.deleteAll();
    }

    @Test
    void deveAdicionarLivroViaController() throws Exception {
        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Dom Casmurro\",\"autor\":\"Machado de Assis\",\"ano\":1899,\"paginas\":256}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$.autor").value("Machado de Assis"));
    }

    @Test
    void deveListarLivrosViaController() throws Exception {
        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"O Alquimista\",\"autor\":\"Paulo Coelho\",\"ano\":1988,\"paginas\":208}"));

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveDeletarLivroViaController() throws Exception {
        String resposta = mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Livro para Deletar\",\"autor\":\"Autor Y\",\"ano\":2020,\"paginas\":150}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String idStr = resposta.replaceAll(".*\"id\":(\\d+).*", "$1");
        Long id = Long.parseLong(idStr);

        mockMvc.perform(delete("/livros/" + id))
                .andExpect(status().isOk());
    }
}