package com.github.avrokotlin.benchmark.data.gen

interface DataGenerator<T> {
    fun populate(obj: T, size: Int): Int
}
