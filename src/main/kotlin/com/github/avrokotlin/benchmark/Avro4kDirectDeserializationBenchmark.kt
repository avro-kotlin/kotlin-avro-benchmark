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

data class Avro4kDirectDeserializationInput(
    val decoder: AvroDecoder,
    val writerSchema: Schema,
    val readerSchema: Schema,
    val deserializer: DeserializationStrategy<*>
)

class Avro4kDirectDeserializationBenchmark : DeserializationBenchmark<Avro4kDirectDeserializationInput>() {
    override fun createDeserializationInput(benchmarkData: BenchmarkData): Avro4kDirectDeserializationInput {
        val buffer = Buffer()
        buffer.write(benchmarkData.byteArray)
        return Avro4kDirectDeserializationInput(
            AvroBinaryDecoder(buffer),
            benchmarkData.writeSchema,
            benchmarkData.readSchema,
            benchmarkData.forKlass.serializer()
        )
    }

    override fun <R : Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema =
        Avro.default.schema(forType.serializer())

    override fun <R : Any> deserialize(deserializeInput: Avro4kDirectDeserializationInput, expectedType: KClass<R>): R {
        @Suppress("UNCHECKED_CAST")
        return Avro.default.decode(
            deserializeInput.decoder,
            deserializeInput.deserializer,
            deserializeInput.writerSchema,
            deserializeInput.readerSchema
        ) as R
    }
}