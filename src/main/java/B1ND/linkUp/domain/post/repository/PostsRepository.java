package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAll(Pageable pageable);
    Page<Posts> findByCategory(Category category, Pageable pageable);
    Page<Posts> findByUser(User user, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT p.* " +
                    "FROM posts p " +
                    "LEFT JOIN posts_like pl ON p.id = pl.post_id " +
                    "GROUP BY p.id " +
                    "HAVING COUNT(pl.id) >= 1 " +
                    "ORDER BY COUNT(pl.id) DESC, p.id DESC",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM posts p " +
                    "LEFT JOIN posts_like pl ON p.id = pl.post_id " +
                    "GROUP BY p.id " +
                    "HAVING COUNT(pl.id) >= 1")
    Page<Posts> findPopular(Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT p.* " +
                    "FROM posts p " +
                    "LEFT JOIN posts_like pl ON p.id = pl.post_id " +
                    "WHERE p.create_at BETWEEN :startDate AND :endDate " +
                    "GROUP BY p.id " +
                    "HAVING COUNT(pl.id) >= 1 " +
                    "ORDER BY COUNT(pl.id) DESC, p.id DESC",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM posts p " +
                    "LEFT JOIN posts_like pl ON p.id = pl.post_id " +
                    "WHERE p.create_at BETWEEN :startDate AND :endDate " +
                    "GROUP BY p.id " +
                    "HAVING COUNT(pl.id) >= 1")
    Page<Posts> findPopularPosts(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate,
                                 Pageable pageable);
}