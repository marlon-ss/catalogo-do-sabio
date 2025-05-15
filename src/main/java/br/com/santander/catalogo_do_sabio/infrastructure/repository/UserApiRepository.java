package br.com.santander.catalogo_do_sabio.infrastructure.repository;

import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserApiRepository extends MongoRepository<UserApi, String> {
    Optional<UserApi> findByUsername(String username);
}