package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroBinaryEncoder
import com.github.avrokotlin.benchmark.data.gen.ClientsGenerator
import com.github.avrokotlin.benchmark.data.gen.UsersGenerator
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.*
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import org.openjdk.jmh.annotations.Level
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


@OptIn(InternalSerializationApi::class)
@State(Scope.Benchmark)
@Warmup(iterations = 2)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
abstract class SerializationBenchmark<T : Any> {
    val benchmarkData = mutableMapOf<KClass<*>, BenchmarkData>()
    lateinit var usersInput: T
    lateinit var clientsInput: T

    data class BenchmarkData(val forKlass: KClass<*>, val writeSchema: Schema, val valueToSerialize: Any)

    @Setup
    fun initTestData() {
        prepareTestData(ClientsGenerator::populate)
        prepareTestData(UsersGenerator::populate)
    }

    private inline fun <reified K : Any> prepareTestData(populate: (K, Int) -> Unit) {
        val instance = K::class.createInstance()
        populate.invoke(instance, 1000)
        val serializer = K::class.serializer()
        val writeSchema = Avro.default.schema(serializer)
        benchmarkData[K::class] = BenchmarkData(K::class, writeSchema, instance)
    }

    @Setup(Level.Invocation)
    fun setupInput() {
        usersInput = createSerializationInput(benchmarkData[Users::class]!!)
        clientsInput = createSerializationInput(benchmarkData[Clients::class]!!)
    }

    abstract fun createSerializationInput(benchmarkData: BenchmarkData): T
    abstract fun serialize(serializationInput: T): OutputStream

    @Benchmark
    fun clients() = serialize(clientsInput)

    @Benchmark
    fun users() = serialize(usersInput)
}