package eu.michaelvogt.ar.author.data.utils

import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertersTest {

    @Test
    fun vector3FromString_null() {
        assertThat(Converters().vector3FromString(null), `is`(equalTo<Vector3>(null)))
    }

    @Test
    fun vector3FromString_vector() {
        assertThat(Converters().vector3FromString("${TAG_VECTOR3}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0"), `is`(instanceOf(Vector3::class.java)))
    }

    @Test
    fun vector3FromString_111() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("${TAG_VECTOR3}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0"), Vector3(1.0f, 1.0f, 1.0f)))
    }

    @Test
    fun vector3FromString_123() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("${TAG_VECTOR3}1.0${VALUE_DIVIDER}2.1${VALUE_DIVIDER}3.2"), Vector3(1.0f, 2.1f, 3.2f)))
    }

    @Test
    fun vector3ToString_111() {
        assertThat(Converters()
                .vector3ToString(Vector3.one()), `is`(equalTo("${TAG_VECTOR3}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0")))
    }

    @Test
    fun vector3ToString_negative123() {
        assertThat(Converters()
                .vector3ToString(Vector3(-1f, -2f, -3f)), `is`(equalTo("$TAG_VECTOR3-1.0${VALUE_DIVIDER}-2.0${VALUE_DIVIDER}-3.0")))
    }

    @Test
    fun quaternionFromString_null() {
        assertThat(Converters()
                .quaternionFromString(null), `is`(equalTo<Quaternion>(null)))
    }

    @Test
    fun quaternionFromString_quaternion() {
        assertThat(Converters()
                .quaternionFromString("${TAG_QUATERNION}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0"), `is`(instanceOf(Quaternion::class.java)))
    }

    @Test
    fun quaternionFromString_1111() {
        assertThat(Converters().quaternionFromString("${TAG_QUATERNION}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0").toString(),
                `is`(equalTo("[x=1.0, y=1.0, z=1.0, w=1.0]")))
    }

    @Test
    fun quaternionFromString_negative1234() {
        assertThat(Converters().quaternionFromString("$TAG_QUATERNION-1.0${VALUE_DIVIDER}2.0${VALUE_DIVIDER}-3.0${VALUE_DIVIDER}4.0").toString(),
                `is`(equalTo("[x=-1.0, y=2.0, z=-3.0, w=4.0]")))
    }

    @Test
    fun quaternionToString_111() {
        assertThat(Converters().quaternionToString(setupQuaternion(1f, 1f, 1f, 1f)),
                `is`(equalTo("${TAG_QUATERNION}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0")))
    }

    @Test
    fun quaternionToString_negative123() {
        assertThat(Converters().quaternionToString(setupQuaternion(-1f, -2f, -3f, -4f)),
                `is`(equalTo("$TAG_QUATERNION-1.0${VALUE_DIVIDER}-2.0${VALUE_DIVIDER}-3.0${VALUE_DIVIDER}-4.0")))
    }

    @Test
    fun stringifyBoolean() {
        val converter = Converters()

        assertThat(converter.stringify(true), equalTo("${TAG_BOOL}1"))
        assertThat(converter.stringify(false), equalTo("${TAG_BOOL}0"))
    }

    @Test
    fun stringifyNumbers() {
        val converter = Converters()

        assertThat(converter.stringify(5), equalTo("${TAG_INT}5"))
        assertThat(converter.stringify(1.5f), equalTo("${TAG_FLOAT}1.5"))
    }

    @Test
    fun stringifyCollections() {
        val converter = Converters()

        assertThat(converter.stringify(listOf(5, 1.5)), equalTo("[5, 1.5]"))
        assertThat(converter.stringify(arrayListOf(5, 1.5)), equalTo("[5, 1.5]"))
        assertThat(converter.stringify(mapOf(Pair(1, "first"), Pair(2, "second"))), equalTo("{1=first, 2=second}"))
    }

    @Test
    fun stringifyEmptyList() {
        val converter = Converters()

        assertThat(converter.stringify(listOf<String>()), equalTo("[]"))
    }

    @Test
    fun stringifyFloatList() {
        val converter = Converters()

        assertThat(converter.stringify(listOf(1.0f, 2.0f, 3.0f)), equalTo("<floatlist>1.0, 2.0, 3.0"))
    }

    @Test
    fun stringifyVector() {
        val converter = Converters()

        assertThat(converter.stringify(Vector3.one()), equalTo("${TAG_VECTOR3}1.0${VALUE_DIVIDER}1.0${VALUE_DIVIDER}1.0"))
        assertThat(converter.stringify(Vector3(3.1f, -2.1f, 1.1f)), equalTo("${TAG_VECTOR3}3.1${VALUE_DIVIDER}-2.1${VALUE_DIVIDER}1.1"))
    }

    @Test
    fun stringifyQuaternion() {
        val converter = Converters()

        assertThat(converter.stringify(Quaternion.identity()), equalTo("${TAG_QUATERNION}0.0${VALUE_DIVIDER}0.0${VALUE_DIVIDER}0.0${VALUE_DIVIDER}1.0"))
        assertThat(converter.stringify(setupQuaternion(0.1f, 0.2f, -0.3f, 0.4f)), equalTo("${TAG_QUATERNION}0.1${VALUE_DIVIDER}0.2${VALUE_DIVIDER}-0.3${VALUE_DIVIDER}0.4"))
    }

    @Test
    fun objectifyBoolean() {
        val converter = Converters()

        assertThat(converter.objectify("${TAG_BOOL}1"), IsInstanceOf.instanceOf(Boolean::class.java))
        assertThat(converter.objectify("${TAG_BOOL}1") as Boolean, equalTo(true))
        assertThat(converter.objectify("${TAG_BOOL}0") as Boolean, equalTo(false))
    }

    @Test
    fun objectifyInt() {
        val converter = Converters()

        assertThat(converter.objectify("${TAG_INT}5"), IsInstanceOf.instanceOf(Int::class.java))
        assertThat(converter.objectify("$TAG_INT-5") as Int, equalTo(-5))
    }

    @Test
    fun objectifyFloat() {
        val converter = Converters()

        assertThat(converter.objectify("${TAG_FLOAT}5"), IsInstanceOf.instanceOf(Float::class.java))
        assertThat(converter.objectify("$TAG_FLOAT-1.23") as Float, equalTo(-1.23f))
    }

    @Test
    fun objectifyCollection() {
        val converter = Converters()

        assertThat(converter.objectify("[1,2,3]"), equalTo(listOf("1", "2", "3") as Any))
    }

    @Test
    fun objectifyEmptyCollection() {
        val converter = Converters()

        assertThat(converter.objectify("[]"), IsInstanceOf.instanceOf(List::class.java))
        assertThat((converter.objectify("[]") as List<*>).size, equalTo(0))
    }

    @Test
    fun objectifyFloatList() {
        val converter = Converters()

        assertThat(converter.objectify("${TAG_FLOATLIST}1.0, 2.0, 3.0"), equalTo(listOf(1.0f, 2.0f, 3.0f) as Any))
    }

    // Prevent the normalisation that is done when using the constructor
    private fun setupQuaternion(x: Float, y: Float, z: Float, w: Float): Quaternion {
        val result = Quaternion()
        result.x = x
        result.y = y
        result.z = z
        result.w = w

        return result
    }
}