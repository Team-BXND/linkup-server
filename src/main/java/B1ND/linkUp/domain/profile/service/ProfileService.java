package B1ND.linkUp.domain.profile.service;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.auth.repository.UserRepository;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import B1ND.linkUp.domain.post.repository.PostsCommentRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.domain.profile.dto.response.MyAnswerItemResponse;
import B1ND.linkUp.domain.profile.dto.response.MyQuestionItemResponse;
import B1ND.linkUp.domain.profile.dto.response.ProfileResponse;
import B1ND.linkUp.domain.ranking.dto.response.GetRankingResponse;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
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

    public APIResponse<PageResponse<MyQuestionItemResponse>> getMyQuestions(int page) {
        User user = securityUtil.getUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Posts> postsPage = postsRepository.findByUser(user, pageable);

        List<MyQuestionItemResponse> items =
                MyQuestionItemResponse.fromPage(
                        postsPage,
                        postId -> (int) postsCommentRepository.countByPosts_Id(postId)
                );

        return APIResponse.ok(PageResponse.of(items, postsPage));
    }

    public APIResponse<PageResponse<MyAnswerItemResponse>> getMyAnswers(int page) {
        User user = securityUtil.getUser();
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<PostsComment> commentPage = postsCommentRepository.findByUser(user, pageable);

        List<MyAnswerItemResponse> items = MyAnswerItemResponse.fromPage(commentPage);

        return APIResponse.ok(PageResponse.of(items, commentPage));
    }
}