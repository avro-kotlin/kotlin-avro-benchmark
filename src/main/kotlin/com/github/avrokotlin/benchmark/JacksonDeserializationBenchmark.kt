package com.github.avrokotlin.benchmark

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.dataformat.avro.AvroFactory
import com.fasterxml.jackson.dataformat.avro.AvroMapper
import com.fasterxml.jackson.dataformat.avro.AvroSchema
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.avrokotlin.benchmark.data.model.Clients
import com.github.avrokotlin.benchmark.data.model.Users
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Setup
import org.apache.avro.Schema
import java.io.ByteArrayInputStream
import kotlin.reflect.KClass

data class JacksonDeserializationBenchmarkInput(
    val reader: ObjectReader,
    val inputStream: ByteArrayInputStream
)

class JacksonDeserializationBenchmark : DeserializationBenchmark<JacksonDeserializationBenchmarkInput>() {
    lateinit var mapper: ObjectMapper
    lateinit var schemaMapper: ObjectMapper

    @Setup
    fun setupJackson() {
        schemaMapper = ObjectMapper(AvroFactory())
            .registerKotlinModule()
            .registerModule(AvroJavaTimeModule())
        mapper = AvroMapper().registerModule(kotlinModule()).registerModule(AvroJavaTimeModule())
    }

    override fun createDeserializationInput(benchmarkData: BenchmarkData): JacksonDeserializationBenchmarkInput {
        return JacksonDeserializationBenchmarkInput(
            mapper.reader(AvroSchema(benchmarkData.readSchema)),
            ByteArrayInputStream(benchmarkData.byteArray)
        )
    }

    override fun <R: Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema {
        val gen = AvroSchemaGenerator()
        schemaMapper.acceptJsonFormatVisitor(forType.java, gen)
        return AvroSchema(writeSchema).withReaderSchema(gen.generatedSchema).avroSchema
    }

    override fun <R : Any> deserialize(
        deserializeInput: JacksonDeserializationBenchmarkInput,
        expectedType: KClass<R>
    ): R {
        return deserializeInput.reader.readValue(deserializeInput.inputStream, expectedType.java)
    }
}