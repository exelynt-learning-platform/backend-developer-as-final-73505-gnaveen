package com.example.booking.security;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;


    // =========================================================
    // UNAUTHENTICATED USER
    // =========================================================

    @Test
    void unauthenticatedUserCannotAccessReservations()
            throws Exception {

        mockMvc.perform(
                get("/api/reservations")
        )
        .andExpect(status().isUnauthorized());
    }


    // =========================================================
    // USER ROLE
    // =========================================================

    @Test
    void userCanReadResources()
            throws Exception {

        mockMvc.perform(
                get("/api/resources")
                        .with(user("user").roles("USER"))
        )
        .andExpect(status().isOk());
    }


    @Test
    void userCannotCreateResource()
            throws Exception {

        mockMvc.perform(
                post("/api/resources")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
        .andExpect(status().isForbidden());
    }


    // =========================================================
    // ADMIN ROLE
    // =========================================================

    @Test
    void adminCanReadResources()
            throws Exception {

        mockMvc.perform(
                get("/api/resources")
                        .with(user("admin").roles("ADMIN"))
        )
        .andExpect(status().isOk());
    }
}