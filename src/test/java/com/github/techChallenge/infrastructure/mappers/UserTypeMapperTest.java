package com.github.techChallenge.infrastructure.mappers;

import com.github.techChallenge.domain.usertype.UserType;
import com.github.techChallenge.domain.usertype.dto.UserTypeOutputDTO;
import com.github.techChallenge.infrastructure.entities.usertype.UserTypeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeMapperTest {
    private final UserTypeMapper mapper = new UserTypeMapper();

    @Test
    void shouldMapEntityIdentityAndTitleToDomain() {
        UserTypeEntity entity = new UserTypeEntity(new UserType(7, "Administrador"));

        UserType result = mapper.fromEntityToDomain(entity);

        assertEquals(7, result.getId());
        assertEquals("Administrador", result.getTitle());
    }

    @Test
    void shouldReturnNullForAnAbsentEntity() {
        assertNull(mapper.fromEntityToDomain(null));
    }

    @Test
    void shouldMapDomainIdentityAndTitleToOutput() {
        UserTypeOutputDTO result = mapper.fromDomainToOutputDTO(new UserType(8, "Cliente"));

        assertEquals(new UserTypeOutputDTO(8, "Cliente"), result);
    }

    @Test
    void shouldReturnNullForAnAbsentDomainWhenMappingToOutput() {
        assertNull(mapper.fromDomainToOutputDTO(null));
    }

    @Test
    void shouldPreserveIdentityWhenMappingAnExistingDomainToEntity() {
        UserTypeEntity result = mapper.fromDomainToEntity(new UserType(9, "Fornecedor"));

        assertEquals(9, result.getId());
        assertEquals("Fornecedor", result.getTitle());
    }

    @Test
    void shouldKeepTheGeneratedIdentityUnsetForANewDomain() {
        UserTypeEntity result = mapper.fromDomainToEntity(UserType.create("Novo tipo"));

        assertEquals(0, result.getId());
        assertEquals("Novo tipo", result.getTitle());
    }

    @Test
    void shouldReturnNullForAnAbsentDomainWhenMappingToEntity() {
        assertNull(mapper.fromDomainToEntity(null));
    }

    @Test
    void shouldMapEveryEntityAndPreservePaginationAndOrder() {
        var pageable = PageRequest.of(2, 2);
        Page<UserTypeEntity> source = new PageImpl<>(List.of(
                new UserTypeEntity(new UserType(5, "Dono")),
                new UserTypeEntity(new UserType(6, "Cliente"))), pageable, 11);

        Page<UserType> result = mapper.fromEntityPageToDomainPage(source);

        assertEquals(List.of(5, 6), result.getContent().stream().map(UserType::getId).toList());
        assertEquals(List.of("Dono", "Cliente"), result.getContent().stream().map(UserType::getTitle).toList());
        assertEquals(pageable, result.getPageable());
        assertEquals(11, result.getTotalElements());
        assertEquals(6, result.getTotalPages());
    }

    @Test
    void shouldPreserveAnEmptyEntityPage() {
        var pageable = PageRequest.of(0, 7);

        Page<UserType> result = mapper.fromEntityPageToDomainPage(Page.empty(pageable));

        assertTrue(result.isEmpty());
        assertEquals(pageable, result.getPageable());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void shouldMapEveryDomainAndPreserveOutputPaginationAndOrder() {
        var pageable = PageRequest.of(1, 2);
        Page<UserType> source = new PageImpl<>(List.of(
                new UserType(3, "Dono"), new UserType(4, "Cliente")), pageable, 9);

        Page<UserTypeOutputDTO> result = mapper.fromDomainPageToOutputDTOPage(source);

        assertEquals(List.of(new UserTypeOutputDTO(3, "Dono"), new UserTypeOutputDTO(4, "Cliente")), result.getContent());
        assertEquals(pageable, result.getPageable());
        assertEquals(9, result.getTotalElements());
        assertEquals(5, result.getTotalPages());
    }

    @Test
    void shouldPreserveAnEmptyDomainPage() {
        var pageable = PageRequest.of(0, 5);

        Page<UserTypeOutputDTO> result = mapper.fromDomainPageToOutputDTOPage(Page.empty(pageable));

        assertTrue(result.isEmpty());
        assertEquals(pageable, result.getPageable());
        assertEquals(0, result.getTotalElements());
    }
}
