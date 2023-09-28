package com.github.avrokotlin.benchmark

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.dataformat.avro.AvroMapper
import com.fasterxml.jackson.dataformat.avro.AvroSchema
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class SerializationJacksonInput(
    val writer: ObjectWriter,
    val outputStream: OutputStream,
    val valueToWrite: Any
)
class SerializationJackson : SerializationBenchmark<SerializationJacksonInput>() {
    val mapper: ObjectMapper by lazy {
        AvroMapper().registerModule(kotlinModule()).registerModule(AvroJavaTimeModule())
    }

    override fun createSerializationInput(benchmarkData: BenchmarkData): SerializationJacksonInput {
        val writer = mapper.writer(AvroSchema(benchmarkData.writeSchema))
        val outputStream = ByteArrayOutputStream()
        return SerializationJacksonInput(writer, outputStream, benchmarkData.valueToSerialize)
        
    }

    override fun serialize(serializationInput: SerializationJacksonInput): OutputStream {
        serializationInput.writer.writeValue(serializationInput.outputStream, serializationInput.valueToWrite)
        return serializationInput.outputStream
    }
}