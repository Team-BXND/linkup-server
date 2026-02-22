package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.entity.PostsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface PostsLikeRepository extends JpaRepository<PostsLike, Long> {
    boolean existsByPosts_IdAndUser(Long postsId, User user);

    Optional<PostsLike> findByPosts_IdAndUser(Long postId, User user);
}
