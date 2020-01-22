package com.calendar.repository.custom;

import java.util.List;

import com.calendar.domain.Entry;

public interface CustomEntryRepository {

	List<Entry> getEntitiesByUserId(int userId);
	
}