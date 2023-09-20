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
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


@OptIn(InternalSerializationApi::class)
@State(Scope.Benchmark)
@Warmup(iterations = 2)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
abstract class DeserializationBenchmark<T : Any> {
    val benchmarkData = mutableMapOf<KClass<*>, BenchmarkData>()
    lateinit var usersInput: T
    lateinit var clientsInput: T

    data class BenchmarkData(val forKlass: KClass<*>, val writeSchema: Schema, val readSchema: Schema, val byteArray: ByteArray)

    @Setup
    fun initTestData() {
        prepareTestData(ClientsGenerator::populate)
        prepareTestData(UsersGenerator::populate)
    }

    private inline fun <reified K : Any> prepareTestData(populate: (K, Int) -> Unit) {
        val instance = K::class.createInstance()
        populate.invoke(instance, 1000)
        val buffer = Buffer()
        val avroEncoder = AvroBinaryEncoder(buffer)
        val serializer = K::class.serializer()
        val writeSchema = Avro.default.schema(serializer)
        Avro.default.encode(avroEncoder, serializer, instance, writeSchema)
        val byteArray = buffer.readByteArray()
        val readSchema = createReadSchema(writeSchema, K::class)
        benchmarkData[K::class] = BenchmarkData(K::class, writeSchema, readSchema, byteArray)
    }

    @Setup(Level.Invocation)
    fun setupInput() {
        usersInput = createDeserializationInput(benchmarkData[Users::class]!!)
        clientsInput = createDeserializationInput(benchmarkData[Clients::class]!!)
    }

    abstract fun createDeserializationInput(benchmarkData: BenchmarkData): T
    abstract fun <R : Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema
    abstract fun <R : Any> deserialize(deserializeInput: T, expectedType: KClass<R>): R

    @Benchmark
    fun clients(): Clients = deserialize(clientsInput, Clients::class)

    @Benchmark
    fun users(): Users = deserialize(usersInput, Users::class)
}