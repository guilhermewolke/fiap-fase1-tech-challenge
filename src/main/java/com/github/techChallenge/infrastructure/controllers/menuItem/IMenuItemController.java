package com.github.techChallenge.infrastructure.controllers.menuItem;

import com.github.techChallenge.domain.menuItem.dto.MenuItemCreateInputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemOutputDTO;
import com.github.techChallenge.domain.menuItem.dto.MenuItemUpdateInputDTO;
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
@ApiResponse(responseCode = "404", description = "Restaurante ou item inexistente neste restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
public interface IMenuItemController {

    @Operation(operationId = "createRestaurantMenuItem", summary = "Cadastrar item no cardápio do restaurante", description = "O restaurante é definido por restaurantId na URL. O usuário deve ser OWNER e dono desse restaurante.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "201", description = "Item cadastrado", headers = @Header(name = "Location", description = "URL do item dentro do restaurante", schema = @Schema(type = "string", format = "uri")))
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é dono do restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome já cadastrado no mesmo restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<MenuItemOutputDTO> create(
            @Parameter(description = "ID do restaurante dono do cardápio", example = "1") Long restaurantId,
            MenuItemCreateInputDTO input, @Parameter(hidden = true) RestaurantUserPrincipal user);

    @Operation(operationId = "updateRestaurantMenuItem", summary = "Atualizar item do cardápio do restaurante", description = "Somente o dono pode atualizar. O item deve pertencer ao restaurante da URL e não pode ser transferido.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "200", description = "Item atualizado")
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é dono do restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome já cadastrado no mesmo restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    MenuItemOutputDTO update(
            @Parameter(description = "ID do restaurante dono do cardápio", example = "1") Long restaurantId,
            @Parameter(description = "ID do item dentro desse restaurante", example = "10") Long id,
            MenuItemUpdateInputDTO input, @Parameter(hidden = true) RestaurantUserPrincipal user);

    @Operation(operationId = "findRestaurantMenuItem", summary = "Consultar item do cardápio do restaurante", description = "Consulta pública. Retorna 404 se o item não pertencer ao restaurante da URL.")
    @ApiResponse(responseCode = "200", description = "Item localizado")
    MenuItemOutputDTO find(
            @Parameter(description = "ID do restaurante dono do cardápio", example = "1") Long restaurantId,
            @Parameter(description = "ID do item dentro desse restaurante", example = "10") Long id);

    @Operation(operationId = "listRestaurantMenu", summary = "Listar cardápio do restaurante", description = "Consulta pública dos itens de um único restaurante. Paginação: page >= 0 e offset de 1 a 100.")
    @ApiResponse(responseCode = "200", description = "Página de itens do restaurante")
    Page<MenuItemOutputDTO> list(
            @Parameter(description = "ID do restaurante dono do cardápio", example = "1") Long restaurantId,
            @Parameter(description = "Número da página, iniciando em zero", example = "0") int page,
            @Parameter(description = "Quantidade de registros por página, de 1 a 100", example = "10") int offset);

    @Operation(operationId = "deleteRestaurantMenuItem", summary = "Excluir item do cardápio do restaurante", description = "Somente o dono pode excluir. O item deve pertencer ao restaurante da URL.")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "204", description = "Item excluído", content = @Content)
    @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é dono do restaurante", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> delete(
            @Parameter(description = "ID do restaurante dono do cardápio", example = "1") Long restaurantId,
            @Parameter(description = "ID do item dentro desse restaurante", example = "10") Long id,
            @Parameter(hidden = true) RestaurantUserPrincipal user);
}
