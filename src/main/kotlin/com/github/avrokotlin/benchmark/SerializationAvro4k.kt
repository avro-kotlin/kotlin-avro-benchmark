package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import com.github.avrokotlin.avro4k.io.AvroOutputStream
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class SerializationAvro4kInput(
    val byteArrayOutputStream: ByteArrayOutputStream,
    val outputStream: AvroOutputStream<*>,
    val writerSchema: Schema,
    val serializer: SerializationStrategy<*>,
    val valueToSerialize: Any
)

@OptIn(InternalSerializationApi::class)
class SerializationAvro4k : SerializationBenchmark<SerializationAvro4kInput>() {

    override fun createSerializationInput(benchmarkData: BenchmarkData): SerializationAvro4kInput {
        val serializer = benchmarkData.forKlass.serializer()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val outputStream = Avro.default.openOutputStream(serializer) {
            this.schema = benchmarkData.writeSchema
            encodeFormat = AvroEncodeFormat.Binary
        }.to(byteArrayOutputStream)
        return SerializationAvro4kInput(
            byteArrayOutputStream,
            outputStream,
            benchmarkData.writeSchema,
            serializer,
            benchmarkData.valueToSerialize
        )

    }

    override fun serialize(serializationInput: SerializationAvro4kInput): OutputStream {
        (serializationInput.outputStream as AvroOutputStream<Any>).write(serializationInput.valueToSerialize)
        return serializationInput.byteArrayOutputStream
    }
}