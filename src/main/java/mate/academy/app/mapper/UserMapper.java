package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.user.UserDto;
import mate.academy.app.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserDto toUserDto(User user);

}
