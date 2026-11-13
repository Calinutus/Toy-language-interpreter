package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {}

    /** The addresses that occur in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    /** Helper to find all addresses reachable from roots, including transitively through the heap. */
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> reachable = new java.util.HashSet<>(roots);
        boolean changed;
        do {
            changed = false;
            // Find any address in the heap whose key is currently in 'reachable'
            // and whose value is a RefValue whose address is NOT yet in 'reachable'.
            Set<Integer> newAddresses = heap.entrySet().stream()
                    .filter(e -> reachable.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).address())
                    .filter(addr -> !reachable.contains(addr))
                    .collect(Collectors.toSet());

            if (!newAddresses.isEmpty()) {
                reachable.addAll(newAddresses);
                changed = true;
            }
        } while (changed);

        return reachable;
    }

    /** SAFE: keeps the locations reachable from the SymTable and transitively through the heap. */
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}