package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroBinaryEncoder
import com.github.avrokotlin.avro4k.io.AvroEncoder
import kotlinx.io.Buffer
import kotlinx.io.asOutputStream
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import java.io.OutputStream

data class SerializationAvro4kDirectInput(
    val buffer: Buffer,
    val decoder: AvroEncoder,
    val writerSchema: Schema,
    val serializer: SerializationStrategy<*>,
    val valueToSerialize: Any
)

@OptIn(InternalSerializationApi::class)
class SerializationAvro4kDirect : SerializationBenchmark<SerializationAvro4kDirectInput>() {
    override fun createSerializationInput(benchmarkData: BenchmarkData): SerializationAvro4kDirectInput {
        val buffer = Buffer()
        val encoder = AvroBinaryEncoder(buffer)
        val serializer = benchmarkData.forKlass.serializer()
        return SerializationAvro4kDirectInput(
            buffer,
            encoder,
            benchmarkData.writeSchema,
            serializer,
            benchmarkData.valueToSerialize
        )
    }

    override fun serialize(serializationInput: SerializationAvro4kDirectInput): OutputStream {
        @Suppress("UNCHECKED_CAST")
        Avro.default.encode(
            serializationInput.decoder,
            serializationInput.serializer as SerializationStrategy<Any>,
            serializationInput.valueToSerialize,
            serializationInput.writerSchema
        )
        return serializationInput.buffer.asOutputStream()
    }
}