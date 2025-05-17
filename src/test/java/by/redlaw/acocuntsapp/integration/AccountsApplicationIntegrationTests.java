package by.redlaw.acocuntsapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountsApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldFindUserByPhone() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("phone", "79201111111"))

                .andExpect(status().isOk())
                .andExpect(content().string(containsString("79201111111")));
    }

    @Test
    void shouldFindUserByDateOfBirth() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("dateOfBirth", "1990-01-01T00:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindUserByName() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("name", "Ivan"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ivan Petrov")));
    }

    @Test
    void shouldFindUserByEmail() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("email", "ivan.petrov@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ivan.petrov@example.com")));
    }

    @Test
    void shouldReturnAllUsersWithoutFilters() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(10)))
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    void shouldReturnEmptyResultWhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/user/search")
                        .param("name", "NotExists")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(0)));
    }
}
