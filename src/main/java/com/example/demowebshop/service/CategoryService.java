package com.example.demowebshop.service;

import com.example.demowebshop.entity.Category;
import com.example.demowebshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository  categoryRepository;

    public void create(Category category){
        categoryRepository.save(category);
    }

    public Category getById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("không tìm thấy thể loại"));
    }

    public void update(Long id, Category newData){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại"));
        category.setName(newData.getName());
        category.setDescription(newData.getDescription());
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

}
