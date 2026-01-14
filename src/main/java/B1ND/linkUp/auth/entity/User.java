package B1ND.linkUp.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private User(String email, String username, String password, LocalDateTime createdAt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}