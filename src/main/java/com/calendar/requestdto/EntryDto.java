package com.calendar.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import java.time.LocalDateTime;

public class EntryDto {

	@NotNull
	private Integer parentId;

	@NotNull
	private String title;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime date;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime duration;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime deadline;

	private String entryType;

	private String entryPhase;
	
	public EntryDto() {
		
	}

	public EntryDto(Integer parentId, String title, String description, LocalDateTime date, LocalDateTime duration, LocalDateTime deadline,
					String entryType, String entryPhase) {
		this.parentId = parentId;
		this.title = title;
		this.description = description;
		this.date = date;
		this.duration = duration;
		this.deadline = deadline;
		this.entryType = entryType;
		this.entryPhase = entryPhase;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getDuration() {
		return duration;
	}

	public void setDuration(LocalDateTime duration) {
		this.duration = duration;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getEntryPhase() {
		return entryPhase;
	}

	public void setEntryPhase(String entryPhase) {
		this.entryPhase = entryPhase;
	}
	
	
}
