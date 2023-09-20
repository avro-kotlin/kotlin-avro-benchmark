package com.github.avrokotlin.benchmark.data.model

import com.github.avrokotlin.avro4k.ScalePrecision
import com.github.avrokotlin.avro4k.serializer.BigDecimalSerializer
import com.github.avrokotlin.avro4k.serializer.InstantSerializer
import com.github.avrokotlin.avro4k.serializer.LocalDateSerializer
import com.github.avrokotlin.avro4k.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import sun.awt.X11.InfoWindow.Balloon
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import kotlin.math.abs

@Serializable
data class Clients(
    var clients: MutableList<Client> = mutableListOf()
)
@Serializable
data class Client(
    var id: Long = 0,
    var index: Int = 0,
    var isActive: Boolean = false,
    var picture: ByteArray? = null,
    var age: Int = 0,
    var eyeColor: EyeColor? = null,
    var name: String? = null,
    var gender: String? = null,
    var company: String? = null,
    var emails: Array<String> = emptyArray(),
    var phones: LongArray = LongArray(0),
    var address: String? = null,
    var about: String? = null,
    @Serializable(with = LocalDateSerializer::class)
    var registered: LocalDate? = null,
    var latitude : Double = 0.0,
    var longitude: Double = 0.0,
    var tags: List<String> = emptyList(),
    var partners: List<Partner> = emptyList(),
)

@Serializable
enum class EyeColor {
    BROWN,
    BLUE,
    GREEN;
}
@Serializable
class Partner(
    val id: Long = 0,
    val name: String? = null,
    @Serializable(with = InstantSerializer::class)
    val since: Instant? = null
)
