package com.healthcare.api.controller;

import com.healthcare.api.service.ProntuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProntuarioControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProntuarioService prontuarioService;

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaNaoDeveAcessarProntuarios() throws Exception {
        mockMvc.perform(get("/prontuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveAcessarProntuarios() throws Exception {
        org.mockito.Mockito.when(prontuarioService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/prontuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoDeveAcessarProntuarios() throws Exception {
        org.mockito.Mockito.when(prontuarioService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/prontuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoDeveAcessarProntuarios() throws Exception {
        mockMvc.perform(get("/prontuarios"))
                .andExpect(status().isForbidden());
    }
}