package com.calendar.controllers;

import com.calendar.requestdto.EntryDto;
import com.calendar.requestdto.EntryForModificationDto;
import com.calendar.requestdto.ProjectDto;
import com.calendar.requestdto.SingleDto;
import com.calendar.responsedto.*;
import com.calendar.service.impl.EntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EntryController {
	
	private EntryServiceImpl entryServiceImpl;
	
	@Autowired
	public EntryController(EntryServiceImpl entryServiceImpl) {
		this.entryServiceImpl = entryServiceImpl;
	}

	//Currently not used, but worth keeping for Postman tests.
	@GetMapping("/entry/getentries")
	public EntryListResponseDto getEntries() {
		return entryServiceImpl.getEntries();
	}

	@GetMapping("/projects")
	public ArrayList<ProjectEntriesResponseDto> getProjects(){
		return entryServiceImpl.getProjects();
	}

	@GetMapping("/project")
	public FullProjectResponseDto getFullProjectById(@Valid @RequestParam int id) {
		return entryServiceImpl.getFullProjectById(id);
	}

	@GetMapping("/projectview")
	public ProjectViewResponseDto getProjectview(@Valid @RequestParam int id) {
		return entryServiceImpl.getProjectView(id);
	}

	@GetMapping("/entry")
	public EntryResponseDto getEntryById(@RequestParam int id) {
		return entryServiceImpl.getEntryById(id);
	}

	@GetMapping("/periodsGroupedEntries")
	public List<SortedEntryDto> getSortedEntries(@RequestParam String startDate,
												 @RequestParam String endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm");
		LocalDateTime start = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);
		return entryServiceImpl.getSortedEntries(start, end);

	}

	@PostMapping("/entry")
	public ProjectViewResponseDto addEntryOnProjectview(@Valid @RequestBody EntryDto entryDto) {
		return entryServiceImpl.createEntry(entryDto);
	}

	@PostMapping("/expand")
	public void expand(@RequestParam int id, @RequestParam boolean expanded) {
		entryServiceImpl.expandEntry(id, expanded);
	}

	@PostMapping("/project")
	public ProjectViewResponseDto makeProjectEntry(@Valid @RequestBody ProjectDto projectDto) {
		return entryServiceImpl.createProject(projectDto);
	}

	@PostMapping("/single")
	public SingleDto makeProjectEntry(@Valid @RequestBody SingleDto singleDto) {
		return entryServiceImpl.createSingle(singleDto);
	}

	@PutMapping("/entry")
	public ProjectViewResponseForModificationDto modifyEntryById(@Valid @RequestBody EntryForModificationDto eDto,
																 @RequestParam int id,
																 @RequestParam boolean checkIfAllChildrenAreClosed,
																 @RequestParam boolean checkIfAllSiblingsAreClosed) {
		return entryServiceImpl.modifyEntryById(id, eDto, checkIfAllChildrenAreClosed, checkIfAllSiblingsAreClosed);
	}


	//Save it for future implementation - DO NOT USE NOW!!!
//	@PutMapping("/project")
//	public ProjectViewResponseDto modifyProjectById(@Valid @RequestBody ProjectDto projectDto, @RequestParam int id) {
//		return entryServiceImpl.modifyProjectById(id, projectDto);
//	}

	@DeleteMapping("/entry")
	public ProjectViewResponseDto deleteEntryById(@RequestParam int id) {
		return entryServiceImpl.deleteEntryById(id);
	}
}
