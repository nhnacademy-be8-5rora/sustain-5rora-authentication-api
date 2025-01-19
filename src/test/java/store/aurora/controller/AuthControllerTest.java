package store.aurora.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.util.JWTUtil;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTUtil jwtUtil;

    @Test
    void createJwt_ReturnsJwtToken() throws Exception {
        // Given
        String username = "testUser";
        Long expiredMs = 1000L * 60 * 10; // 10 minutes
        String expectedJwt = "dummyJwtToken";

        when(jwtUtil.createJwt(username, expiredMs)).thenReturn(expectedJwt);

        // When / Then
        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"expiredMs\":600000}"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJwt));
    }

    @Test
    void reissue_ReturnsNewJwtToken() throws Exception {
        // Given
        String userId = "testUserId";
        String expectedJwt = "newDummyJwtToken";

        when(jwtUtil.createJwt(userId, null)).thenReturn(expectedJwt);

        // When / Then
        mockMvc.perform(post("/api/auth/refresh")
                        .header("X-USER-ID", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJwt));
    }
}