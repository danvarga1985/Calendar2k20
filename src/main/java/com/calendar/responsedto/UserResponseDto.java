package com.calendar.responsedto;

public class UserResponseDto {
	
	private int id;
    
    private String email;
    
    private boolean validated;

    private boolean onlyActiveProjects;
    
    public UserResponseDto() {
    	
    }

    public UserResponseDto(int id, String email, boolean validated, boolean onlyActiveProjects) {
        this.id = id;
        this.email = email;
        this.validated = validated;
        this.onlyActiveProjects = onlyActiveProjects;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isOnlyActiveProjects() {
        return onlyActiveProjects;
    }

    public void setOnlyActiveProjects(boolean onlyActiveProjects) {
        this.onlyActiveProjects = onlyActiveProjects;
    }
}
