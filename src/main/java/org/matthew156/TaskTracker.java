/**
 * TaskTracker application - A command-line task management tool.
 *
 * This application allows users to create, update, delete and track tasks with different statuses
 * (to do, in-progress, done). Tasks are stored in a JSON file specified by the FILE_PATH constant.)
 *
 * @author Matthew156
 * @version 1.0
 */
package org.matthew156;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.json.JSONObject;
import org.matthew156.model.Task;
import org.matthew156.utils.Status;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import static org.matthew156.utils.Constants.FILE_PATH;

/**
 * Main entry point for the TaskTracker application.
 * This class defines the top-level command and delegates to subcommands.
 */
@Command(name = "task-cli", mixinStandardHelpOptions = true, version = "task-cli 1.0",
        description = "Accepts user actions and inputs as a argument to store in a JSON file",
        subcommands = {Add.class, Update.class, Delete.class, ListItems.class, MarkInProgress.class, MarkDone.class})
class TaskTracker{

    /**
     * Main method that initializes the command-line interface.
     * Uses Picocli to parse command-line arguments and execute the appropriate command.
     *
     * @param args Command-line arguments passed by the user
     */
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new TaskTracker());
        cmd.execute(args);
    }
}

/**
 * Command class for adding a new task to the task tracker.
 * Handles the 'add' command and processes the task description provided by the user.
 *
 * Example usage: {@code taskcli add "Complete project report"}
 */
@Command(name = "add", description = "adds a task to the list")
class Add implements Callable<Integer>{

    /**
     * The description of the task to be added.
     */
    @Parameters(index = "0", description = "description of task" )
    private String description;

    /**
     * Adds a new task with the specified description to the task list.
     * Creates the tasks file if it doesn't exist.
     *
     * @throws IOException If there is an error accessing or creating the file
     */
    public void addTask() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()){
            file.createNewFile();
        }
        writeFile(file,FILE_PATH);
    }

    /**
     * Writes the task data to the specified JSON file.
     * Creates a new Task object with the provided description and current timestamp,
     * assigns a random UUID, and adds it to the JSON file.
     *
     * @param file The file to write to
     * @param filepath The path of the file
     * @throws IOException If there is an error writing to the file
     */
    public void writeFile(File file, String filepath) throws IOException {
        Task task = new Task(description, new StdDateFormat().format(Date.from(Instant.now())),
                new StdDateFormat().format(Date.from(Instant.now())));
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
        JSONObject jsonObject = jsonString.equals("")? new JSONObject() : new JSONObject(jsonString);
        FileWriter writer = new FileWriter(file);
        task.setId(UUID.randomUUID());
        JSONObject taskObject = new JSONObject(mapper.writeValueAsString(task));
        jsonObject.put(String.valueOf(task.getId()), taskObject);
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        System.out.println("Task added successfully (ID: " +task.getId() + ")");
    }

    /**
     * Executes the command by calling the addTask method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        addTask();
        return 0;
    }
}

/**
 * Command class for updating an existing task.
 * Allows modification of a task's description by specifying its UUID.
 *
 * <p>Example usage: {@code taskcli update <task-id> "Updated description"}</p>
 */
@Command(name = "update", description = "updates a task to the list by taking an id and description")
class Update implements Callable<Integer>{

    /**
     * The unique identifier of the task to update.
     */
    @Parameters(index = "0", description = "id of the task being updated")
    private UUID id;

    /**
     * The new description to assign to the task.
     */
    @Parameters(index = "1", description = "description of task" )
    private String description;

    /**
     * Updates the description of the specified task.
     * Updates the 'updatedAt' timestamp to the current time.
     *
     * @throws IOException If there is an error accessing the file
     */
    public void updateTask() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli update <id> <description>");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});
            Task task = map.get(id);
            task.setDescription(description);
            task.setUpdatedAt(new StdDateFormat().format(Date.from(Instant.now())));
            map.put(id, task);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("Updated ID: " + id);
        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }
    }

    /**
     * Executes the command by calling the updateTask method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        updateTask();
        return 0;
    }
}

/**
 * Command class for deleting a task.
 * Removes a task from the tracker by specifying its UUID.
 *
 * <p>Example usage: {@code taskcli delete <task-id>}</p>
 */
@Command(name = "delete", description = "deletes a task to the list by taking an id")
class Delete implements Callable<Integer>{

    /**
     * The unique identifier of the task to delete.
     */
    @Parameters(index = "0", description = "id of the task being updated")
    private UUID id;

    /**
     * Deletes the specified task from the task tracker.
     *
     * @throws IOException If there is an error accessing the file
     */
    public void deleteTask() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli delete <id> <description>");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});
            map.remove(id);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("ID: " + id + " deleted.");
        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }
    }

    /**
     * Executes the command by calling the deleteTask method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        deleteTask();
        return 0;
    }
}

/**
 * Command class for marking a task as in-progress.
 * Changes the status of a task to "IN_PROGRESS" by specifying its UUID.
 *
 * <p>Example usage: {@code taskcli mark-in-progress <task-id>}</p>
 */
@Command(name = "mark-in-progress", description = "shows the list of tasks created")
class MarkInProgress implements Callable<Integer>{

    /**
     * The unique identifier of the task to mark as in-progress.
     */
    @Parameters(index = "0", description = "id of the task being marked in progress")
    UUID id;

    /**
     * Executes the command by creating a MarkStatus helper and calling its updateStatus method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        MarkStatus markStatus = new MarkStatus(Status.IN_PROGRESS.getName(), id);
        markStatus.updateStatus();
        return 0;
    }
}

/**
 * Helper class for changing the status of a task.
 * Used by both MarkInProgress and MarkDone commands to update task status.
 */
class MarkStatus {

    /**
     * The status to set for the task.
     */
    private String status;

    /**
     * The unique identifier of the task to update.
     */
    private UUID id;

    /**
     * Constructs a new MarkStatus with the specified status and task ID.
     *
     * @param status The status to set ("IN_PROGRESS", "DONE", or "TODO")
     * @param id The unique identifier of the task
     */
    public MarkStatus(String status, UUID id) {
        this.status = status;
        this.id = id;
    }

    /**
     * Updates the status of the specified task.
     * Updates the 'updatedAt' timestamp to the current time.
     *
     * @throws IOException If there is an error accessing the file
     */
    public void updateStatus() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File is either empty or missing task. Please add a task to update....");
            System.out.println("\n Example usage: taskcli list");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task> map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>() {});
            Task task = map.get(id);
            task.setStatus(status);
            task.setUpdatedAt(new StdDateFormat().format(Date.from(Instant.now())));
            map.put(id, task);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("Marked ID: " + id + " as " + status);
        } catch (Exception e) {
            System.out.println("Exception Thrown: " + e.getMessage() + "\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }
    }
}

/**
 * Command class for marking a task as done.
 * Changes the status of a task to "DONE" by specifying its UUID.
 *
 * <p>Example usage: {@code taskcli mark-done <task-id>}</p>
 */
@Command(name = "mark-done", description = "shows the list of tasks created")
class MarkDone implements Callable<Integer>{

    /**
     * The unique identifier of the task to mark as done.
     */
    @Parameters(index = "0", description = "id of the task being marked in progress")
    UUID id;

    /**
     * Executes the command by creating a MarkStatus helper and calling its updateStatus method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        MarkStatus markStatus = new MarkStatus(Status.DONE.getName(), id);
        markStatus.updateStatus();
        return 0;
    }
}

/**
 * Command class for listing all tasks.
 * Displays all tasks in the task tracker, or can filter tasks by status using subcommands.
 *
 * <p>Example usage:
 * <ul>
 *   <li>{@code taskcli list} - Lists all tasks</li>
 *   <li>{@code taskcli list to do} - Lists only to do tasks</li>
 *   <li>{@code taskcli list in-progress} - Lists only in-progress tasks</li>
 *   <li>{@code taskcli list done} - Lists only completed tasks</li>
 * </ul>
 * </p>
 */
@Command(name = "list", description = "shows the list of tasks created",
        subcommands = {ListItemsByDone.class, ListItemsByInProgress.class, ListItemsByTodo.class})
class ListItems implements Callable<Integer>{

    /**
     * Lists all tasks in the task tracker, regardless of status.
     *
     * @throws IOException If there is an error accessing the file
     */
    public void listItems() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli list");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});

            List<Map.Entry<UUID, Task>> list = new ArrayList<>(map.entrySet());
            for (Map.Entry<UUID, Task> entry : list){
                System.out.println(entry.getKey() + ": " + mapper.writeValueAsString(entry.getValue()));
            }
        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }
    }

    /**
     * Executes the command by calling the listItems method.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        listItems();
        return 0;
    }
}

/**
 * Subcommand class for listing only in-progress tasks.
 *
 * <p>Example usage: {@code taskcli list in-progress}</p>
 */
@Command(name = "in-progress", description = "shows the list of tasks marked in-progress")
class ListItemsByInProgress implements Callable<Integer>{

    /**
     * Executes the command by creating a ListItemsByStatus helper with the IN_PROGRESS status.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.IN_PROGRESS.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }
}

/**
 * Subcommand class for listing only completed tasks.
 *
 * <p>Example usage: {@code taskcli list done}</p>
 */
@Command(name = "done", description = "shows the list of tasks marked done")
class ListItemsByDone implements Callable<Integer>{

    /**
     * Executes the command by creating a ListItemsByStatus helper with the DONE status.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.DONE.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }
}

/**
 * Subcommand class for listing only to do tasks.
 *
 * Example usage: {@code taskcli list to do}
 */
@Command(name = "todo", description = "shows the list of tasks marked todo")
class ListItemsByTodo implements Callable<Integer>{

    /**
     * Executes the command by creating a ListItemsByStatus helper with the TODO status.
     *
     * @return 0 to indicate successful execution
     * @throws Exception If there is an error during execution
     */
    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.TODO.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }
}

/**
 * Helper class for filtering and listing tasks by their status.
 * Used by the ListItemsByTodo, ListItemsByInProgress, and ListItemsByDone subcommands.
 */
class ListItemsByStatus{

    /**
     * The status to filter tasks by.
     */
    private String status;

    /**
     * Constructs a new ListItemsByStatus with the specified status.
     *
     * @param status The status to filter by ("IN_PROGRESS", "DONE", or "TODO")
     */
    public ListItemsByStatus(String status) {
        this.status = status;
    }

    /**
     * Lists only the tasks with the specified status.
     *
     * @throws IOException If there is an error accessing the file
     */
    public void listItemsByStatus() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli list");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task> map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>() {});

            List<Map.Entry<UUID, Task>> list = new ArrayList<>(map.entrySet());
            List<Map.Entry<UUID, Task>> resultList = new ArrayList<>();
            for (Map.Entry<UUID, Task> entry : list) {
                if (entry.getValue().getStatus().equals(status))
                    resultList.add(entry);
            }
            if(resultList.isEmpty()) {
                System.out.println("There are no items for " + status + " status");
            }
            for (Map.Entry<UUID, Task> entry : resultList) {
                System.out.println(entry.getKey() + ": " + mapper.writeValueAsString(entry.getValue()));
            }
        } catch (Exception e) {
            System.out.println("Exception Thrown: " + e.getMessage() + "\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }
    }
}