package mate.academy.app.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.dto.category.CreateCategoryRequestDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.mapper.CategoryMapper;
import mate.academy.app.model.Category;
import mate.academy.app.repository.CategoryRepository;
import mate.academy.app.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toCategoryDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can not find category by id: " + id)));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper
                .toCategoryDto(categoryRepository
                        .save(categoryMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not find category by id: " + id));
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
