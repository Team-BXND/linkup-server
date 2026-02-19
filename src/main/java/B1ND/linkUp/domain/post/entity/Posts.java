package B1ND.linkUp.domain.post.entity;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.dto.request.UpdatePostsRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(columnDefinition = "TEXT")
    private String content;
    private String author;
    private Category category;

    @Builder.Default
    private boolean isAccepted = false;
    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<PostsLike> like;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<PostsComment> comments;

    @ManyToOne
    private User user;

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
        comment.setAccepted();
    }

    public int commentCount() {
        return comments.size();
    }
}
