package com.calendar.repository.custom.impl;

import com.calendar.domain.Entry;
import com.calendar.repository.custom.CustomEntryRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomEntryRepositoryImpl implements CustomEntryRepository{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<Entry> getEntriesByUserId(int userId) {
		return em
				.createQuery("SELECT e FROM Entry e WHERE e.userId = :id AND e.child = :isC", Entry.class)
				.setParameter("id", userId)
				.setParameter("isC", false)
				.getResultStream().sorted(new Comparator<Entry>() {
					@Override
					public int compare(Entry e1, Entry e2) {
						return e1.getTitle().compareToIgnoreCase(e2.getTitle());
					}
				}).collect(Collectors.toList());
	}
	
	@Override
	public List<Entry> getProjectsByUserIdAndStatus(int userId, boolean closed) {
		return em
				.createQuery("SELECT e FROM Entry e WHERE e.userId = :id AND e.child = :isC " +
						"AND e.closed = :isCl", Entry.class)
				.setParameter("id", userId)
				.setParameter("isC", false)
				.setParameter("isCl", closed)
				.getResultStream().sorted(new Comparator<Entry>() {
					@Override
					public int compare(Entry e1, Entry e2) {
						return e1.getTitle().compareToIgnoreCase(e2.getTitle());
					}
				}).collect(Collectors.toList());

	}

	public void removeEntry(Entry entry) {
		
		em.remove(em.contains(entry) ? entry : em.merge(entry));
		
	}
	
	public List<Entry> getOrderedEntriesByUserId(int userId) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entry> cq = cb.createQuery(Entry.class);
		Root<Entry> e = cq.from(Entry.class);
		
		cq
			.where(cb.equal(e.get("userId"), userId), cb.equal(e.get("child"), false));
		
		List<Order> orderList = new ArrayList();
		orderList.add(cb.asc(e.get("sortNumber")));
		
		cq
			.orderBy(orderList);
		
		TypedQuery<Entry> typedQuery = em.createQuery(cq);
		
		return typedQuery.getResultList();
		
	}
	
}
