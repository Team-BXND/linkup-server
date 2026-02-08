package B1ND.linkUp.domain.auth.repository;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.ranking.dto.response.GetRankingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(nativeQuery = true,
    value = "SELECT u.username AS username, u.point AS point, DENSE_RANK() OVER (ORDER BY u.point DESC) AS ranking " +
            "FROM `users` u " +
            "ORDER BY u.point DESC " +
            "LIMIT 30")
    List<GetRankingResponse> findAllByRanking();
}
