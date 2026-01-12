package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.post.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsLikeRepository extends JpaRepository<Posts, Long> {
    boolean existsByPostsIdAndMemberId(Long postsId, Long memberId);
}
