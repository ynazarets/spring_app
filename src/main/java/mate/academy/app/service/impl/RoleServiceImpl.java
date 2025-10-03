package mate.academy.app.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.app.model.Role;
import mate.academy.app.model.enums.RoleName;
import mate.academy.app.repository.RoleRepository;
import mate.academy.app.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findRoleByName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
