package org.example.deliveryservice.configuration;

import org.example.deliveryservice.entity.auth.AuthRole;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.repository.AuthRoleRepository;
import org.example.deliveryservice.repository.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;
    private final AuthRoleRepository authRoleRepository;

    public CustomUserDetailsService(AuthUserRepository authUserRepository, AuthRoleRepository authRoleRepository) {
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("bad credentials"));
        Set<AuthRole> authRoles = authRoleRepository.findAllById(authUser.getId());

        authUser.setRoles(authRoles);

        return new CustomUserDetails(authUser);

    }
}
