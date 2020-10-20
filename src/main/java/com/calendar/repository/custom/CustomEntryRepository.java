package com.calendar.repository.custom;

import com.calendar.domain.Entry;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEntryRepository {

	List<Entry> getAllProjects(int userId);
	
	List<Entry> getActiveOrClosedProjects(int userId, boolean closed);
	
	List<Entry> getOrderedEntriesByUserId(int userId);
	
	void removeEntry(Entry entry);

	List<Entry> getSortedEntries(int userId, LocalDateTime startDate, LocalDateTime endDate);
}
