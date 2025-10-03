package mate.academy.app.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.app.model.Role;
import mate.academy.app.repository.RoleRepository;
import mate.academy.app.service.RoleService;

import java.util.Optional;

@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findByRoleName(name);
    }
}
