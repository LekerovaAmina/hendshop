package kz.handshop.service;

import kz.handshop.dto.request.CreateShelfRequest;
import kz.handshop.dto.response.ShelfResponse;
import kz.handshop.dto.response.CategoryResponse;
import kz.handshop.entity.FreelancerShelf;
import kz.handshop.entity.GlobalCategory;
import kz.handshop.entity.User;
import kz.handshop.exception.CategoryNotFoundException;
import kz.handshop.exception.ForbiddenException;
import kz.handshop.exception.ShelfNotFoundException;
import kz.handshop.repository.FreelancerShelfRepository;
import kz.handshop.repository.GlobalCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreelancerService {

    @Autowired
    private FreelancerShelfRepository shelfRepository;

    @Autowired
    private GlobalCategoryRepository categoryRepository;

    @Transactional
    public ShelfResponse createShelf(CreateShelfRequest request, User freelancer) {
        GlobalCategory category = categoryRepository.findById(request.getGlobalCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        FreelancerShelf shelf = new FreelancerShelf();
        shelf.setFreelancer(freelancer);
        shelf.setGlobalCategory(category);
        shelf.setShelfName(request.getShelfName());
        shelf.setDescription(request.getDescription());

        shelf = shelfRepository.save(shelf);

        return convertToShelfResponse(shelf);
    }

    public List<ShelfResponse> getFreelancerShelves(User freelancer) {
        List<FreelancerShelf> shelves = shelfRepository.findByFreelancer(freelancer);
        return shelves.stream()
                .map(this::convertToShelfResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteShelf(Long shelfId, User freelancer) {
        FreelancerShelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new ShelfNotFoundException("Полка не найдена"));

        if (!shelf.getFreelancer().getId().equals(freelancer.getId())) {
            throw new ForbiddenException("Вы не можете удалить чужую полку");
        }

        shelfRepository.delete(shelf);
    }

    private ShelfResponse convertToShelfResponse(FreelancerShelf shelf) {
        ShelfResponse response = new ShelfResponse();
        response.setId(shelf.getId());
        response.setShelfName(shelf.getShelfName());
        response.setDescription(shelf.getDescription());

        if (shelf.getGlobalCategory() != null) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(shelf.getGlobalCategory().getId());
            categoryResponse.setName(shelf.getGlobalCategory().getName());
            categoryResponse.setIconUrl(shelf.getGlobalCategory().getIconUrl());
            categoryResponse.setIsActive(shelf.getGlobalCategory().getIsActive());
            response.setGlobalCategory(categoryResponse);
        }

        return response;
    }
}