package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.domain.Vendor;
import com.charlie.webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.ws.WebServiceException;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createNewVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();

        if (vendor.getFirstName() != null && (foundVendor.getFirstName() != vendor.getFirstName())) {
            foundVendor.setFirstName(vendor.getFirstName());
            vendorRepository.save(foundVendor);
        }

        if (vendor.getLastName() != null && (foundVendor.getLastName() != vendor.getLastName())) {
            foundVendor.setLastName(vendor.getLastName());
            vendorRepository.save(foundVendor);
        }

        return Mono.just(foundVendor);
    }


}
