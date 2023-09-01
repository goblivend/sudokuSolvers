package fr.ivan.profiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TimeProfiler extends Profiler {
    private Stack<String> _funcNames;
    private Long _lastRecorded;
    private Map<String, Long> _funcCalls;

    public TimeProfiler() {
        _funcCalls = new HashMap<>();
        _lastRecorded = System.nanoTime();
        _funcNames = new Stack<>();
        _funcNames.push("init");
    }

    public void start(String name){
        long time = System.nanoTime();

        if (!_funcNames.isEmpty())
        {
            String lastName = _funcNames.peek();

            if (_funcCalls.containsKey(lastName))
                _funcCalls.put(lastName, time - _lastRecorded + _funcCalls.get(lastName));
            else
                _funcCalls.put(lastName, time - _lastRecorded);
        }
        _funcNames.push(name);
        _lastRecorded = System.nanoTime();
    }

    public void finish(String name) {
        long time = System.nanoTime();
        if (_funcNames.isEmpty())
            throw new RuntimeException(getClass().getName() + ".finish(): Empty Stack No functions to record");
        String lastName = _funcNames.pop();
        if (!name.equals(lastName))
            throw new RuntimeException(getClass().getName() + "finish(): Expecting '" + lastName + "' but got '" + name + "'");


        if (_funcCalls.containsKey(lastName))
            _funcCalls.put(lastName, time - _lastRecorded + _funcCalls.get(lastName));
        else
            _funcCalls.put(lastName, time - _lastRecorded);
        _lastRecorded = System.nanoTime();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        long total = 0;
        for (var kv: _funcCalls.entrySet())
            total += kv.getValue();
        long decTotal = total % 1000000000;
        String decStrTotal = "" + decTotal;
        res.append("total" + " ".repeat(32 - 5) + " :" + total/1000000000 + "." + ("0".repeat(9-decStrTotal.length()))  + decStrTotal + "s\n");
        for (var kv: _funcCalls.entrySet()) {
            int len = kv.getKey().length();
            long dec = kv.getValue() % 1000000000;
            String decStr = "" + dec;
            double percent = ((double)((long)((double) kv.getValue() /total * 10000)))/100;
            res.append(kv.getKey() + " ".repeat(32 - len) + " :" + kv.getValue()/1000000000 + "." + ("0".repeat(9-decStr.length()))  + decStr + "s " + percent+  "%\n");
        }
        return res.toString();
    }
}
