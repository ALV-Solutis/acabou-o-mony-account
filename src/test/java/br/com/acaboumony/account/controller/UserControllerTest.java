package br.com.acaboumony.account.controller;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<UserReqDTO> userReqDTOJson;
    @Autowired
    private JacksonTester<UserUpdateDTO> userUpdateDTOJson;

    @Test
    @DisplayName("Devolver HTTP 201 quando um usuário for criado.")
    void createUser() throws Exception {

        var response = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userReqDTOJson.write(
                                new UserReqDTO("Leonardo Macedo", "54784328943",
                                        "leonardo123", "61995568949", "testando@gmail.com")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Devolver HTTP 500 quando requisição vier sem algum campo")
    void createUserFail() throws Exception {

        var response = mockMvc.perform(post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void notAuthorizedLogin() throws Exception {

        var response = mockMvc.perform(post("/users/login")
                .header("email", "testando@gmail.com")
                .header("password", "leonardo12"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void authorizedLogin() throws Exception {

        var response = mockMvc.perform(post("/users/login")
                        .header("email", "testando@gmail.com")
                        .header("password", "leonardo123"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Devolver HTTP 200 com os usuários")
    @WithMockUser
    void listUsers() throws Exception {

        var response = mockMvc.perform(get("/users")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Devolver HTTP 403 quando o usuário não estiver autorizado")
    void notAuthorizedRequest() throws Exception {

        var response = mockMvc.perform(get("/users/details")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Devolver HTTP 200 quando o usuário for atualizado")
    @WithMockUser
    void updateUser() throws Exception{

        var response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userUpdateDTOJson.write(
                        new UserUpdateDTO(UUID.fromString("2e4f8ddd-c741-48ca-9997-f5e3a4acb902"), "Teste update",
                                null, null)
                ).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}