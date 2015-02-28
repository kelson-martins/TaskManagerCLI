package br.com.kesmarti.dao;

import java.util.List;

import br.com.kesmarti.bean.Task;

public interface ITaskDao {

	public void insert(Task task);
	
	public List<Task> list();
	
	public void delete(Task task);
	
	public List<Task> searchTask(String taskName);
	
	public boolean verificaExistenciaTask(long id);
	
	public void updateTask(Task task);
	
	public Task getTask(long id);

}
