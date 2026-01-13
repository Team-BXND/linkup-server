package B1ND.linkUp.domain.post.entity;

import B1ND.linkUp.domain.post.dto.request.UpdatePostsRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;
    private Category category;

    @Builder.Default
    private boolean isAccepted = false;
    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @OneToMany(mappedBy = "posts")
    private List<PostsLike> like;

    @OneToMany(mappedBy = "posts")
    private List<PostsComment> comments;

    @OneToOne
    @JoinColumn(name = "accepted_comment_id")
    private PostsComment postsComment;

    public void updatePosts(UpdatePostsRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.category = request.category();
        this.author = request.author();
    }

    public int likeCount() {
        return like.size();
    }

    public void setAccepted(PostsComment comment) {
        this.postsComment = comment;
        this.isAccepted = true;
    }
}
