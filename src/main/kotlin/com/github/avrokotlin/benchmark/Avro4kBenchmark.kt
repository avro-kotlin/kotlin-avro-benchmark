package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.AvroOutputStreamBuilder
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import com.github.avrokotlin.avro4k.io.AvroOutputStream
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Setup
import org.apache.avro.Schema

class Avro4kBenchmark : SerializationBenchmark() {
    lateinit var clientsOutputStream: AvroOutputStream<Clients>
    lateinit var usersOutputStream: AvroOutputStream<Users>
    lateinit var clientsAvroOutputStreamBuilder: AvroOutputStreamBuilder<Clients>
    lateinit var usersAvroOutputStreamBuilder: AvroOutputStreamBuilder<Users>
    lateinit var clientsSchema: Schema
    lateinit var usersSchema: Schema
    override fun setupOutput() {
        super.setupOutput()
        this.clientsOutputStream = clientsAvroOutputStreamBuilder.to(out)
        this.usersOutputStream = usersAvroOutputStreamBuilder.to(out)
    }

    @Setup
    fun setupAvro4k() {
        clientsSchema = Avro.default.schema(Clients.serializer())
        usersSchema = Avro.default.schema(Users.serializer())
        clientsAvroOutputStreamBuilder = Avro.default.openOutputStream(Clients.serializer()) {
            this.schema = this@Avro4kBenchmark.clientsSchema
            encodeFormat = AvroEncodeFormat.Binary
        }
        usersAvroOutputStreamBuilder = Avro.default.openOutputStream(Users.serializer()) {
            this.schema = this@Avro4kBenchmark.usersSchema
            encodeFormat = AvroEncodeFormat.Binary
        }

    }
    @Benchmark
    override fun clients() {
        clientsOutputStream.write(clients).flush()
    }

    @Benchmark
    override fun users() {
        usersOutputStream.write(users).flush()
    }
}