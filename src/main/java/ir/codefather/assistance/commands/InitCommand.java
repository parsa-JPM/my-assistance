package ir.codefather.assistance.commands;

import ir.codefather.assistance.command.core.BaseCommand;
import ir.codefather.assistance.command.core.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Command(name = "init", description = "it responsible to init Jpanel Configs")
public class InitCommand extends BaseCommand {
    @Override
    public void execute(String[] args) {
        createJpanelAlias();
    }


    /**
     * check alias exist and make it if it doesn't exist
     */
    private void createJpanelAlias() {
        Path path = Paths.get(System.getenv("HOME") + "/.bashrc");

        Boolean aliasExist = checkAliasExist(path);
        if (aliasExist)
            successMsg("jpanel alias already exists");
        else
            createAlias(path);

    }


    /**
     * it check that jpanel alias is exist or not
     *
     * @param path is path of bashrc
     * @return Boolean
     */
    private Boolean checkAliasExist(Path path) {
        try {
            List<String> bashrcLines = Files.readAllLines(path);
            return bashrcLines.stream()
                    .anyMatch((line) -> line.contains("alias") && line.contains("jpanel"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * add alias to the bashrc file
     *
     * @param path path of bashrc
     */
    private void createAlias(Path path) {
        try {
            String jpanelAlias = "alias jpanel='java -cp base/target/classes/:base/target/dependency/* ir.ir.codefather.base.command.core.Dispatcher'";
            Files.write(path, jpanelAlias.getBytes(), StandardOpenOption.APPEND);
            successMsg("jpanel alias create please don't forget restart your terminal :)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
