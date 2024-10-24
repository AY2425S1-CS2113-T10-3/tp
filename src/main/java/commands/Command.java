package commands;

import author.AuthorList;
import exceptions.TantouException;
import parser.Parser;
import ui.Ui;

import java.util.logging.Logger;

//@@author averageandyyy
public abstract class Command {
    protected Logger logger;
    private String command;
    private boolean isExit;


    public Command(String command) {
        this.command = command;
        this.isExit = false;
        this.logger = Logger.getLogger(command);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public abstract void execute(Ui ui, AuthorList authorList) throws TantouException;
}
