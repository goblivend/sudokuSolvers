package fr.ivan.profiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class Profiler {


    public Profiler() {

    }

    public abstract void start(String name);
    public abstract void finish(String name);
}
