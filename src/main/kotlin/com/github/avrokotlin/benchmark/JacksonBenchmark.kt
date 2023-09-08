package com.github.avrokotlin.benchmark

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.dataformat.avro.AvroFactory
import com.fasterxml.jackson.dataformat.avro.AvroMapper
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Setup

class JacksonBenchmark : SerializationBenchmark() {
    lateinit var clientsWriter: ObjectWriter
    lateinit var usersWriter: ObjectWriter
    @Setup
    fun setupJackson() {
        val schemaMapper = ObjectMapper(AvroFactory())
            .registerKotlinModule()
            .registerModule(AvroJavaTimeModule())
        clientsWriter = Clients::class.java.createWriter(schemaMapper)
        usersWriter = Users::class.java.createWriter(schemaMapper)
    }

    private fun <T> Class<T>.createWriter(schemaMapper: ObjectMapper) : ObjectWriter{
        val gen = AvroSchemaGenerator()
        schemaMapper.acceptJsonFormatVisitor(this, gen)

        val mapper = AvroMapper().registerModule(kotlinModule()).registerModule(AvroJavaTimeModule())
        return mapper.writer(gen.generatedSchema)
    }

    @Benchmark
    override fun clients() {
        clientsWriter.writeValue(out, clients)
    }

    @Benchmark
    override fun users() {
        usersWriter.writeValue(out, users)
    }
}