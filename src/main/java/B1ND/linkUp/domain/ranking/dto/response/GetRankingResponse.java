package B1ND.linkUp.domain.ranking.dto.response;

public record GetRankingResponse(
        String username,
        int point,
        long ranking
) {
}
