package org.example.deliveryservice.service;



import org.example.deliveryservice.dto.authUserDto.AuthUserResponseDTO;
import org.example.deliveryservice.dto.authUserDto.CreateAuthUserDTO;
import org.example.deliveryservice.dto.authUserDto.UpdateAuthUserDTO;

import java.util.List;

public interface AuthUserService {
    AuthUserResponseDTO createUser(CreateAuthUserDTO createAuthUserDTO);

    AuthUserResponseDTO getUserById(Long id);

    List<AuthUserResponseDTO> getAllUsers();

    AuthUserResponseDTO updateUser(Long id, UpdateAuthUserDTO updateAuthUserDTO);

    void deleteUserById(Long id);
}
