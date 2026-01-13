package B1ND.linkUp.domain.post.repository;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAll(Pageable pageable);
    Page<Posts> findByCategory(Category category, Pageable pageable);
}
