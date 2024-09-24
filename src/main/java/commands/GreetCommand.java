package commands;

import author.AuthorList;
import ui.Ui;

public class GreetCommand extends Command {
    public GreetCommand() {
        super("hi");
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) {
        ui.greetUser();
    }
}
