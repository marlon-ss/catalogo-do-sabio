package br.com.santander.catalogo_do_sabio.api.controllers;

import br.com.santander.catalogo_do_sabio.api.dto.UserApiDTO;
import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.domain.service.UserApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários")
public class UserApiController {

    private final UserApiService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastra um novo usuário", description = "Retorna o usuário cadastrado")
    public ResponseEntity<UserApiDTO> registerUser(@RequestBody UserApi user) {
        return ResponseEntity.ok(new UserApiDTO(userService.createUser(user)));
    }

    @PostMapping("/get/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Busca um usuário", description = "Busca um usuário pelo seu username")
    public ResponseEntity<UserApiDTO> registerUser(@PathVariable String username) {
        return ResponseEntity.ok(new UserApiDTO(userService.getUser(username)));
    }
}