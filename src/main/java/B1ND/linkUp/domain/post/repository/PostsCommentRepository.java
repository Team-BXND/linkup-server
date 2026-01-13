package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.post.entity.PostsComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsCommentRepository extends JpaRepository<PostsComment, Long> {
}
