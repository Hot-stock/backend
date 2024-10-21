package com.bjcareer.stockservice.timeDeal.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationDomain {
	private String clientId;
	private String sessionId;

	private boolean result = false;
	private Long participationIndex;

	public ParticipationDomain(String clientId, String sessionId) {
		this.clientId = clientId;
		this.sessionId = sessionId;
	}

	public void addParticipationIndex(Long participationIndex) {
		this.participationIndex = participationIndex;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
