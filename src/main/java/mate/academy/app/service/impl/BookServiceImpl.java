package mate.academy.app.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.app.dto.book.BookSearchParametersDto;
import mate.academy.app.dto.book.CreateBookRequestDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.mapper.BookMapper;
import mate.academy.app.mapper.CategoryMapperHelper;
import mate.academy.app.model.Book;
import mate.academy.app.repository.BookRepository;
import mate.academy.app.repository.specification.BookSpecificationBuilder;
import mate.academy.app.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryMapperHelper categoryMapperHelper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        if (requestDto.getCategoryIds() != null) {
            book.setCategories(categoryMapperHelper
                    .mapCategoryIdsToCategory(requestDto.getCategoryIds()));
        }
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toBookDto(bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Book with id: " + id + " not Found.")));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Book with id " + id
                + " not found and can not be updated!"));
        bookMapper.updateBookFromDto(requestDto, book);
        if (requestDto.getCategoryIds() != null) {
            book.setCategories(categoryMapperHelper
                    .mapCategoryIdsToCategory(requestDto.getCategoryIds()));
        }
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable)
                .map(bookMapper::toBookDtoWithoutCategoryIds);
    }
}
