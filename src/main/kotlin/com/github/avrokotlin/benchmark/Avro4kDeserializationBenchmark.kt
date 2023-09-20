package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroInputStream
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import kotlin.reflect.KClass

data class Avro4kDeserializationInput(
    val inputStream: AvroInputStream<*>
)

@OptIn(InternalSerializationApi::class)
class Avro4kDeserializationBenchmark : DeserializationBenchmark<Avro4kDeserializationInput>() {
    override fun createDeserializationInput(benchmarkData: BenchmarkData): Avro4kDeserializationInput {
        val serializer = benchmarkData.forKlass.serializer()
        val inputStream = Avro.default.openInputStream(serializer) {
            decodeFormat = AvroDecodeFormat.Binary(benchmarkData.writeSchema, benchmarkData.readSchema)
        }.from(benchmarkData.byteArray)
        return Avro4kDeserializationInput(
            inputStream
        )
    }

    override fun <R : Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema =
        Avro.default.schema(forType.serializer())

    override fun <R : Any> deserialize(deserializeInput: Avro4kDeserializationInput, expectedType: KClass<R>): R {
        @Suppress("UNCHECKED_CAST")
        return deserializeInput.inputStream.nextOrThrow() as R
    }
}