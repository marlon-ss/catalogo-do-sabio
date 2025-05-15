package br.com.santander.catalogo_do_sabio.domain.service;

import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.UserApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiService {

    private final UserApiRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserApi createUser(UserApi user) {
        log.info("Criando usuário: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username já está em uso"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));;
        return userRepository.save(user);
    }

    public UserApi findUserByUsername(String username) {
        log.info("Buscando usuário: {}", username);
        UserApi user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Sem usuários com este username"));
        log.info("Usuário encontrado: {}", user.toString());
        return user;
    }

    public UserApi updateUser(UserApi user) {
        log.info("Atualizando usuário: {}", user.getUsername());
        Optional<UserApi> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            UserApi userDb = userOptional.get();
            userDb.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(userDb);
        } else {
            throw new DataNotFoundException("Usuário não encontrado");
        }
    }
}