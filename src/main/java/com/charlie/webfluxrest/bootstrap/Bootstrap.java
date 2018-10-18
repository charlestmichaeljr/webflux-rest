package com.charlie.webfluxrest.bootstrap;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.domain.Vendor;
import com.charlie.webfluxrest.repositories.CategoryRepository;
import com.charlie.webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if(categoryRepository.count().block() == 0) {
            loadCategories();
            loadVendors();
        }
        else {
            System.out.println("Bootstrap data already exists. No data created.");
        }

    }

    private void loadCategories() {
        categoryRepository.save(Category.builder().description("Dried Fruit").build()).block();
        categoryRepository.save(Category.builder().description("Nuts").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();
        categoryRepository.save(Category.builder().description("Breads").build()).block();
        categoryRepository.save(Category.builder().description("Eggs").build()).block();

        System.out.println("Created " + categoryRepository.count().block() + " Categories");
    }
    private void loadVendors() {
        vendorRepository.save(Vendor.builder().firstName("Charlie").lastName("Michael").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Clinton").build()).block();

        System.out.println("Created "+ vendorRepository.count().block() +" vendors");
    }
}
