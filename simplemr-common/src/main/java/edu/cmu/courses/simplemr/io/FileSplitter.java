package edu.cmu.courses.simplemr.io;

import java.util.List;

public interface FileSplitter {
    public List<FileBlock> split(String file, int number);
}