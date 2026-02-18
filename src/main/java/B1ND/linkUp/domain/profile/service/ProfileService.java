package B1ND.linkUp.domain.profile.service;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.auth.repository.UserRepository;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import B1ND.linkUp.domain.post.repository.PostsCommentRepository;
import B1ND.linkUp.domain.post.repository.PostsLikeRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.domain.profile.dto.response.MyAnswerItemResponse;
import B1ND.linkUp.domain.profile.dto.response.MyQuestionItemResponse;
import B1ND.linkUp.domain.profile.dto.response.ProfileResponse;
import B1ND.linkUp.domain.ranking.dto.response.GetRankingResponse;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.ProfilePageResponse;
import B1ND.linkUp.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final PostsCommentRepository postsCommentRepository;
    private final PostsLikeRepository postsLikeRepository;

    public APIResponse<ProfileResponse> getProfile() {
        User user = securityUtil.getUser();

        GetRankingResponse my = userRepository.findMyRanking(user.getEmail());

        ProfileResponse response = new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPoint(),
                my.ranking()
        );

        return APIResponse.ok(response);
    }

    public APIResponse<ProfilePageResponse<MyQuestionItemResponse>> getMyQuestions(int page) {
        User user = securityUtil.getUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Posts> postsPage = postsRepository.findByUser(user, pageable);

        List<MyQuestionItemResponse> items = postsPage.getContent().stream()
                .map(p -> {
                    Long postId = p.getId();

                    int commentCount = (int) postsCommentRepository.countByPosts_Id(postId);
                    int like = (int) postsLikeRepository.countByPosts_Id(postId);
                    boolean isAccepted = p.isAccepted();

                    return MyQuestionItemResponse.of(p, like, commentCount, isAccepted, page);
                })
                .toList();

        return APIResponse.ok(ProfilePageResponse.of(items, postsPage));
    }

    public APIResponse<ProfilePageResponse<MyAnswerItemResponse>> getMyAnswers(int page) {
        User user = securityUtil.getUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<PostsComment> commentPage = postsCommentRepository.findByUser(user, pageable);

        List<MyAnswerItemResponse> items = MyAnswerItemResponse.fromPage(commentPage);

        return APIResponse.ok(ProfilePageResponse.of(items, commentPage));
    }
}