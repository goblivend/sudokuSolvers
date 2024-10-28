package fr.ivan.profiler;

import java.util.HashMap;
import java.util.Map;

public class NumberProfiler extends Profiler{

    private final Map<String, Long> _funcCalls;

    public NumberProfiler() {
        _funcCalls = new HashMap<>();
    }

    @Override
    public void start(String name) {
        if (_funcCalls.containsKey(name))
            _funcCalls.put(name, _funcCalls.get(name)+1);
        else
            _funcCalls.put(name, 1L);
    }

    @Override
    public void finish(String name) {

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Profiler :\n");
        for (var kv: _funcCalls.entrySet()) {
            int len = kv.getKey().length();

            res.append(kv.getKey())
               .append(" ".repeat(32 - len))
               .append(" :")
               .append(kv.getValue())
               .append("\n");
        }
        return res.toString();
    }
}
