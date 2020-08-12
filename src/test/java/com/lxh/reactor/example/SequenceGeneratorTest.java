package com.lxh.reactor.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceGeneratorTest  {

    @Test
    public void testGenerateFibonacciWithTuples() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Flux<Integer> fibonacciFlux = sequenceGenerator.generateFibonacciWithTuples().take(5);

        StepVerifier.create(fibonacciFlux).expectNext(0, 1, 1, 2, 3).expectComplete().verify();
    }

    @Test
    public void testGenerateFibonacciWithCustomClass() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();

        StepVerifier.create(sequenceGenerator.generateFibonacciWithCustomClass(10))
                .expectNext(0,1,1,2,3,5,8)
                .expectComplete()
                .verify();
    }

    @Test
    public void testCreateNumbers_thenSequencedAsynchronously() throws InterruptedException {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        List<Integer>  sequence1 = sequenceGenerator.generateFibonacciWithTuples().take(3).collectList().block();
        List<Integer>  sequence2 = sequenceGenerator.generateFibonacciWithTuples().take(4).collectList().block();
        SequenceCreator sequenceCreator = new SequenceCreator();

        Thread producingThread1 = new Thread(()-> sequenceCreator.consumer.accept(sequence1));
        Thread producingThread2 = new Thread(()-> sequenceCreator.consumer.accept(sequence2));

        List<Integer> consolidated = new ArrayList<>();
        sequenceCreator.createNumberSequence().subscribe(consolidated::add);

        producingThread1.start();
        producingThread2.start();

        producingThread1.join();
        producingThread2.join();

        Assertions.assertTrue(consolidated.containsAll(Arrays.asList(0,1,1,0,1,1,2)));

    }

    @Test
    public void whenHandlingNumbers_thenSequenceIsMappedAndFiltered() {
        SequenceHandler sequenceHandler = new SequenceHandler();
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Flux<Integer> sequence = sequenceGenerator.generateFibonacciWithTuples().take(10);

        StepVerifier.create(sequenceHandler.handleIntegerSequence(sequence))
                .expectNext(0,1,4,17)
                .expectComplete()
                .verify();
    }

}