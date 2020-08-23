package com.calendar.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProjectDto {

	@NotNull
	private String title;

	private String description;
	
	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime deadline;
	
	public ProjectDto() {
		
	}

	public ProjectDto(String title, String description, LocalDateTime deadline) {
		this.title = title;
		this.description = description;
		this.deadline = deadline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}
	
}
