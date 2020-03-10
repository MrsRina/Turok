package com.oldturok.turok.command.syntax.parsers;

import com.oldturok.turok.command.syntax.SyntaxParser;
import com.oldturok.turok.command.syntax.SyntaxChunk;

public abstract class AbstractParser implements SyntaxParser {
    @Override
    public abstract String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue);
    protected String getDefaultChunk(SyntaxChunk chunk){
        return (chunk.isHeadless() ? "" : chunk.getHead()) + (chunk.isNecessary() ? "<" : "[") + chunk.getType() + (chunk.isNecessary() ? ">" : "]");
    }

}
