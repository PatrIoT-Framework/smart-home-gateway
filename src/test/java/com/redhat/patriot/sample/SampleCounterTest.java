/*
 * Copyright 2018 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.redhat.patriot.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SampleCounterTest {

    @Test
    void getCount() {
        SampleCounter c1 = new SampleCounter();
        assertEquals(0, c1.getCount());
    }

    @Test
    void increment() {
        SampleCounter tested = new SampleCounter();
        for (int expected = 0; expected < 100; expected++) {
            assertEquals(expected, tested.getCount());
            tested.increment();
        }
    }

    @Test
    void decrement() {
        SampleCounter tested = new SampleCounter();
        for (int expected = 0; expected > -100; expected--) {
            assertEquals(expected, tested.getCount());
            tested.decrement();
        }
    }
}