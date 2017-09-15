package com.how2java.pojo;

import java.io.Serializable;

public class Hero implements Serializable{

	private String name;
	private int id;
	private String level;
	
	public Hero(int id,String name,String level)
	{
		this.id=id;
		this.name=name;
		this.level=level;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
