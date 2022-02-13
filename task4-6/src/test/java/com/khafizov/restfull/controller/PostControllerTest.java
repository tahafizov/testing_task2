package com.khafizov.restfull.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void correct_post_method() throws Exception {
        mockMvc.perform(post("/cat")
                    .content("{\n" +
                        "  \"name\": \"Vasya\",\n" +
                        "  \"color\": \"black & white\",\n" +
                        "  \"tailLength\": 10,\n" +
                        "  \"whiskersLength\": 12\n" +
                        "}")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void incorrect_post_dublicate_name() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"black & white\",\n" +
                                "  \"tailLength\": 10,\n" +
                                "  \"whiskersLength\": 12\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_null_values() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{ }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_negative_tail_length() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"black & white\",\n" +
                                "  \"tailLength\": -10,\n" +
                                "  \"whiskersLength\": 12\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_negative_whiskers_length() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"black & white\",\n" +
                                "  \"tailLength\": 10,\n" +
                                "  \"whiskersLength\": -12\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_large_tail_length() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"black & white\",\n" +
                                "  \"tailLength\": 100,\n" +
                                "  \"whiskersLength\": 12\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_large_whiskers_length() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"black & white\",\n" +
                                "  \"tailLength\": 10,\n" +
                                "  \"whiskersLength\": 120\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void incorrect_post_color() throws Exception {
        mockMvc.perform(post("/cat")
                        .content("{\n" +
                                "  \"name\": \"Vasya\",\n" +
                                "  \"color\": \"yellow\",\n" +
                                "  \"tailLength\": 10,\n" +
                                "  \"whiskersLength\": 10\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
