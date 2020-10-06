package com.calendar.responsedto;

import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class SortedEntryDto {

    private String title;

    private String parentTitle;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private LocalDateTime duration;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    @Enumerated(EnumType.STRING)
    private EntryPhase entryPhase;

    private boolean closed;

    public SortedEntryDto(String title, String parentTitle, LocalDateTime startDate, LocalDateTime duration,
                          LocalDateTime deadline, EntryType entryType, EntryPhase entryPhase, boolean closed) {
        this.title = title;
        this.parentTitle = parentTitle;
        this.startDate = startDate;
        this.duration = duration;
        this.deadline = deadline;
        this.entryType = entryType;
        this.entryPhase = entryPhase;
        this.closed = closed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
