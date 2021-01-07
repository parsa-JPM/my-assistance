package ir.codefather.assistance.command.core;

import com.jakewharton.fliptables.FlipTable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * this class dispatch command request to the correct Command class
 */
public class Dispatcher {

    private Map<Command, Class> commands;
    private Map<String, Class> commandNames;

    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.loadCommands();

        try {
            if (dispatcher.commandNames.containsKey(args[0])) {
                dispatcher.callCommand(args);
            } else
                dispatcher.usage();
        } catch (Exception e) {
//            if any bad things happen it will show usage
            dispatcher.usage();
//            e.printStackTrace();
        }
    }


    /**
     * this method load all of commands classes and put it to the map with command name
     */
    private void loadCommands() {
        //disable reflections log
//        Reflections.log = null;
        //todo make a better way to search of commands(not a static string)
        Reflections reflections = new Reflections("ir.codefather.assistance.commands");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Command.class);
        commands = new HashMap<>();
        commandNames = new HashMap<>();

        for (Class klass : classes) {
            Command command = (Command) klass.getAnnotation(Command.class);
            commands.put(command, klass);
            commandNames.put(command.name(), klass);
        }
    }


    /**
     * this methods show usage and show all command could we use
     */
    private void usage() {
        Set<Command> keys = commands.keySet();
        String[] headers = {"Command", "Description"};
        String[][] data = new String[keys.size()][2];

        int i = 0;
        int j = 0;
        for (Command command : keys) {
            data[i][j] = command.name();
            j++;
            data[i][j] = command.description();
            j--;
            i++;
        }
        System.out.println(BaseCommand.ANSI_BRIGHT_RED + "\n Command not found This is list of commands:");
        System.out.println(BaseCommand.ANSI_RESET);
        System.out.println(FlipTable.of(headers, data));
    }


    /**
     * this method call correct Command class dynamically
     *
     * @param args command line arguments
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private void callCommand(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, CommandException {
        Class klass = commandNames.get(args[0]);
        BaseCommand baseCommand = (BaseCommand) klass.getConstructor().newInstance();
        baseCommand.execute(args);
    }
}
