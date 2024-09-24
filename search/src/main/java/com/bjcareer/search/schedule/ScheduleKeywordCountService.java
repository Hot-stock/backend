package com.bjcareer.search.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.entity.ThemaInfoSearchCount;
import com.bjcareer.search.repository.stock.SearchCountRepository;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;
import com.bjcareer.search.service.ConverterSearchCountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleKeywordCountService {
	private final ThemaInfoRepository themaInfoRepository;
	private final SearchCountRepository searchCountRepository;
	private final ConverterSearchCountService converterSearchCountService;

	// 매일 아침 10시에 실행
	@Scheduled(cron = "0 0 10 * * *")
	@Transactional
	public void collectKeywordCountOfThemas() throws InterruptedException {
		log.debug("collectKeywordCountOfThemas started");
		List<ThemaInfo> themaInfos = themaInfoRepository.findAll();

		for (ThemaInfo themaInfo : themaInfos) {
			log.info(themaInfo.getName());
			List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterSearchCountService.getAbsoluteValueOfKeyword(
				themaInfo.getName());

			for (AbsoluteRankKeyword absoluteRankKeyword : absoluteValueOfKeyword) {
				LocalDate date = LocalDate.parse(absoluteRankKeyword.getPeriod());

				searchCountRepository.findByThemaInfo_NameAndDate(themaInfo.getName(), date)
					.ifPresentOrElse(
						// 키워드가 존재할 때는 아무것도 하지 않음
						existing -> {
						},
						// 키워드가 존재하지 않을 때 실행할 로직
						() -> searchCountRepository.save(new ThemaInfoSearchCount(
							themaInfo,
							absoluteRankKeyword.getAbsoluteKeywordCount(),
							LocalDate.parse(absoluteRankKeyword.getPeriod())
						))
					);
			}

			Thread.sleep(1000);
		}
	}
}
