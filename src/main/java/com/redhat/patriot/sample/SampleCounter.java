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

/**
 * Simple counter file
 */
public class SampleCounter {

    private int count;

    SampleCounter() {
        this.count = 0;
    }

    /**
     * Get current value
     * @return current value
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Increments current value by one
     */
    public void increment() {
        this.count++;
    }

    /**
     * Decrements current value by one
     */
    public void decrement() {
        this.count--;
    }
}
