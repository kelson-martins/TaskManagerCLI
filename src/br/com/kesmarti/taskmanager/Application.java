package br.com.kesmarti.taskmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.com.kesmarti.bean.Task;
import br.com.kesmarti.dao.ITaskDao;
import br.com.kesmarti.dao.jdbc.TaskJdbcDao;

public class Application {

	private static ITaskDao taskDao = new TaskJdbcDao();

	private static final String COMMAND_QUIT = "quit";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_LIST = "list";
	private static final String COMMAND_CREATE = "create";
	private static final String COMMAND_HELP = "help";
	private static final String COMMAND_REMOVE = "remove";
	private static final String COMMAND_UPDATE = "update";
	private static final String COMMAND_GET = "get";
	private boolean running = true;
	private Options options = new Options();
	static Scanner leitor = new Scanner(System.in);

	public Application() {
		options.addOption("s", COMMAND_SEARCH, false, "Search a contact");
		options.addOption("h", COMMAND_HELP, false, "Shows this list");
		options.addOption("q", COMMAND_QUIT, false, "Quits the application");
		options.addOption("l", COMMAND_LIST, false, "Lists all contacts");
		options.addOption("c", COMMAND_CREATE, false, "Creates a new contact");
		options.addOption(OptionBuilder
				.withArgName("contactId")
				.hasArgs()
				.withDescription("Removes the contact specified by <contactId>")
				.withLongOpt(COMMAND_REMOVE).create("r"));
		options.addOption(OptionBuilder
				.withArgName("contactId")
				.hasArgs()
				.withDescription("Updates the contact specified by <contactId>")
				.withLongOpt(COMMAND_UPDATE).create("u"));
		options.addOption(OptionBuilder
				.withArgName("contactId")
				.hasArgs()
				.withDescription("Get the contact specified by <contactId>")
				.withLongOpt(COMMAND_GET).create("g"));
	}

	public void start() {
		help();
		while (running) {
			processCommand(getUserInput().split(" "));
		}
	}

	private String getUserInput(String prefix) {
		System.out.print(prefix + ": ");
		String result = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			result = reader.readLine().trim();
		} catch (Exception e) {
		}
		return result;
	}

	private String getUserInput() {
		return getUserInput("[My Tasks]");
	}

	private void processCommand(String[] args) {
		CommandLineParser parser = new BasicParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption(COMMAND_HELP)) {
				help();
			}
			if (line.hasOption(COMMAND_QUIT)) {
				quit();
			}
			if (line.hasOption(COMMAND_CREATE)) {
				insertTask();
			}
			if (line.hasOption(COMMAND_LIST)) {
				listTasks();
			}
			if (line.hasOption(COMMAND_SEARCH)) {
				search();
			}
			if (line.hasOption(COMMAND_REMOVE)) {
				try {
					removeTask(Long.parseLong(line
							.getOptionValue(COMMAND_REMOVE)));
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID");
				}

			}
			if (line.hasOption(COMMAND_UPDATE)) {
				try {
					updateTask(Long.parseLong(line
							.getOptionValue(COMMAND_UPDATE)));
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID");
				}

			}
			if (line.hasOption(COMMAND_GET)) {
				try {
					getTask(Long.parseLong(line
							.getOptionValue(COMMAND_GET)));
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID");
				}

			}

		} catch (ParseException e) {
			System.out.println("Unexpected exception:" + e.getMessage());
		}

	}

	private void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("My Tasks", options);
	}

	private void quit() {
		System.out.println("Quitting application...");
		running = false;
	}

	public static void main(String[] args) {
		new Application().start();
	}

	public static void listTasks() {
		List<Task> taskList = taskDao.list();

		Iterator<Task> cursor = taskList.iterator();
		while (cursor.hasNext()) {
			Task item = (Task) cursor.next();
			System.out.println(item);
		}
	}

	private static void insertTask() {
		Task task = new Task();
		Scanner leitor = new Scanner(System.in);

		System.out.println("Digite o nome da tarefa: ");
		task.setTaskDescription(leitor.nextLine());
		taskDao.insert(task);
	}

	public static void removeTask(long id) {
		Task task = new Task();
		task.setId(id);
		if (taskDao.verificaExistenciaTask(id)) {
			taskDao.delete(task);
			System.out.println("Task removed");
		} else {
			System.out.println("Task doesnt exists");
		}

	}

	public static void search() {
		System.out.print("Digite o nome da tarefa: ");
		String taskName = leitor.nextLine();
		List<Task> taskList = taskDao.searchTask(taskName);
		Iterator<Task> cursor = taskList.iterator();
		while (cursor.hasNext()) {
			Task item = (Task) cursor.next();
			System.out.println(item);
		}
	}

	public void updateTask(Long id) {
		Task task = new Task();
		task.setId(id);

		if (taskDao.verificaExistenciaTask(id)) {
			System.out.println("Digite a descrição da Task: ");
			task.setTaskDescription(leitor.nextLine());

			taskDao.updateTask(task);
			System.out.println("Task updated");
		} else {
			System.out.println("Task doesnt exists");
		}

	}
	
	public void getTask(Long id) {
		if (taskDao.verificaExistenciaTask(id)) {
			Task task = taskDao.getTask(id);
			System.out.println(task.toString());
		} else {
			System.out.println("Task doesnt exists");
		}
		
	}
}
