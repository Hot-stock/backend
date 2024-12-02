package com.bjcareer.search.out.persistence.thema;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ThemaInfoRepositoryAdapter implements ThemaInfoRepositoryPort {
	private final ThemaInfoRepository themaInfoRepository;

	@Override
	public Optional<ThemaInfo> loadByName(String thema) {
		return themaInfoRepository.findByName(thema);
	}

	@Override
	public ThemaInfo save(ThemaInfo themaInfo) {
		return themaInfoRepository.save(themaInfo);
	}

	@Override
	public List<ThemaInfo> findAll() {
		return themaInfoRepository.findAll();
	}

	@Override
	public void saveAll(List<ThemaInfo> themaInfoList) {
		themaInfoRepository.saveAll(themaInfoList);
	}

	@Override
	public Optional<ThemaInfo> findByName(String thema) {
		return themaInfoRepository.findByName(thema);
	}
}
