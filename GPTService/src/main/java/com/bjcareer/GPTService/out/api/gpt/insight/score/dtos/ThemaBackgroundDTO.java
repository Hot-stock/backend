package com.bjcareer.GPTService.out.api.gpt.insight.score.dtos;

public class ThemaBackgroundDTO {
	public String thema;
	public String background;
	public String trigger;

	public ThemaBackgroundDTO(String thema, String background, String trigger) {
		this.thema = thema;
		this.background = background;
		this.trigger = trigger;
	}
}
