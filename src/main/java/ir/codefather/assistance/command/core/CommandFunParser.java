package ir.codefather.assistance.command.core;


import com.jakewharton.fliptables.FlipTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * this class responsible to get BaseCommand class and with its annotation (@CommandFun)
 * parse it and call proper methods to handle request and if it doesn't parse it print usage of BaseCommand class
 */
public class CommandFunParser {
    /**
     * this use for get annotation info and get command functions
     */
    private Class klass;
    /**
     * this use for invoking command function because we expect a object for invoking
     */
    private BaseCommand commandObj;
    /**
     * this is args of command we process them
     */
    private List<String> args;
    /**
     * this keeps parameter of command function that are retrieved by CommandFun and Flags Annotations
     */
    private List<CommandParameter> parameters = new ArrayList<>();
    /**
     * this is map of command function name to its method object and use for invoking
     */
    private Map<String, Method> methodMap = new HashMap<>();

    /**
     * @param command command object to retrieve class object and use itself to invoke method
     * @return CommandFunParser
     */
    public static CommandFunParser build(BaseCommand command) {
        CommandFunParser parser = new CommandFunParser();
        parser.klass = command.getClass();
        parser.commandObj = command;
        return parser;
    }

    public CommandFunParser setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public void parse() {
        Method[] methods = klass.getMethods();
        Arrays.stream(methods).forEach(this::fetchCommandClassDetail);
        if (args.size() > 0)
            findMethod();
        else {
            commandObj.failedMsg("It doesn't work with nothing :)");
            usage();
        }

    }

    /**
     * generate usage of parameters of a command class
     */
    private void usage() {
        String[] headers = {"Functions name", "Description", "Flags"};
        String[][] data = new String[parameters.size()][3];
        int i = 0;
        int j = 0;
        for (CommandParameter parameter : parameters) {
            data[i][j] = parameter.getName();
            j++;
            data[i][j] = parameter.getDescription();
            j++;
            data[i][j] = "";
            for (CommandFlag flag : parameter.getFlags()) {
                data[i][j] += flag.getName() + ": " + flag.getDescription() + "\n";
            }
            j = 0;
            i++;
        }
        System.out.println(FlipTable.of(headers, data));
        System.exit(1);
    }

    /**
     * analyze methods of class and make usage of command class
     */
    private void fetchCommandClassDetail(Method method) {
        if (!method.isAnnotationPresent(CommandFun.class)) {
            return;
        }

        CommandFun commandFun = method.getAnnotation(CommandFun.class);
        CommandParameter parameter = new CommandParameter();
        methodMap.put(commandFun.name(), method);
        parameter.setName(commandFun.name());
        parameter.setDescription(commandFun.description());
        parameter.setWithValue(commandFun.withValue());
        parameter.setFlags(fetchFlags(method));
        parameters.add(parameter);

    }


    /**
     * try to fetch flags if exist in method
     *
     * @param method method object to find Flags annotation
     * @return CommandParameter
     */
    private List<CommandFlag> fetchFlags(Method method) {
        if (!method.isAnnotationPresent(Flags.class)) {
            return new ArrayList<>();
        }

        Flags flags = method.getAnnotation(Flags.class);
        List<CommandFlag> commandFlags = new ArrayList<>();

        for (Flag flag : flags.flags()) {
            CommandFlag commandFlag = new CommandFlag();
            commandFlag.setName(flag.name());
            commandFlag.setDescription(flag.description());
            commandFlag.setWithValue(flag.withValue());
            commandFlags.add(commandFlag);
        }

        return commandFlags;
    }


    /**
     * find command function among args
     */
    private void findMethod() {
        boolean ParameterExistflag = false;
        for (CommandParameter parameter : parameters) {
            if (args.get(0).equals(parameter.getName())) {
                CommandParameter inputCommandFun = parseCommandParameter(parameter);
                invokeCommandFunction(inputCommandFun);
                ParameterExistflag = true;
                break;
            }
        }

        if (!ParameterExistflag) {
            commandObj.failedMsg("This parameter not found use below functions");
            usage();
        }
    }


    /**
     * this get a command parameter and try to retrieve its data from terminal and set value of command parameter and
     * set value of its flags if exist. if any error happen it show usage of parameter
     *
     * @param parameter command parameter to retrieve its data from terminal
     * @return CommandParameter
     */
    private CommandParameter parseCommandParameter(CommandParameter parameter) {
        int i = 1;
        if (parameter.isWithValue()) {
            setCommandParameterValue(parameter, i);
            i++;
        }

        List<CommandFlag> existFlags = new ArrayList<>();
        for (int j = i; j < args.size(); j++) {

            int beforeFlagsSize = existFlags.size();

            j = checkFlagExist(parameter, existFlags, j);


            // check that arg is trash or mistake
            if (beforeFlagsSize == existFlags.size()) {
                commandObj.failedMsg("You use unexpected element (maybe use flag with typo)");
                usage();
            }

        }

        parameter.setFlags(existFlags);

        return parameter;
    }


    /**
     * this get index of specific arg and check it that it's a flag or not
     *
     * @param parameter  command parameter to get flags
     * @param existFlags list of flags that exist in terminal
     * @param index      index of arg
     * @return int if flag has value we have to increase index to get its value then return correct index
     */
    private int checkFlagExist(CommandParameter parameter, List<CommandFlag> existFlags, int index) {
        for (CommandFlag flag : parameter.getFlags()) {
            String flagName = flag.getName();
            if (flagName.equals(args.get(index))) {
                index = checkFlagWithValue(flag, index);
                existFlags.add(flag);
                break;
            }
        }
        return index;
    }

    /**
     * check if flag has value increase index and get it and set it to the Command flag
     *
     * @param flag
     * @param index
     * @return int
     */
    private int checkFlagWithValue(CommandFlag flag, int index) {
        if (flag.isWithValue()) {
            index++;
            setFlagValue(flag, index);
        }

        return index;
    }

    /**
     * set value for flag and show usage if that index of arg doesn't exist
     *
     * @param flag
     * @param index
     */
    private void setFlagValue(CommandFlag flag, int index) {
        if (hasArg(index))
            flag.setValue(args.get(index));
        else {
            commandObj.failedMsg("This flag need value please give it");
            usage();
        }
    }

    /**
     * set value of a command parameter
     *
     * @param parameter CommandParameter to set its value
     * @param index     index of args
     */
    private void setCommandParameterValue(CommandParameter parameter, int index) {
        if (hasArg(index)) {
            String value = args.get(index);
            if (value.startsWith("--") || value.startsWith("-")) {
                commandObj.failedMsg("Parameter value must not start with '-' or '--'");
                usage();
            }
            parameter.setValue(value);
        } else {
            commandObj.failedMsg("This parameter need value please give it");
            usage();
        }
    }

    /**
     * invoke command function method and run  command :)
     *
     * @param parameter it means command function input that involves detail of command (name , value , flags and their value)
     */
    private void invokeCommandFunction(CommandParameter parameter) {
        try {
            String commandParameterName = parameter.getName();
            Method commandFun = methodMap.get(commandParameterName);
            commandFun.invoke(commandObj, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * check arg index exists
     *
     * @param index index of args
     * @return boolean
     */
    private boolean hasArg(int index) {
        return index < args.size();
    }
}
