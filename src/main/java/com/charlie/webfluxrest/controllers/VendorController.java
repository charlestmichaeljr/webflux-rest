package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Vendor;
import com.charlie.webfluxrest.repositories.VendorRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @RequestMapping({"","/"})
    public Flux<Vendor> listVendors() {
        return vendorRepository.findAll();
    }

    @RequestMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }


}
