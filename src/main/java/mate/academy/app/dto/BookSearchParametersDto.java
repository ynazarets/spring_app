package mate.academy.app.dto;

public record BookSearchParametersDto(String[] title,
                                      String[] author,
                                      String[] isbn) {

}
