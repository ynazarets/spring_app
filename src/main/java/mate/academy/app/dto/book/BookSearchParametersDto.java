package mate.academy.app.dto.book;

public record BookSearchParametersDto(String[] title,
                                      String[] author,
                                      String[] isbn) {

}
