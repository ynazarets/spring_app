package mate.academy.app.service;

import mate.academy.app.model.Role;
import java.util.Optional;

public interface RoleService {

    Optional<Role> findRoleByName(String name);
}
