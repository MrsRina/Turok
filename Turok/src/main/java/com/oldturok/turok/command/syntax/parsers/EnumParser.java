package com.oldturok.turok.command.syntax.parsers;

import com.oldturok.turok.command.syntax.SyntaxChunk;

import java.util.Collections;
import java.util.ArrayList;

public class EnumParser extends AbstractParser {
    String[] modes;

    public EnumParser(String[] modes) {
        this.modes = modes;
    }

    @Override
    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (chunkValue == null){
            String s = "";
            for (String a : modes){
                s += a + ":";
            }
            s = s.substring(0, s.length()-1);
            return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + s + (thisChunk.isNecessary() ? ">" : "]");
        }

        ArrayList<String> possibilities = new ArrayList<String>();

        for (String s : modes){
            if (s.toLowerCase().startsWith(chunkValue.toLowerCase()))
                possibilities.add(s);
        }

        if (possibilities.isEmpty()) return "";

        Collections.sort(possibilities);

        String s = possibilities.get(0);
        return s.substring(chunkValue.length());
    }
}
