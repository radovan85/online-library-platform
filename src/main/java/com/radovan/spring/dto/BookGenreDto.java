package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class BookGenreDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String description;
	private List<Integer> booksIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Integer> getBooksIds() {
		return booksIds;
	}
	public void setBooksIds(List<Integer> booksIds) {
		this.booksIds = booksIds;
	}
	@Override
	public String toString() {
		return "BookGenreDto [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
	

}
