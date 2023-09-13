package fr.ivan.profiler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class FileTimeProfiler extends Profiler {
    private Stack<Pair<String, Integer>> _funcNames;
    private Long _lastRecorded;

    private final File _file;
    public FileTimeProfiler() {
        _file = new File("profiler");
//        if (_file.exists())
//            _file.delete();
        _lastRecorded = System.nanoTime();
        _funcNames = new Stack<>();
        _funcNames.push(new Pair<>("init", 1));
    }

    public void start(String name){
        long time = System.nanoTime();

        if (!_funcNames.isEmpty())
        {
            String lastName = _funcNames.peek().getLeft();
            try {
                FileWriter f = new FileWriter(_file, true);
                f.write(lastName);
                f.write("=" + (time - _lastRecorded));
                f.write("\n");
                f.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        //if (!_funcNames.isEmpty() && _funcNames.peek().getLeft().equals(name))
        //    _funcNames.peek().setRight(_funcNames.peek().getRight() + 1);
        //else
            _funcNames.push(new Pair<>(name, 1));
        _lastRecorded = System.nanoTime();
    }

    public void finish(String name) {
        long time = System.nanoTime();
        if (_funcNames.isEmpty())
            throw new RuntimeException(getClass().getName() + ".finish(): Empty Stack No functions to record");
        Pair<String, Integer> last = _funcNames.pop();
        String lastName = last.getLeft();
        if (!name.equals(lastName))
            throw new RuntimeException(getClass().getName() + "finish(): Expecting '" + lastName + "' but got '" + name + "'");

        if (time < _lastRecorded)
            throw new RuntimeException(getClass().getName() + "finish(): Expecting " + time + " higher than " + _lastRecorded);

        if ((time - _lastRecorded) > 500000000)
            throw new RuntimeException(getClass().getName() + "finish(): Did not expect such long function :" + (time- _lastRecorded) + " for " + name);

        try {
            FileWriter f = new FileWriter(_file, true);
            f.write(lastName);
            f.write("=" + (time - _lastRecorded));
            f.write("\n");
            f.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (last.getRight() > 1){
            last.setRight(last.getRight() - 1);
            _funcNames.push(last);
        }
        _lastRecorded = System.nanoTime();
    }

    @Override
    public String toString() {
        System.out.println("Starting Analysing");
        StringBuilder res = new StringBuilder();
        Map<String, Long> funMap = new HashMap<>();
        try {
            Scanner myReader = new Scanner(_file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] s = data.split("=");
                if (funMap.containsKey(s[0]))
                    funMap.put(s[0], funMap.get(s[0]) + Long.parseLong(s[1]));
                else
                    funMap.put(s[0], Long.parseLong(s[1]));
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        res.append("Profiler :")
           .append(_funcNames.size())
           .append("\n");

        for (var kv: funMap.entrySet()) {
            int len = kv.getKey().length();

//            res.append(kv.getValue())
//               .append("\n");
            res.append(kv.getKey() + " ".repeat(32 - len) + " :" + (kv.getValue()*0.000000001)  + "s\n");
        }
        return res.toString();
    }
}
