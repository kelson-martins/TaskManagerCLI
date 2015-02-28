package br.com.kesmarti.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.kesmarti.bean.Task;
import br.com.kesmarti.dao.ITaskDao;

public class TaskJdbcDao implements ITaskDao {

	@Override
	public void insert(Task task) {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionHelper.getConnection();
			String sql = "INSERT INTO task (taskName,createdDate) VALUES (?,?)";
			statement = connection.prepareStatement(sql);
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy/MM/dddd HH:mm:ssss");
			Date date = new Date();
			statement.setString(1, task.getTaskDescription());
			statement.setString(2, sdf.format(date).toString());
			statement.executeUpdate();

		} catch (Exception e) {
			throw new RuntimeException(
					"Error inserting task." + e.getMessage(), e);
		} finally {
			releaseDatabaseResources(statement, connection);
		}

	}

	public void releaseDatabaseResources(Statement statement,
			Connection connection) {
		try {
			statement.close();
		} catch (Exception e) {
		}

		try {
			connection.close();
		} catch (Exception e) {
		}
	}

	@Override
	public List<Task> list() {
		List<Task> tasks = new ArrayList<Task>();
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionHelper.getConnection();
			statement = connection.prepareStatement("SELECT * FROM task");
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Task task = new Task();
				task.setId(rs.getLong("idTask"));
				task.setTaskDescription(rs.getString("taskName"));
				task.setTaskDate(rs.getDate("createdDate"));

				tasks.add(task);
			}
		} catch (SQLException e) {
			System.out.println("Error listing the tasks." + e.getMessage());
		} finally {
			releaseDatabaseResources(statement, connection);
		}

		return tasks;
	}

	@Override
	public void delete(Task task) {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionHelper.getConnection();
			statement = connection
					.prepareStatement("DELETE FROM task WHERE idTask = ?");
			statement.setLong(1, task.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Error deleting contact.", e);
		} finally {
			releaseDatabaseResources(statement, connection);
		}
	}

	@Override
	public List<Task> searchTask(String taskName) {
		List<Task> taskList = new ArrayList<Task>();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionHelper.getConnection();
			statement = connection
					.prepareStatement("SELECT * from task WHERE taskName LIKE ?");
			statement.setString(1, "%" + taskName + "%");

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Task task = new Task();
				task.setId(rs.getLong("idTask"));
				task.setTaskDescription(rs.getString("taskName"));
				task.setTaskDate(rs.getDate("createdDate"));

				taskList.add(task);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			releaseDatabaseResources(statement, connection);
		}
		return taskList;
	}

	@Override
	public boolean verificaExistenciaTask(long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		Boolean existe = false;
		try {
			connection = ConnectionHelper.getConnection();
			statement = connection.prepareStatement("SELECT * FROM task WHERE idTask = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			if (rs.next())
				existe = true;
		} catch (Exception e) {

		} finally {
			releaseDatabaseResources(statement, connection);
		}

		return existe;
	}

	@Override
	public void updateTask(Task task) {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			String sql = "UPDATE task SET taskName = ? WHERE idTask = ?";

			connection = ConnectionHelper.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, task.getTaskDescription());
			statement.setLong(2, task.getId());
			
			statement.executeUpdate();
		} catch (Exception e) {

		} finally {
			releaseDatabaseResources(statement, connection);
		}

	}

	@Override
	public Task getTask(long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		Task task = new Task();
		try {
			connection = ConnectionHelper.getConnection();
			statement = connection.prepareStatement("SELECT * FROM task WHERE idTask = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				task.setId(rs.getLong("idTask"));
				task.setTaskDescription(rs.getString("taskName"));
				task.setTaskDate(rs.getDate("createdDate"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return task;
	}

}
