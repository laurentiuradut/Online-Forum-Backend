package com.laur.licenta.service;

import com.laur.licenta.dto.CategoryDto;
import com.laur.licenta.exceptions.IdNotFoundException;
import com.laur.licenta.models.Category;
import com.laur.licenta.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto CategoryToDto(Category category){
       /* CategoryDto dto=new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        return dto;

        */
        ModelMapper mapper=new ModelMapper();
        CategoryDto map= mapper.map(category,CategoryDto.class);
        return map;
    }

    public Category dtoToCategory(CategoryDto categoryDto){
        ModelMapper mapper=new ModelMapper();
        Category map=mapper.map(categoryDto,Category.class);
        return map;
    }

    public List<CategoryDto> getAll()
    {
        return categoryRepository.findAll().stream().map(this::CategoryToDto).collect(Collectors.toList());
    }

    public CategoryDto getByName(String name) {
        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> new IdNotFoundException("No category found with id - " + name));
            return CategoryToDto(category);
        }
        catch (IdNotFoundException exception1){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found",exception1);
        }
    }

    public CategoryDto save(CategoryDto categoryDto)
    {
        Category category=dtoToCategory(categoryDto);
        category=categoryRepository.save(category);
        return CategoryToDto(category);
    }




    /*

    public CategoryDto save (CategoryDto categoryDto) {
        Category save = categoryRepository.save(categoryMapper.mapDtoToCategory(categoryDto));
        return categoryDto;

    }
    public Category createCategory(Category category){
        category=categoryRepository.save(category);
        return category;
    }

    @Transactional


    public Category createCategory(Category category){
        category=categoryRepository.save(category);
        return category;
    }

    @Transactional
    public List<CategoryDto> getAll(){
        return categoryRepository.findAll().stream().map(categoryMapper::mapCategoryToDto).collect(Collectors.toList());
    }



    }

    private Category mapCategoryDto(CategoryDto categoryDto){
        return Category.builder().name(categoryDto.getName()).description(categoryDto.getDescription()).build();
    }

    private CategoryDto mapToDto(Category category){
        return CategoryDto.builder().name(category.getName()).id(category.getId()).numberOfPosts(category.getPosts().size()).build();
    }

   */
}
