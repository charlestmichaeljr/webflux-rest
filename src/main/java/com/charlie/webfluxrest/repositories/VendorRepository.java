package com.charlie.webfluxrest.repositories;

import com.charlie.webfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor,String> {
}
