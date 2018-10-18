package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.domain.Vendor;
import com.charlie.webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class VendorControllerTest {

    VendorRepository vendorRepository;
    VendorController vendorController;
    WebTestClient webTestClient;

    private final String VENDOR_URL = "api/v1/vendors";

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void listVendors() {

        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Bozo").lastName("TheClown").build(),
                        Vendor.builder().firstName("Krusty").lastName("TheClown").build(),
                        Vendor.builder().firstName("Homey").lastName("TheClown").build()));

        webTestClient.get()
                .uri(VENDOR_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(3);
    }

    @Test
    public void getVendorById() {

        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Bill").lastName("Clinton").build()));

        webTestClient.get()
                .uri(VENDOR_URL + "/1")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void testCreateVendor() throws Exception {

        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Glenn").lastName("Campbell").build());

        webTestClient.post()
                .uri(VENDOR_URL)
                .body(vendorMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void testUpdateVendor() throws Exception {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Anthony").lastName("Scaramucci").build());

        webTestClient.put()
                .uri(VENDOR_URL + "/1")
                .body(vendorMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}