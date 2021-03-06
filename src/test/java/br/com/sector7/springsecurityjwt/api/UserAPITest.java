package br.com.sector7.springsecurityjwt.api;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertNotNull;

/**
 * ---------------------------------
 * Teste unitário de API de usuários
 * ---------------------------------
 * @author bruno.carneiro
 * @since 04/2019
 */
public class UserAPITest {

    private UserAPI userAPI;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.userAPI = new UserAPI(userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userAPI).build();
        this.passwordEncoder = new BCryptPasswordEncoder();

        // singleton mapper
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
        }
    }

    /**
     * ------------------------------------
     * Teste de cadastramento de usuário OK
     * ------------------------------------
     */
    @Test
    public void save_200() throws Exception {

        // given
        ResponseEntity<User> responseEntity;
        User user = new User(
                "admin@sector7.com",
                passwordEncoder.encode("123456"),
                null,
                true);
        String jsonRequestBody = mapper.writeValueAsString(user);

        // when
        Mockito.when(userService.cadastrar(Mockito.any(User.class)))
                .thenReturn(user);

        responseEntity = userAPI.save(new User());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        assertNotNull(responseEntity);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().json(jsonRequestBody));

    }

    /**
     * -----------------------------------------------
     * Testa retorno vazio durante cadastro de usuário
     * -----------------------------------------------
     */
    @Test
    public void save_204() throws Exception {

        // given
        ResponseEntity<User> responseEntity;
        User user = new User();
        String jsonRequestBody = mapper.writeValueAsString(user);

        // when
        Mockito.when(userService.cadastrar(Mockito.any(User.class)))
                .thenReturn(null);

        responseEntity = userAPI.save(new User());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        assertNotNull(responseEntity);
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    /**
     * ----------------------------------------------------
     * Testa erro durante requisição de cadastro de usuário
     * ----------------------------------------------------
     */
    @Test
    public void save_400() throws Exception {

        // given
        ResponseEntity<User> responseEntity;

        // when
        Mockito.when(userService.cadastrar(Mockito.any(User.class)))
                .thenReturn(null);

        responseEntity = userAPI.save(new User());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        assertNotNull(responseEntity);
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    /**
     * ------------------------------
     * Teste de remoção de usuário OK
     * ------------------------------
     */
    @Test
    public void remove_200() throws Exception {

        // given
        Long id = 2L;
        ResponseEntity<User> responseEntity = null;
        User user = new User();

        // when
        Mockito.when(userService.buscarPorId(id)).thenReturn(new User());
        Mockito.when(userService.deletar(Mockito.any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{userId}", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

        responseEntity = this.userAPI.remove(id);

        // then
        assertNotNull(responseEntity.getBody());
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    }

    /**
     * ------------------------------------------------
     * Teste de usuário não encontrado (204) ao remover
     * ------------------------------------------------
     */
    @Test
    public void remove_204() throws Exception {

        // given
        Long id = 2L;

        // when
        Mockito.when(userService.buscarPorId(id)).thenReturn(new User());
        Mockito.when(userService.deletar(Mockito.any(User.class))).thenReturn(null); // Espera NO_CONTENT (204)

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{userId}", Mockito.anyLong())
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    /**
     * --------------------------------
     * Testando erro ao remover usuário
     * --------------------------------
     */
    @Test
    public void remove_400() throws Exception {

        // given
        Long id = 2L;
        User user = new User();

        // when
        Mockito.when(userService.buscarPorId(id)).thenReturn(new User());
        Mockito.when(userService.deletar(Mockito.any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{userId}", "dois") // Passando parâmetro string ao invés de long
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    /**
     * --------------------------------
     * Teste de alteração de usuário OK
     * --------------------------------
     */
    @Test
    public void update_200() throws Exception {

        // given
        User user = new User();
        user.setId(2L);
        user.setEmail("updated@sector7.com");
        user.setPassword("update");
        user.setEnabled(true);
        user.setAuthorities(null);

        String jsonRequestBody = mapper.writeValueAsString(user);

        // when
        Mockito.when(this.userService.alterar(Mockito.any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * ---------------------------------------
     * Testa erro durante alteração de usuário
     * ---------------------------------------
     */
    @Test
    public void update_400() throws Exception {

        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("errorupdate@sector7.com");
        user.setPassword("<secret>");
        user.setEnabled(true);

        // when
        Mockito.when(this.userService.alterar(Mockito.any(User.class))).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    /**
     * -------------------------------
     * Testando obtenção de usuário OK
     * -------------------------------
     */
    @Test
    public void findById_200() throws Exception {

        // given
        Long id = 1L;
        User user = new User();
        user.setId(id);

        // when
        Mockito.when(this.userService.buscarPorId(Mockito.anyLong())).thenReturn(user);

        ResponseEntity<User> responseUser = this.userAPI.findById(1L);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{userId}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        Assert.assertNotNull((responseUser));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findById_204() throws Exception {

        // given
        Long id = Mockito.anyLong();

        // when
        Mockito.when(this.userService.buscarPorId(id)).thenReturn(null); // Espera NO_CONTENT (204)

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{userId}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * --------------------------------
     * Testando listagem de usuários OK
     * --------------------------------
     */
    @Test
    public void findAll() {

    }
}