package com.calendar.requestdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryForModificationDto {

    private Optional<String> title;

    private Optional<String> description;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Optional<LocalDateTime> date;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Optional<LocalDateTime> duration;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Optional<LocalDateTime> deadline;

    private Optional<String> entryType;

    private Optional<String> entryPhase;

    private Optional<Boolean> closed;

    private Optional<Boolean> expanded;

    public EntryForModificationDto() {

    }

    public EntryForModificationDto(Optional<String> title, Optional<String> description, Optional<LocalDateTime> date,
								   Optional<LocalDateTime> duration, Optional<LocalDateTime> deadline,
								   Optional<String> entryType, Optional<String> entryPhase, Optional<Boolean> closed, Optional<Boolean> expanded) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.deadline = deadline;
        this.entryType = entryType;
        this.entryPhase = entryPhase;
        this.closed = closed;
		this.expanded = expanded;
	}

	public Optional<String> getTitle() {
		return title;
	}

	public void setTitle(Optional<String> title) {
		this.title = title;
	}

	public Optional<String> getDescription() {
		return description;
	}

	public void setDescription(Optional<String> description) {
		this.description = description;
	}

	public Optional<LocalDateTime> getDate() {
		return date;
	}

	public void setDate(Optional<LocalDateTime> date) {
		this.date = date;
	}

	public Optional<LocalDateTime> getDuration() {
		return duration;
	}

	public void setDuration(Optional<LocalDateTime> duration) {
		this.duration = duration;
	}

	public Optional<LocalDateTime> getDeadline() {
		return deadline;
	}

	public void setDeadline(Optional<LocalDateTime> deadline) {
		this.deadline = deadline;
	}

	public Optional<String> getEntryType() {
		return entryType;
	}

	public void setEntryType(Optional<String> entryType) {
		this.entryType = entryType;
	}

	public Optional<String> getEntryPhase() {
		return entryPhase;
	}

	public void setEntryPhase(Optional<String> entryPhase) {
		this.entryPhase = entryPhase;
	}

	public Optional<Boolean> isClosed() {
		return closed;
	}

	public void setClosed(Optional<Boolean> closed) {
		this.closed = closed;
	}

	public Optional<Boolean> isExpanded() {
		return expanded;
	}

	public void setExpanded(Optional<Boolean> expanded) {
		this.expanded = expanded;
	}
}
