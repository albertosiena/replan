package eu.supersede.fe.dto;

import java.util.Set;

public class NewResourceData {
	
	private String code;
	private String description;
	private Set<Skill> skills;
	private float availability; // (0 .. 100)
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<Skill> getSkills() {
		return skills;
	}
	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}
	public float getAvailability() {
		return availability;
	}
	public void setAvailability(float availability) {
		this.availability = availability;
	}
	
	
}
