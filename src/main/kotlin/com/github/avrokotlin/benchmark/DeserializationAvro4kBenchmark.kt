package com.github.avrokotlin.benchmark

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroInputStream
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import kotlin.reflect.KClass

data class DeserializationAvro4kInput(
    val inputStream: AvroInputStream<*>
)

@OptIn(InternalSerializationApi::class)
class DeserializationAvro4k : DeserializationBenchmark<DeserializationAvro4kInput>() {
    override fun createDeserializationInput(benchmarkData: BenchmarkData): DeserializationAvro4kInput {
        val serializer = benchmarkData.forKlass.serializer()
        val inputStream = Avro.default.openInputStream(serializer) {
            decodeFormat = AvroDecodeFormat.Binary(benchmarkData.writeSchema, benchmarkData.readSchema)
        }.from(benchmarkData.byteArray)
        return DeserializationAvro4kInput(
            inputStream
        )
    }

    override fun <R : Any> createReadSchema(writeSchema: Schema, forType: KClass<R>): Schema =
        Avro.default.schema(forType.serializer())

    override fun <R : Any> deserialize(deserializeInput: DeserializationAvro4kInput, expectedType: KClass<R>): R {
        @Suppress("UNCHECKED_CAST")
        return deserializeInput.inputStream.nextOrThrow() as R
    }
}