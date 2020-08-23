package com.calendar.responsedto;

public class ProjectEntriesResponseDto {
	
	private int entryId;
	
	private String title;
	
	private boolean closed;
	
	private Integer sortNumber;
	
	public ProjectEntriesResponseDto(int entryId, String title, boolean closed, Integer sortNumber) {
		this.entryId = entryId;
		this.title = title;
		this.closed = closed;
		this.sortNumber = sortNumber;
	}
	
	

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
