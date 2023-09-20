package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroBinaryEncoder
import com.github.avrokotlin.avro4k.io.AvroEncoder
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Setup
import kotlinx.io.Buffer
import org.apache.avro.Schema

class Avro4kDirectBenchmark : SerializationBenchmark() {
    lateinit var avroEncoder : AvroEncoder
    lateinit var clientsSchema : Schema
    lateinit var usersSchema: Schema
    override fun setupOutput() {
        super.setupOutput()
        avroEncoder = AvroBinaryEncoder(Buffer())
    }

    @Setup
    fun setupAvro4kDirect() {
        clientsSchema = Avro.default.schema(Clients.serializer())
        usersSchema = Avro.default.schema(Users.serializer())
    }
    @Benchmark
    override fun clients() {
        Avro.default.encode(avroEncoder, Clients.serializer(), clients, clientsSchema)
        avroEncoder.flush()
    }

    @Benchmark
    override fun users() {
        Avro.default.encode(avroEncoder, Users.serializer(), users, usersSchema)
        avroEncoder.flush()
    }
}