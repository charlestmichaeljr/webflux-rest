package com.charlie.webfluxrest.controllers;

import com.charlie.webfluxrest.domain.Category;
import com.charlie.webfluxrest.domain.Vendor;
import com.charlie.webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.misc.Request;

import javax.xml.ws.WebServiceException;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"", "/"})
    public Flux<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> getCategoryById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createNewCategory(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category) {
        Category foundCategory = categoryRepository.findById(id).block();

        if (category.getDescription() != null && (foundCategory.getDescription() != category.getDescription())) {
            foundCategory.setDescription(category.getDescription());
            categoryRepository.save(foundCategory);
        }

        return Mono.just(foundCategory);
    }
}
