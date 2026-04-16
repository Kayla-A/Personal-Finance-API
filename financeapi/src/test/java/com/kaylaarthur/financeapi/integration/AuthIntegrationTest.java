package com.kaylaarthur.financeapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLoginSuccessfully() throws Exception {        
        // login
        mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "email": "testing3@gmail.com",
                        "password": "testPass3!"
                    }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.name").value("Testing3"))
            .andExpect(jsonPath("$.email").value("testing3@gmail.com"));
    } // shouldLoginSuccessfully

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {        
        // login: should fail here
        mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "email": "testing4@gmail.com",
                        "password": "testPass4"
                    }
            """))
            .andExpect(status().isBadRequest());
    } // shouldFailLoginWithWrongPassword

} // AuthIntegrationTest
