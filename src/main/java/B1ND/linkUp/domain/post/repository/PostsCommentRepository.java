package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsCommentRepository extends JpaRepository<PostsComment, Long> {
    Optional<PostsComment> findByIdAndPosts(Long id, Posts posts);
    Page<PostsComment> findByUser(User user, Pageable pageable);
    long countByPosts_Id(Long postsId);
}