package ir.codefather.assistance.command.core;

public abstract class BaseCommand {
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_BRIGHT_RED = "\u001B[91m";
    static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    static final String ANSI_BRIGHT_CYAN = "\u001B[96m";


    /**
     * it's brain your command your logic goes here
     *
     * @param args these same main arguments
     */
    public abstract void execute(String[] args) throws CommandException;


    /**
     * it provide message with greed color
     *
     * @param msg your message
     */
    protected void successMsg(String msg) {
        System.out.println(BaseCommand.ANSI_BRIGHT_GREEN);
        System.out.println(msg);
        System.out.println(BaseCommand.ANSI_RESET);
    }

    /**
     * it provide message with red color
     *
     * @param msg your message
     */
    protected void failedMsg(String msg) {
        System.out.println(BaseCommand.ANSI_BRIGHT_RED);
        System.out.println(msg);
        System.out.println(BaseCommand.ANSI_RESET);
    }


    /**
     * change font color to red
     */
    public void redTerminal() {
        System.out.println(ANSI_BRIGHT_RED);
    }


    /**
     * change font color to green
     */
    public void greenTerminal() {
        System.out.println(ANSI_BRIGHT_GREEN);
    }

    /**
     * reset terminal color to default
     */
    public void resetTerminal() {
        System.out.println(BaseCommand.ANSI_RESET);
    }

}
