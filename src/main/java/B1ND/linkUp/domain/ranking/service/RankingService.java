package B1ND.linkUp.domain.ranking.service;

import B1ND.linkUp.domain.auth.repository.UserRepository;
import B1ND.linkUp.domain.ranking.dto.response.GetRankingResponse;
import B1ND.linkUp.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final UserRepository userRepository;

     public APIResponse<List<GetRankingResponse>> getRanking() {
         List<GetRankingResponse> users = userRepository.findAllByRanking();

         return APIResponse.ok(users);
     }
}
