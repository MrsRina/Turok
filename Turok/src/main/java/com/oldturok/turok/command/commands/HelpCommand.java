package com.oldturok.turok.command.commands;

import com.oldturok.turok.command.syntax.SyntaxChunk;
import com.oldturok.turok.module.ModuleManager;
import com.oldturok.turok.command.Command;
import com.oldturok.turok.TurokMod;

import java.util.Arrays;

public class HelpCommand extends Command {
    private static final Subject[] subjects = new Subject[]{
            new Subject(
                    new String[]{"type","int","boolean","double","float"},
                    new String[]{
                        "Turok not know this."
                    }
            )
    };
    private static String subjectsList = "";
    static {
        for (Subject subject : subjects)
            subjectsList += subject.names[0] + ", ";
        subjectsList = subjectsList.substring(0, subjectsList.length()-2);
    }

    public HelpCommand() {
        super("help", new SyntaxChunk[]{});
        setDescription("Delivers help on certain subjects. Use &b-help subjects&r for a list.");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendStringChatMessage(new String[]{
                    "Help Turok:",
                    "Press " + ModuleManager.getModuleByName("ClickGUI").getBindName() + "&4 to open GUI",
                    "For change prefix write .prefix (character)",
                    "For some other command talk with Rina!"
            });
        }
    }

    private static class Subject {
        String[] names;
        String[] info;

        public Subject(String[] names, String[] info) {
            this.names = names;
            this.info = info;
        }
    }
}
