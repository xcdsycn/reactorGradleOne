package com.lxh.reactor.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceCreatorTest  {

    @Test
    public void testCreateNumberSequence() {
        Flux<String> just = Flux.just("1","2","3");
        Mono<String> mono = Mono.just("foo");

        List<Integer> elements = new ArrayList<>();

        Flux.just(1,2,3,4)
                .log()
                .subscribe(elements::add);

        Assertions.assertTrue(elements.containsAll(Arrays.asList(1,2,3,4)));
        // attention: this elements type String is not ok
        List<String> ems = new ArrayList<>();
         Flux.just(5, 6, 7, 8)
                .log()
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First Flux: %d, Second Flux : %d", one, two))
                 .subscribe(ems::add);

        System.out.println(ems);
    }
}