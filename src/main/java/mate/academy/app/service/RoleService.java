package mate.academy.app.service;

import java.util.Optional;
import mate.academy.app.model.Role;
import mate.academy.app.model.enums.RoleName;

public interface RoleService {

    Optional<Role> findRoleByName(RoleName roleName);
}
