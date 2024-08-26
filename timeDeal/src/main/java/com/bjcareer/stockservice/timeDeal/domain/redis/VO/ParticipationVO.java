package com.bjcareer.stockservice.timeDeal.domain.redis.VO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ParticipationVO {
	private final String clientId;
	private final Double score;
}
