package ir.codefather.assistance.commands;

import com.google.common.collect.Lists;
import ir.codefather.assistance.command.core.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Command(name = "module", description = "module operation will done with it")
public class ModuleCommand extends BaseCommand {

    private List<String> notFoundModule = new ArrayList<>();

    @Override
    public void execute(String[] args) {
        List<String> argList = Lists.newArrayList(args);
        argList.remove(0);
        CommandFunParser.build(this).setArgs(argList).parse();
    }


    /**
     * try to disable modules that given by terminal
     *
     * @param parameter detail parameter value and flags in terminal
     */
    @CommandFun(name = "disable", description = "This can be a module name or names(comma separated) to disable")
    @Flags(flags = {
            @Flag(name = "--r", description = "disable also submodules"),
    })
    public void disable(CommandParameter parameter) {
        String[] modules = parameter.getValue().split(",");
        removeModulesFromRoot(modules);
        notFoundModule.forEach((x) -> failedMsg("Module " + x + " not found"));
        if (parameter.hasFlag("--r"))
            Arrays.stream(modules).forEach(this::disableChildModules);
    }


    @CommandFun(name = "enable", description = "This can be a module name or names(comma separated) to enable")
    public void enable(CommandParameter parameter) {
        successMsg("enable this modules " + parameter.getValue());
    }


    /**
     * looking for modules in the root pom.xml
     *
     * @param modules list of module that must be disable
     */
    private void removeModulesFromRoot(String[] modules) {

    }

    /**
     * remove module from root pom.xml if that exist
     *
     * @param enableModules list of modules that were in <modules/> tag
     * @param module name of module that must be removed
     *
     * @return boolean that demonstrate module was in pom.xml or not
     */
    private boolean findAndRemoveModuleFromXml(NodeList enableModules, String module) {
        for (int i = 0; i < enableModules.getLength(); i++) {
            Node enableModule = enableModules.item(i);
            if (enableModule.getTextContent().equals(module)) {
                enableModule.getParentNode().removeChild(enableModule);
                successMsg("Module " + module + " successfully disabled");
                return true;
            }
        }

        return false;
    }

    /**
     * try to disable modules that used given module
     *
     * @param module parent module
     */
    private void disableChildModules(String module) {
        if (notFoundModule.contains(module))
            return;
        File file = new File(".");
        String[] strings = file.list();
        for (String dirName : strings) {
            checkIsSubmodule(dirName, module);
        }
    }

    /**
     * check a module that used from parent module or not if used it will be disabled
     *
     * @param dirName dir of a module that must be checked
     * @param module parent module
     */
    private void checkIsSubmodule(String dirName, String module) {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            File pom = new File(dirName + "/pom.xml");
            if (pom.exists()) {
                //remove module from root pom if it was sub module of given module
                removeSubmoduleFromRoot(pom, module);
            }
        }
    }


    /**
     * remove submodule from root pom.xml if it used from given module
     *
     * @param pom pom.xml file of submodule (for check their dependencies)
     * @param module parent module
     */
    private void removeSubmoduleFromRoot(File pom, String module) {

    }

}
