package com.github.techChallenge.infrastructure.controllers.restaurantCategory;

import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryCreateInputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryOutputDTO;
import com.github.techChallenge.domain.restaurantCategory.dto.RestaurantCategoryUpdateInputDTO;
import com.github.techChallenge.shared.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
    name="Restaurantes » Tipos",
    description="API de gestão de tipos de restaurante"
)
public interface IRestaurantCategoryController {
    @Operation(
        summary = "Criação de novo tipo de restaurante"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Um objeto com os dados do tipo de restaurante criado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = RestaurantCategoryOutputDTO.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ocorreu um erro com os dados do tipo de restaurante, durante a tentativa de criação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Você não tem permissão para realizar esta ação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Já existe um tipo de restaurante cadastrado com o título informado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Ocorreu um erro do lado do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    ResponseEntity<RestaurantCategoryOutputDTO> create(
        @Parameter(description = "Dados para criação de novo tipo de restaurante")
        RestaurantCategoryCreateInputDTO dto
    );

    @Operation(
        summary = "Atualização de dados do tipo de restaurante"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "O tipo de restaurante foi modificado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RestaurantCategoryOutputDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ocorreu um erro com os dados do tipo de restaurante, durante a tentativa de atualização",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Você não tem permissão para realizar esta ação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "O tipo de restaurante que se deseja atualizar não foi encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "O título informado já pertence a outro tipo de restaurante",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Ocorreu um erro do lado do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    ResponseEntity<RestaurantCategoryOutputDTO> update(
        @Parameter(description = "Dados para edição de novo tipo de restaurante")
        RestaurantCategoryUpdateInputDTO dto,
        @Parameter(description = "ID do tipo de restaurante a ser editado")
        int id);

    @Operation(
        summary = "Buscar por ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Busca com sucesso de tipo de restaurante pelo seu ID",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RestaurantCategoryOutputDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Você não tem permissão para realizar esta ação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de restaurante não encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Ocorreu um erro do lado do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    ResponseEntity<RestaurantCategoryOutputDTO> findByID(
        @Parameter(description = "ID do tipo de restaurante a ser localizado")
        int id
    );

    @Operation(
        summary = "Listar todos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listar todos os níveis de restaurante",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array= @ArraySchema(schema = @Schema(implementation = RestaurantCategoryOutputDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Você não tem permissão para realizar esta ação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Ocorreu um erro do lado do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    ResponseEntity<Page<RestaurantCategoryOutputDTO>> list(
        @Parameter(description = "Página da paginação da listagem. Valor padrão '0'.", example = "0", required = false)
        int page,
        @Parameter(description = "Quantidade de registros por página. Valor padrão '10'.", example = "10", required = false)
        int offset);

    @Operation(
        summary = "Remover tipo de restaurante"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "O tipo de restaurante foi removido com sucesso. A resposta não possui corpo.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Você não tem permissão para realizar esta ação",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "O tipo de restaurante que se deseja remover não foi encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Ocorreu um erro do lado do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        )
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "ID do tipo de restaurante a ser removido")
        int id);
}
