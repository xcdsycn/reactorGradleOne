package com.lxh.reactor.example;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

/**
 * generate sequence
 */
public class SequenceGenerator {
    public Flux<Integer> generateFibonacciWithTuples() {
        return Flux.generate(
                () -> Tuples.of(0, 1),
                (state, sink) -> {
                    sink.next(state.getT1());
                    return Tuples.of(state.getT2(), state.getT1() + state.getT2());
                }
        );
    }

    /**
     * 用自定义类生成序列
     * @param limit
     * @return
     */
    public Flux<Integer> generateFibonacciWithCustomClass(int limit) {
        return Flux.generate(() -> new FibonacciState(0,1),
                (state,sink) -> {
                    sink.next(state.getFormer());
                    if( state.getLatter() > limit) {
                        sink.complete();
                    }
                    int temp = state.getFormer();
                    state.setFormer(state.getLatter());
                    state.setLatter(temp + state.getLatter());
                    return state;
                }
        );
    }
}
