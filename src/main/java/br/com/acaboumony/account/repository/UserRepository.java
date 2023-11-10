package br.com.acaboumony.account.repository;

import br.com.acaboumony.account.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);

    @Query("SELECT u.email FROM Users u")
    List<String> findEmails();
    @Query("SELECT u.cpf FROM Users u")
    List<String> findCpfs();
}
