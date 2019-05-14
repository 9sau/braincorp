package com.saurabh.braincorp.model;

import java.util.List;

public class Group {

	private String name;
	private Long gid;
	private List<String> members;

	
	public Group() {
		super();
	}

	public Group(String name, Long gid, List<String> members) {
		super();
		this.name = name;
		this.gid = gid;
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

}
