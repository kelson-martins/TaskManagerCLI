/**
 * 
 */
package br.com.kesmarti.bean;

import java.util.Date;


/**
 * @author Kelson Martins
 * 
 */
public class Task {
	private long id;
	private String taskDescription;
	private Date taskDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	@Override
	public String toString() {
		return("Id: " + getId() + " Task: " + getTaskDescription() + " Date: " + getTaskDate());
	}
	
	
}
