package com.bjcareer.search.out.persistence.memory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.Suggestion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class MemoryRepository {
	@PersistenceContext
	private EntityManager em;

	public List<Suggestion> findSuggestionKeyword(String keyword) {
		List<Suggestion> result = new ArrayList<>();

		Query query = em.createQuery(QueryConst.GET_SUGGESTION_KEYWORD);
		query.setParameter("target", keyword + "%");

		query.getResultList().forEach(rank -> result.add((Suggestion)rank));
		return result;
	}

	public Suggestion updateSearchCount(String keyword) {
		Query query = em.createQuery(QueryConst.UPDATE_SEARCH_COUNT);
		query.setParameter("target", keyword);
		Suggestion suggestion = (Suggestion) query.getSingleResult();
		return suggestion;
	}

	public void saveKeyword(Suggestion suggestion) {
		em.persist(suggestion);
	}

	public List<Suggestion> getAllSuggestion() {
		return em.createQuery(QueryConst.GET_ALL_DATA, Suggestion.class).getResultList();
	}
}
