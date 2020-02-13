package com.calendar.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.calendar.domain.User;
import com.calendar.repository.EntryRepository;
import com.calendar.repository.custom.CustomEntryRepository;
import com.calendar.requestdto.EntryDto;
import com.calendar.responsedto.EntryListResponseDto;
import com.calendar.responsedto.EntryResponseDto;
import com.calendar.responsedto.ProjektEntriesResponseDto;
import com.calendar.service.EntryService;

@Service
public class EntryServiceImpl implements EntryService {

	private EntryRepository entryRepository;
	private CustomEntryRepository customEntryRepository;
	private UserServiceImpl userServiceImpl;

	@Autowired
	public EntryServiceImpl(EntryRepository entryRepository, UserServiceImpl userServiceImpl
			, CustomEntryRepository customEntryRepository) {
		this.entryRepository = entryRepository;
		this.userServiceImpl = userServiceImpl;
		this.customEntryRepository = customEntryRepository;
	}

	@Override
	@Transactional
	public void createEntry(EntryDto entryDto) {
		
		User user = userServiceImpl.getFullUser();
		
		Entry entry = new Entry(entryDto.getTitle(), entryDto.getDescription(), entryDto.getDate(), entryDto.getDuration(), entryDto.getTermin(), 
				EntryType.valueOf(entryDto.getEntryType()) , EntryPhase.valueOf(entryDto.getEntryPhase()));
		entry.setUserId(user.getId());
		
		if (entryDto.getAddedEntryId() != null) {
			entry.addEntryConnection(entryRepository.getOne(entryDto.getAddedEntryId()));
			entry.setChild(true);
		}
		
		entryRepository.save(entry);
	}

	@Override
	@Transactional
	public EntryListResponseDto getEntries() {
		
		EntryListResponseDto entryResponseDto = new EntryListResponseDto();
		
		
		User user = userServiceImpl.getFullUser();
		List<Entry> entryList = new ArrayList<Entry>();
		entryList = customEntryRepository.getEntriesByUserId(user.getId());
		entryResponseDto.setEntryList(entryList);
		
		return entryResponseDto;
	}
	
	public ArrayList<ProjektEntriesResponseDto> getProjekts(boolean isFinished) {
		
		User user = userServiceImpl.getFullUser();
		List<Entry> entryList = new ArrayList<Entry>();
		entryList = customEntryRepository.getEntriesByUserIdAndStatus(user.getId(), isFinished);
		
		ArrayList<ProjektEntriesResponseDto> perDtoList = new ArrayList();
		for (int i = 0; i < entryList.size(); i++) {
			Entry entry = entryList.get(i);
			ProjektEntriesResponseDto perDto = new ProjektEntriesResponseDto(entry.getId(), entry.getTitle(), entry.getEntryPhase());
			perDtoList.add(perDto);
		}
		
		return perDtoList;
		
	}

	@Override
	@Transactional
	public EntryResponseDto getEntryById(int id) {
		
		Entry e = entryRepository.getOne(id);		
		EntryResponseDto erDto = new EntryResponseDto(e.getId(), e.getUserId(), e.getTitle(), e.getDescription(), e.getDate(), 
				e.getDuration(), e.getTermin(), e.getEntryType(), e.getEntryPhase(), e.isChild(), e.isFinished());
		
		return erDto;
	}
	
}
