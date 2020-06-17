package com.calendar.responsedto;

import java.util.ArrayList;

public class ProjectviewResponseDto {

	private FullProjectResponseDto fullProjectResponseDto;
	private ArrayList<ProjectEntriesResponseDto> projectEntriesResponseDto;
	
	public ProjectviewResponseDto(ArrayList<ProjectEntriesResponseDto> projectEntriesResponseDto, FullProjectResponseDto fullProjectResponseDto) {
		this.projectEntriesResponseDto = projectEntriesResponseDto;
		this.fullProjectResponseDto = fullProjectResponseDto;
	}

	public ArrayList<ProjectEntriesResponseDto> getProjectEntriesResponseDto() {
		return projectEntriesResponseDto;
	}

	public void setProjectEntriesResponseDto(ArrayList<ProjectEntriesResponseDto> projectEntriesResponseDto) {
		this.projectEntriesResponseDto = projectEntriesResponseDto;
	}

	public FullProjectResponseDto getFullProjectResponseDto() {
		return fullProjectResponseDto;
	}

	public void setFullProjectResponseDto(FullProjectResponseDto fullProjectResponseDto) {
		this.fullProjectResponseDto = fullProjectResponseDto;
	}
}
