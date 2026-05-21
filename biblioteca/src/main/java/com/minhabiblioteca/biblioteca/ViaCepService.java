package com.minhabiblioteca.biblioteca;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.util.Map;

@Service
public class ViaCepService {

    private String baseUrl = "https://viacep.com.br";

    public ViaCepService() {}

    public ViaCepService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map<String, String> buscarCep(String cep) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/ws/" + cep + "/json/";
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao buscar CEP: " + cep, e);
        }
    }
}