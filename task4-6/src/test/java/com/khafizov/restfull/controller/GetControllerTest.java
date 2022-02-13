package com.khafizov.restfull.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void correct_get_method() throws Exception {
        mockMvc.perform(get("/cats"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void correct_get_offset_limit() throws Exception {
        mockMvc.perform(get("/cats?offset=1&limit=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void correct_get_attributes_sortes_offset_limit() throws Exception {
        mockMvc.perform(get("/cats?attribute=name&sort=asc&offset=2&limit=5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    void incorrect_get_attribute_name() throws Exception {
        mockMvc.perform(get("/cats?attribute=sfdsf&sort=asc"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_sort_type() throws Exception {
        mockMvc.perform(get("/cats?attribute=name&sort=qwer"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_attribute_without_sort() throws Exception {
        mockMvc.perform(get("/cats?attribute=name"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_offset_type_string() throws Exception {
        mockMvc.perform(get("/cats?offset=jfgjh"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_offset_is_negative_number() throws Exception {
        mockMvc.perform(get("/cats?offset=-5"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_limit_type_string() throws Exception {
        mockMvc.perform(get("/cats?limit=jfgjh"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_limit_is_negative_number() throws Exception {
        mockMvc.perform(get("/cats?limit=-5"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_get_offset_without_limit() throws Exception {
        mockMvc.perform(get("/cats?offset=5"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
