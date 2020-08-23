package com.calendar.responsedto;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FullProjectResponseDto {

	private int id;
	
	private int userId;
	
	private String title;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime date;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime duration;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
	private LocalDateTime deadline;

	@Enumerated(EnumType.STRING)
	private EntryType entryType;

	@Enumerated(EnumType.STRING)
	private EntryPhase entryPhase;
	
	private boolean child;
	
	private boolean closed;
	
	private boolean deleted;
	
	private Integer sortNumber;

	private boolean expanded;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<Entry> childEntries = new HashSet<Entry>();
	
	public FullProjectResponseDto(
			int id, int userId, String title, String description, LocalDateTime date, LocalDateTime duration,
			LocalDateTime deadline, EntryType entryType, EntryPhase entryPhase, boolean child, boolean closed,
			boolean deleted, Integer sortNumber, boolean expanded, Set<Entry> childEntries) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.date = date;
		this.duration = duration;
		this.deadline = deadline;
		this.entryType = entryType;
		this.entryPhase = entryPhase;
		this.child = child;
		this.closed = closed;
		this.deleted = deleted;
		this.sortNumber = sortNumber;
		this.expanded = expanded;
		this.childEntries = childEntries;
	}
	

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.expanded = isExpanded;
	}

	public void setChildEntries(Set<Entry> childEntries) {
		this.childEntries = childEntries;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public EntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}

	public EntryPhase getEntryPhase() {
		return entryPhase;
	}

	public void setEntryPhase(EntryPhase entryPhase) {
		this.entryPhase = entryPhase;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public Set<Entry> getChildEntries() {
		return childEntries;
	}
	
}
