package org.example.deliveryservice.service.authUser;

import jakarta.persistence.EntityNotFoundException;
import org.example.deliveryservice.configuration.JwtTokenUtil;
import org.example.deliveryservice.dto.authUserDto.AuthUserResponseDTO;
import org.example.deliveryservice.dto.authUserDto.CreateAuthUserDTO;
import org.example.deliveryservice.dto.authUserDto.UpdateAuthUserDTO;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.exception.ResourceNotFoundException;
import org.example.deliveryservice.mapper.AuthUserMapper;
import org.example.deliveryservice.repository.AuthUserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper;
    private final JwtTokenUtil jwtToken;
    private final AuthUserRepository authUserRepository;

    public AuthUserServiceImpl(AuthUserRepository userRepository, PasswordEncoder passwordEncoder, AuthUserMapper authUserMapper, JwtTokenUtil jwtToken, AuthUserRepository authUserRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUserMapper = authUserMapper;
        this.jwtToken = jwtToken;
        this.authUserRepository = authUserRepository;
    }


    @Transactional
    public AuthUserResponseDTO registerUser(CreateAuthUserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        AuthUser user = new AuthUser();
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setName(userDTO.name());
        AuthUser saved = userRepository.save(user);
        return authUserMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public AuthUserResponseDTO createUser(CreateAuthUserDTO createAuthUserDTO) {
        AuthUser user = new AuthUser();
        AuthUser authUser = authUserMapper.partialCreateAuthUser(createAuthUserDTO, user);
        AuthUser saved = userRepository.save(authUser);
        return authUserMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public AuthUserResponseDTO getUserById(Long id) {
        AuthUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
        return authUserMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public List<AuthUserResponseDTO> getAllUsers() {
        List<AuthUser> all = userRepository.findAll();
        return authUserMapper.toResponseDTOList(all);
    }

    @Override
    @Transactional
    public AuthUserResponseDTO updateUser(Long id, UpdateAuthUserDTO updateAuthUserDTO) {
        AuthUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
        AuthUser authUser = authUserMapper.partialUpdateAuthUser(updateAuthUserDTO, user);
        return authUserMapper.toResponseDTO(authUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public String processUser(Map<String, String> userInfo) {
        String email = userInfo.get("email");


        Optional<AuthUser> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return login(existingUser.get());
        } else {
            AuthUser newUser = new AuthUser();
            newUser.setEmail(userInfo.get("email"));
            newUser.setName(userInfo.get("name"));

            authUserRepository.save(newUser);

            return login(newUser);
        }
    }

    @Transactional
    public String login(AuthUser user) {

        String token = jwtToken.generateToken(user.getEmail());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }




}

