package com.bjcareer.search.application.information;

import org.springframework.stereotype.Service;

import com.bjcareer.search.application.CommonValidations;
import com.bjcareer.search.application.port.in.GetInsightCommand;
import com.bjcareer.search.application.port.in.InsightUsecase;
import com.bjcareer.search.application.port.out.api.LoadNewsPort;
import com.bjcareer.search.application.port.out.api.LoadStockInformationPort;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsightService implements InsightUsecase {
	private final LoadNewsPort loadNewsPort;
	private final LoadStockInformationPort loadStockInformationServerPort;

	private final StockRepositoryPort stockRepositoryPort;
	private final StockChartRepositoryPort stockChartRepositoryPort;

	@Override
	public void getInsight(GetInsightCommand command) {
		Stock stock = CommonValidations.validationStock(stockRepositoryPort, command.getStockName());
		StockChart chart = CommonValidations.validationStockChart(stockChartRepositoryPort, stock.getCode());

		// chart.getNextSchedule(date);

		//시가총액 중에 낮은거, 적자 없고, 일단 오른 이유를 조사했지 -> 다른 주식에서도 해당 이유로 오른적이 있는지도 조사했지
		//이전에 올랐던 이유와 현재의 상황이 비슷한지 비교했지, 추가적으로 미국의 대형주들의 이벤트에 따라서도 일정을 매매했지
	}
}
