@file:OptIn(InternalSerializationApi::class)

package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroBinaryDecoder
import com.github.avrokotlin.avro4k.io.AvroDecoder
import kotlinx.io.Buffer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import kotlin.reflect.KClass

data class DeserializationAvro4kDirectInput(
    val decoder: AvroDecoder,
    val writerSchema: Schema,
    val readerSchema: Schema,
    val deserializer: DeserializationStrategy<*>
)

class DeserializationAvro4kDirect : DeserializationBenchmark<DeserializationAvro4kDirectInput>() {
    override fun createDeserializationInput(benchmarkData: BenchmarkData): DeserializationAvro4kDirectInput {
        val buffer = Buffer()
        buffer.write(benchmarkData.byteArray)
        return DeserializationAvro4kDirectInput(
            AvroBinaryDecoder(buffer),
            benchmarkData.writeSchema,
            benchmarkData.readSchema,
            benchmarkData.forKlass.serializer()
        )
    }

    override fun <R : Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema =
        Avro.default.schema(forType.serializer())

    override fun <R : Any> deserialize(deserializeInput: DeserializationAvro4kDirectInput, expectedType: KClass<R>): R {
        @Suppress("UNCHECKED_CAST")
        return Avro.default.decode(
            deserializeInput.decoder,
            deserializeInput.deserializer,
            deserializeInput.writerSchema,
            deserializeInput.readerSchema
        ) as R
    }
}