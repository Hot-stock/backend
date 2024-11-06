package com.bjcareer.search.out.persistence.repository.thema;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.api.LoadSearchKeywordPort;
import com.bjcareer.search.application.port.out.persistence.thema.LoadStockByThemaCommand;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaUsingkeywordCommand;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ThemaRepositoryAdapter implements LoadSearchKeywordPort, ThemaRepositoryPort {
	@PersistenceContext
	private final EntityManager em;
	private final ThemaRepository themaRepository;

	@Override
	public List<Thema> loadSearchKeyword(String keyword) {
		Pageable pageable = PageRequest.of(0, 10);
		return themaRepository.findAllByKeywordContaining(keyword, pageable);
	}

	@Override
	public Optional<Thema> loadByStockNameAndThemaName(LoadStockByThemaCommand command) {
		return themaRepository.findByStockNameAndThemaName(command.getStockName(), command.getThemaName());
	}

	@Override
	public List<Thema> loadAllByKeywordContaining(LoadThemaUsingkeywordCommand command) {
		return themaRepository.findAllByKeywordContaining(command.getKeyword(), command.getPageable());
	}

	@Override
	public Thema save(Thema thema) {
		return themaRepository.save(thema);
	}

	@Override
	public List<Thema> findAll() {
		return themaRepository.findAll();
	}

	@Override
	public void saveAll(List<Thema> themaList) {
		themaRepository.saveAll(themaList);
	}

	@Override
	public Optional<ThemaInfo> findByName(String thema) {
		List<Thema> themas = em.createQuery(Query.findThemaByName, Thema.class)
			.setParameter("thema", thema)
			.getResultList();

		return Optional.of(themas.get(0).getThemaInfo());
	}
}
