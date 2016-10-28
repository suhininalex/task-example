package org.progforge.taskexample;

import org.progforge.taskexample.model.Task;
import org.progforge.taskexample.service.TaskService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ConsoleInterface {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy hh:mm");

    private static final Scanner in = new Scanner(System.in);

    public void start(){
        menu_main();
    }

    private void menu_main(){
        while (true) {
            String command = readCommand();

            if (command.startsWith("exit")){
                System.exit(0);
            } else if (command.startsWith("list")){
                menu_list();
            } else if (command.startsWith("new")) {
                menu_new();
            } else {
                System.out.println("Unknown command. Please type");
                System.out.println("\tnew: to create a new task");
                System.out.println("\tlist: to show a task list");
                System.out.println("\texit: to exit");
                System.out.println();
            }
        }
    }

    private void menu_new(){
        try {
            System.out.println("Name[String]:");
            String name = readCommand();
            System.out.println("Priority[Integer]:");
            int priority = Integer.parseInt(readCommand());
            System.out.println("Deadline[dd.MM.yyyy hh:mm]:");
            Date deadline = dateFormat.parse(readCommand());

            TaskService.getInstance().createTask(name, deadline, priority);
            System.out.println("OK");
        } catch (ParseException | NumberFormatException e) {
            System.out.println("Bad value. Back to main menu.");
        }
        System.out.println();
    }

    private void menu_list(){
        while (true) {
            command_uncompleted();

            String command = readCommand();

            if (command.startsWith("back")){
                return;
            } else if (command.startsWith("complete")){
                command_setComplete(command);
            } else if (command.startsWith("show completed")){
                command_completed();
            } else {
                System.out.println("Unknown command. Please type");
                System.out.println("\tcomplete <id>: to set task completed");
                System.out.println("\tshow completed: to show all completed tasks");
                System.out.println("\tback: to go back to main menu");
                System.out.println();
            }
        }
    }

    private static void command_setComplete(String command){
        try {
            long id = Long.parseLong(command.split(" ")[1]);
            TaskService.getInstance().setCompleted(id);
            System.out.println("OK");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
            System.out.println("Bad command. Example: ");
            System.out.println("\tcomplete 1");
        }
        System.out.println();
    }

    private static void command_uncompleted(){
        System.out.format("%-6s %-25s %-20s %-10s %-5s\n", "ID", "Name", "Deadline", "Priority", "Expired");
        TaskService.getInstance().findAll().stream()
            .filter(it -> ! it.isDone())
            .forEach(it ->
                System.out.format(
                        "%-6d %-25s %-20s %-10s %-5s\n",
                        it.getId(), it.getName(), dateFormat.format(it.getDeadline()), it.getPriority(), getExpiredStatus(it)
                )
            );
        System.out.println();
    }

    private static void command_completed(){
        System.out.format("%-6s %-25s %-20s %-10s\n", "ID", "Name", "Deadline", "Priority");
        TaskService.getInstance().findAll().stream()
            .filter(Task::isDone)
            .forEach(it ->
                System.out.format(
                        "%-6d %-25s %-20s %-10s\n",
                        it.getId(), it.getName(), dateFormat.format(it.getDeadline()), it.getPriority()
                )
            );
        System.out.println();
    }

    private static String getExpiredStatus(Task task){
        return task.getDeadline().getTime() < new Date().getTime() ? "YES" : "NO";
    }

    private static String readCommand(){
        System.out.print("> ");
        return in.nextLine();
    }
}
