package com.example.restapi.model;

public class Greeting {
	private final long id;
	private String content;

	public Greeting() {
		id = 0;
		content = "xxx";
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}