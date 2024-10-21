package com.bjcareer.stockservice.timeDeal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ParticipationDomain {
	private final String clientId;
	private final String sessionId;
	private Long participationIndex;

	public void addParticipationIndex(Long participationIndex) {
		this.participationIndex = participationIndex;
	}
}
