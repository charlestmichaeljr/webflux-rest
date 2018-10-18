package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.net.www.http.HttpCapture;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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

    @Test
    public void testCreateCategory() throws Exception {

        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("stuff").build());

        webTestClient.post()
                .uri(CATEGORIES_URL)
                .body(categoryMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void testUpdateCategory() throws Exception {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("other stuff").build());

        webTestClient.put()
                .uri(CATEGORIES_URL + "/1")
                .body(categoryMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchCategory() throws Exception {

        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Cool Stuff").id("1234").build()));
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Hot Foods").build());

        webTestClient.patch()
                .uri(CATEGORIES_URL + "/1")
                .body(catToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository).save(any());
    }

    @Test
    public void testPatchCategoryNoDescription() throws Exception {

        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Cool Stuff").id("1234").build()));
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri(CATEGORIES_URL + "/1")
                .body(catToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository,never()).save(any());
    }
}