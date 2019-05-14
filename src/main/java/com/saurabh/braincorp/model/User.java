package com.saurabh.braincorp.model;

public class User {

	private String name;
	private long uid;
	private long gid;
	private String comment;
	private String home;
	private String shell;
	
	public User() {
		super();
	}

	public User(String name, long uid, long gid, String comment, String home, String shell) {
		super();
		this.name = name;
		this.uid = uid;
		this.gid = gid;
		this.comment = comment;
		this.home = home;
		this.shell = shell;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

}
