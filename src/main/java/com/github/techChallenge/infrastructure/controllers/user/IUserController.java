package com.github.techChallenge.infrastructure.controllers.user;

import com.github.techChallenge.domain.user.dto.*;
import com.github.techChallenge.shared.ApiErrorResponse;
import com.github.techChallenge.shared.ResponseError;
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

import java.util.Map;

@Tag(
    name="Usuários",
    description="API de gestão de usuários"
)
public interface IUserController {
    @Operation(
        summary = "Criação de novo usuário"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Um objeto com os dados do usuário criado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = UserOutputDTO.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ocorreu um erro com os dados do usuário, durante a tentativa de criação",
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
            description = "Já existe um usuário cadastrado com o e-mail ou o login informado",
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
    ResponseEntity<UserOutputDTO> create(
        @Parameter(description = "Dados para criação de novo usuário")
        UserCreateInputDTO dto
    );

    @Operation(
        summary = "Atualização de dados do usuário"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "O usuário foi modificado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserOutputDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ocorreu um erro com os dados do usuário, durante a tentativa de atualização",
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
            description = "O usuário que se deseja atualizar não foi encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "O e-mail ou o login informado já pertence a outro usuário",
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
    ResponseEntity<UserOutputDTO> update(
        @Parameter(description = "Dados para edição de novo usuário")
        UserUpdateInputDTO dto,
        @Parameter(description = "ID do usuário a ser editado")
        Long id);

    @Operation(
        summary = "Buscar por nome"
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "Busca com sucesso de usuários cujo nome contenha o trecho informado via queryString",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array= @ArraySchema(schema = @Schema(implementation = UserOutputDTO.class))
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Ocorreu um erro com os dados do usuário, durante a tentativa de atualização",
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
                responseCode = "500",
                description = "Ocorreu um erro do lado do servidor",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class)
                )
            )
    })
    ResponseEntity<Page<UserOutputDTO>> list(
        @Parameter(description = "Trecho do nome do usuário a ser buscado", example = "josé", required = false)
        String name,
        @Parameter(description = "Página da paginação da listagem. Valor padrão '0'.", example = "0", required = false)
        int page,
        @Parameter(description = "Quantidade de registros por página. Valor padrão '10'.", example = "10", required = false)
        int offset);

    @Operation(
        summary = "Buscar por ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Busca com sucesso de usuário pelo seu ID",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserOutputDTO.class)
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
            description = "Usuário não encontrado",
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
    ResponseEntity<UserOutputDTO> findByID(
        @Parameter(description = "ID do usuário a ser localizado")
        Long id
    );

    @Operation(
        summary = "Listar todos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listar todos os usuários",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array= @ArraySchema(schema = @Schema(implementation = UserOutputDTO.class))
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
    ResponseEntity<Page<UserOutputDTO>> list(
        @Parameter(description = "Página da paginação da listagem. Valor padrão '0'.", example = "0", required = false)
        int page,
        @Parameter(description = "Quantidade de registros por página. Valor padrão '10'.", example = "10", required = false)
        int offset);

    @Operation(
        summary = "Remover usuário"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "O usuário foi removido com sucesso. A resposta não possui corpo.",
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
            description = "O usuário que se deseja remover não foi encontrado",
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
        @Parameter(description = "ID do usuário a ser removido")
        Long id);

    @Operation(
        summary = "Alterar a senha do usuário."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "O usuário foi modificado com sucesso",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Ocorreu um erro com os dados do usuário, durante a tentativa de atualização",
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
                responseCode = "402",
                description = "Conteudo da requisição não processável",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ResponseError.class)
                )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "O usuário que se deseja atualizar não foi encontrado",
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
    ResponseEntity<Map<String, Object>> changePassword(Long id, UserChangePasswordInputDTO inputDTO);

    @Operation(
            summary = "Autenticação em sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de autenticação inválidos",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
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
    ResponseEntity<Map<String, Object>> authUser(
            @Parameter(description = "Credenciais de login e senha")
            UserAuthInputDTO dto
    );

    @Operation(
            summary = "Adição de um usuário à um nível de usuário"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "O usuário foi modificado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserOutputDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ocorreu um erro com os dados do usuário, durante a tentativa de atualização",
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
                    description = "O usuário que se deseja adicionar, ou o nível que se deseja atribuir este usuário não foram encontrados",
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
    ResponseEntity<UserOutputDTO> grantLevel(
            @Parameter(description = "Dados para atribuição de nível do usuário")
            GrantUserLevelInputDTO dto);
}
