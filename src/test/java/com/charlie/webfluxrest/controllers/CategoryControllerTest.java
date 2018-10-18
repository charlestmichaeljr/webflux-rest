package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.net.www.http.HttpCapture;

import java.net.URL;

import static org.mockito.ArgumentMatchers.anyString;

public class CategoryControllerTest {

    CategoryRepository categoryRepository;
    CategoryController categoryController;
    WebTestClient webTestClient;

    private static final String CATEGORIES_URL = "/api/v1/categories";

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void listAllCategories() throws Exception {
        // given
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));
        // when
        webTestClient.get()
                .uri(CATEGORIES_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
        // then
    }

    @Test
    public void getCategoryById() {

        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Peanuts").build()));

        webTestClient.get()
                .uri(CATEGORIES_URL + "/1")
                .exchange()
                .expectBody(Category.class);

    }
}