package mate.academy.app.service;

import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.dto.category.CreateCategoryRequestDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.mapper.CategoryMapper;
import mate.academy.app.model.Category;
import mate.academy.app.repository.CategoryRepository;
import mate.academy.app.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("""
            
            """)
    public void findAll_ValidRequest_ShouldReturnListOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);

        Category category = new Category();
        category.setId(1L);
        category.setName("Test");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        List<Category> categories = List.of(category);
        Page<Category> bookPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(bookPage);
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actualPage = categoryService.findAll(pageable);
        assertEquals(1, actualPage.getContent().size());
        assertEquals(categoryDto, actualPage.getContent().get(0));
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("""
            
            """)
    public void getById_ValidId_ShouldReturnCategoryDto() {
        Long requestId = 1L;
        Category expectedCategory = new Category();
        expectedCategory.setId(requestId);
        expectedCategory.setName("Test");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(expectedCategory.getId());
        expectedDto.setName(expectedCategory.getName());

        when(categoryRepository.findById(requestId)).thenReturn(Optional.of(expectedCategory));
        when(categoryMapper.toCategoryDto(expectedCategory)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.getById(requestId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("""
            
            """)
    public void getById_InvalidId_ShouldReturnNull() {
        Long requestId = 1L;

        when(categoryRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(requestId));

        verify(categoryRepository, times(1)).findById(requestId);
    }

    @Test
    @DisplayName("""
    
            """)
    public void deleteById_ValidId_ShouldDeleteCategory() {
        Long id = 1L;
        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("""
            
            """)
    public void update_ValidId_ShouldUpdateCategory() {
        Long id = 1L;

        Category expectedCategory = new Category();
        expectedCategory.setId(id);
        expectedCategory.setName("Test");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(expectedCategory.getId());
        categoryDto.setName(expectedCategory.getName());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(expectedCategory));
        when(categoryMapper.toCategoryDto(expectedCategory)).thenReturn(categoryDto);

        CategoryDto actualCategory = categoryService.getById(id);

        assertEquals(actualCategory, categoryDto);
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("""
            
            """)
    public void save_ValidCategoryDto_ShouldSaveCategory() {
        CreateCategoryRequestDto createCategoryDto = new CreateCategoryRequestDto();
        createCategoryDto.setName("Test");

        Category category = new Category();
        category.setName(createCategoryDto.getName());

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(createCategoryDto.getName());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(savedCategory.getId());
        categoryDto.setName(savedCategory.getName());

        when(categoryMapper.toCategory(createCategoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toCategoryDto(savedCategory)).thenReturn(categoryDto);

        CategoryDto actualDto = categoryService.save(createCategoryDto);
        assertEquals(categoryDto, actualDto);
    }
}
