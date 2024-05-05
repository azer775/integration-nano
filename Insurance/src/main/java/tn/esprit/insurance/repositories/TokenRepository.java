package tn.esprit.insurance.repositories;

import java.util.List;
import java.util.Optional;

import tn.esprit.insurance.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.Id = u.Id\s
      where u.Id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    void deleteByUserId(int userId);
}
