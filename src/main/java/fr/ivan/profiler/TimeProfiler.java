package fr.ivan.profiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static java.lang.String.format;

public class TimeProfiler extends Profiler {
    private final Stack<String> _funcNames;
    private final Map<String, Pair<Long, Long>> _funcCalls;
    private final Map<String, Map<String, Integer>> _directCallers;
    private Long _lastRecorded;

    public TimeProfiler() {
        _funcCalls = new HashMap<>();
        _funcCalls.put("init", new Pair<>(1L, 0L));
        _funcNames = new Stack<>();
        _funcNames.push("init");
        _directCallers = new HashMap<>();
        _lastRecorded = System.nanoTime();
    }

    public void start(String name) {
        long time = System.nanoTime();

        if (_funcCalls.containsKey(name)) {
            Pair<Long, Long> last = _funcCalls.get(name);
            last.setLeft(last.getLeft() + 1);
        } else
            _funcCalls.put(name, new Pair<>(1L, 0L));

        String lastName = _funcNames.peek();
        addDirectCall(name, lastName);
        Pair<Long, Long> last = _funcCalls.get(lastName);
        last.setRight(last.getRight() + time - _lastRecorded);

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

        Pair<Long, Long> last = _funcCalls.get(name);
        last.setRight(last.getRight() + time - _lastRecorded);

        _lastRecorded = System.nanoTime();
    }

    private void addDirectCall(String func, String parent) {
        if (!_directCallers.containsKey(func)) {
            _directCallers.put(func, new HashMap<>());
        }
        Map<String, Integer> funcMap = _directCallers.get(func);

        funcMap.compute(parent, (k, v) -> v == null ? 1 : v + 1);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        long total = 0;
        for (var kv : _funcCalls.entrySet())
            total += kv.getValue().getRight();
        Long decTotal = total % 1_000_000_000;
        res.append(format("total%s: %d.%s%ss\n", " ".repeat(32 - 5), total / 1_000_000_000, "0".repeat(9 - decTotal.toString().length()), decTotal));
        for (var kv : _funcCalls.entrySet()) {
            int len = kv.getKey().length();
            long dec = kv.getValue().getRight() % 1_000_000_000;
            String decStr = "" + dec;
            double percent = ((double) ((long) ((double) kv.getValue().getRight() / total * 10_000))) / 100;
            res.append(format("%s%s: %d.%s%ss %6.02f%%\t%d%n", kv.getKey(), " ".repeat(32 - len), kv.getValue().getRight() / 1_000_000_000, ("0".repeat(9 - decStr.length())), decStr, percent, kv.getValue().getLeft()));
        }

        for (var kv : _directCallers.entrySet()) {
            var callee = kv.getKey();
            res.append(format("%s :\n", callee));
            Integer totalCalls = 0;
            for (var kv2 : kv.getValue().entrySet()) {
                totalCalls += kv2.getValue();
            }
            for (var kv2 : kv.getValue().entrySet()) {
                var caller = kv2.getKey();
                double nbCalls = kv2.getValue();
                res.append(format("\t%s: %.2f%%\n", caller, nbCalls/totalCalls*100));
            }
        }

        return res.toString();
    }
}
