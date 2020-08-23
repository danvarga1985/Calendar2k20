package com.calendar.domain;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int userId;
	
	@NotNull
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

	@JsonBackReference
	@JoinColumn(name="entry_id")
	@ManyToOne
	private Entry parentEntry;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OrderBy("sortNumber ASC")
	@OneToMany(mappedBy = "parentEntry", cascade = CascadeType.REMOVE)
	private Set<Entry> childEntries;
	
	public Entry() {
		
	}

	public Entry(String title, String description, LocalDateTime date, LocalDateTime duration,
				 LocalDateTime deadline, EntryType entryType, EntryPhase entryPhase) {
		this.childEntries = new HashSet<Entry>();
		
		this.title = title;
		this.description = description;
		this.date = date;
		this.duration = duration;
		this.deadline = deadline;
		this.entryType = entryType;
		this.entryPhase = entryPhase;
		this.child = false;
		this.closed = false;
		this.deleted = false;
		this.expanded = true;
	}
	
	

	public Entry getParentEntry() {
		return parentEntry;
	}
	
	public void setParentEntry(Entry parentEntry) {
		this.parentEntry = parentEntry;
	}

	public Set<Entry> getChildEntries() {
		return childEntries;
	}

	public void setChildEntries(Set<Entry> childEntries) {
		this.childEntries = childEntries;
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
	    if (entryPhase == EntryPhase.COMPLETED) {
	        setClosed(true);
        }
		this.entryPhase = entryPhase;
	}
	
	public int getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


}
