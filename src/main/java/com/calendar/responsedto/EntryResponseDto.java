package com.calendar.responsedto;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class EntryResponseDto {

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
	
	private Integer sortNumber;
	
	private boolean deleted;

	private boolean expanded;

	private Integer parentId;

	private Integer projectId;

	private String projectTitle;
	
	public EntryResponseDto(int id, int userId, String title, String description, LocalDateTime date,
							LocalDateTime duration, LocalDateTime deadline, EntryType entryType, EntryPhase entryPhase,
							boolean child, boolean closed, Integer sortNumber, boolean deleted, boolean expanded,
							Integer parentId, Integer projectId, String projectTitle) {
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
		this.setSortNumber(sortNumber);
		this.deleted = deleted;
		this.expanded = expanded;
		this.parentId = parentId;
		this.projectId = projectId;
		this.projectTitle = projectTitle;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.expanded = isExpanded;
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

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
}
