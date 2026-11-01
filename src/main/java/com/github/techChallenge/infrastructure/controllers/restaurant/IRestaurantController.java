package com.github.techChallenge.infrastructure.controllers.restaurant;

import com.github.techChallenge.domain.restaurant.dto.RestaurantCreateInputDTO;
import com.github.techChallenge.domain.restaurant.dto.RestaurantOutputDTO;
import com.github.techChallenge.domain.restaurant.dto.RestaurantUpdateInputDTO;
import com.github.techChallenge.infrastructure.security.RestaurantUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "Restaurantes", description = "Gestão de restaurantes e dos itens de seus cardápios")
@ApiResponse(responseCode = "400", description = "Dados ou paginação inválidos", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
@ApiResponse(responseCode = "404", description = "Restaurante ou categoria inexistente", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
public interface IRestaurantController {

    @Operation(operationId = "createRestaurant", summary = "Cadastrar restaurante", description = "O dono é o usuário autenticado. O nome é único, sem distinguir maiúsculas e minúsculas.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "201", description = "Restaurante cadastrado", headers = @Header(name = "Location", description = "URL do restaurante criado", schema = @Schema(type = "string", format = "uri")))
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não possui nível OWNER", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome já cadastrado", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<RestaurantOutputDTO> create(RestaurantCreateInputDTO input,
            @Parameter(hidden = true) RestaurantUserPrincipal user);

    @Operation(operationId = "updateRestaurant", summary = "Atualizar restaurante", description = "Somente o dono pode atualizar. O proprietário é preservado.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "200", description = "Restaurante atualizado")
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é dono deste restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome já cadastrado", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    RestaurantOutputDTO update(@Parameter(description = "ID do restaurante", example = "1") Long id,
            RestaurantUpdateInputDTO input, @Parameter(hidden = true) RestaurantUserPrincipal user);

    @Operation(operationId = "findRestaurant", summary = "Consultar restaurante", description = "Consulta pública de um restaurante pelo ID.")
    @ApiResponse(responseCode = "200", description = "Restaurante localizado")
    RestaurantOutputDTO find(@Parameter(description = "ID do restaurante", example = "1") Long id);

    @Operation(operationId = "listRestaurants", summary = "Listar restaurantes", description = "Consulta pública. Use categoryId para filtrar pelo tipo de cozinha. Paginação: page >= 0 e offset de 1 a 100.")
    @ApiResponse(responseCode = "200", description = "Página de restaurantes")
    Page<RestaurantOutputDTO> list(
            @Parameter(description = "ID da categoria de cozinha; omita para consultar todas", example = "1") Integer categoryId,
            @Parameter(description = "Número da página, iniciando em zero", example = "0") int page,
            @Parameter(description = "Quantidade de registros por página, de 1 a 100", example = "10") int offset);

    @Operation(operationId = "deleteRestaurant", summary = "Excluir restaurante", description = "Somente o dono pode excluir. Remova primeiro os itens do cardápio.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "204", description = "Restaurante excluído", content = @Content)
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é dono deste restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Restaurante ainda possui itens no cardápio", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> delete(@Parameter(description = "ID do restaurante", example = "1") Long id,
            @Parameter(hidden = true) RestaurantUserPrincipal user);
}
