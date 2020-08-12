package com.lxh.reactor.example;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FibonacciState {
    private int former;
    private int latter;
}
