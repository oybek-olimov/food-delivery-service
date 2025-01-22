package org.example.deliveryservice.mapper;

import jakarta.validation.Valid;
import org.example.deliveryservice.dto.authUserDto.AuthUserResponseDTO;
import org.example.deliveryservice.dto.authUserDto.CreateAuthUserDTO;
import org.example.deliveryservice.dto.authUserDto.UpdateAuthUserDTO;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {


    AuthUserResponseDTO toResponseDTO(@Valid AuthUser authUser);

    List<AuthUserResponseDTO> toResponseDTOList(@Valid List<AuthUser> authUsers);

    AuthUserResponseDTO toUpdateDTO(@Valid UpdateAuthUserDTO dto);

    AuthUserResponseDTO toCreateDTO(@Valid CreateAuthUserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AuthUser partialUpdateAuthUser(UpdateAuthUserDTO dto, @MappingTarget AuthUser user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AuthUser partialCreateAuthUser(CreateAuthUserDTO dto, @MappingTarget AuthUser user);


}
