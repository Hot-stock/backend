package com.bjcareer.search.out.persistence.thema;

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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ThemaRepositoryAdapter implements LoadSearchKeywordPort, ThemaRepositoryPort {
	@PersistenceContext
	private final EntityManager em;
	private final ThemaRepository themaRepository;

	@Override
	public List<Thema> loadSearchKeyword(String keyword) {
		Pageable pageable = PageRequest.of(0, 10);
		return themaRepository.findAllByKeywordContaining(keyword);
	}

	@Override
	public Optional<Thema> loadByStockNameAndThemaName(LoadStockByThemaCommand command) {
		return themaRepository.findByStockNameAndThemaName(command.getStockName(), command.getThemaName());
	}

	@Override
	public List<Thema> loadAllByKeywordContaining(LoadThemaUsingkeywordCommand command) {
		return themaRepository.findAllByKeywordContaining(command.getKeyword());
	}

	@Override
	public Thema save(Thema thema) {
		return themaRepository.save(thema);
	}

	@Override
	public List<Thema> findAll() {
		return em.createQuery("SELECT t FROM Thema t join fetch t.stock t1 join fetch  t.themaInfo", Thema.class).getResultList();
	}

	@Override
	public void saveAll(List<Thema> themaList) {
		themaRepository.saveAll(themaList);
	}

	@Override
	public Optional<Thema> findByName(String thema, String stockName) {
		if (thema.isEmpty() || stockName.isEmpty()) {
			log.debug("findByName themas: {} {}", thema, stockName);
			return Optional.empty();
		}

		List<Thema> themas = em.createQuery(Query.findThemaByName, Thema.class)
			.setParameter("thema", thema)
			.setParameter("stockName", stockName)
			.getResultList();

		log.debug("thema = {}, {}", thema, stockName);

		if (themas.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(themas.getFirst());
	}
}
