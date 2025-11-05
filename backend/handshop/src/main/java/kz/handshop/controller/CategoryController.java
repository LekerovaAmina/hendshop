package kz.handshop.controller;

import kz.handshop.dto.response.CategoryResponse;
import kz.handshop.entity.GlobalCategory;
import kz.handshop.repository.GlobalCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private GlobalCategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<GlobalCategory> categories = categoryRepository.findByIsActiveTrue();

        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    private CategoryResponse convertToResponse(GlobalCategory category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setIconUrl(category.getIconUrl());
        response.setIsActive(category.getIsActive());
        return response;
    }
}