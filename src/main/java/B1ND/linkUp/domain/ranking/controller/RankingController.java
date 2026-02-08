package B1ND.linkUp.domain.ranking.controller;

import B1ND.linkUp.domain.ranking.dto.response.GetRankingResponse;
import B1ND.linkUp.domain.ranking.service.RankingService;
import B1ND.linkUp.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @GetMapping
    public APIResponse<List<GetRankingResponse>> getRanking() {
        return rankingService.getRanking();
    }
}
