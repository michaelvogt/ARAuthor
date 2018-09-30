package eu.michaelvogt.ar.author.data.utils

import com.google.ar.sceneform.math.Vector3

import org.junit.Test

import java.util.Calendar

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.equalToIgnoringWhiteSpace
import org.junit.Assert.*

class ConvertersTest {

    @Test
    fun fromString_null() {
        assertThat(Converters.fromString(null), `is`(equalTo<Vector3>(null)))
    }

    @Test
    fun fromString_vector() {
        assertThat(Converters.fromString("1.0!!1.0!!1.0"), `is`(instanceOf(Vector3::class.java)))
    }

    @Test
    fun fromString_111() {
        assertThat(Converters.fromString("1.0!!1.0!!1.0").toString(),
                `is`(equalToIgnoringWhiteSpace(Vector3.one().toString())))
    }

    @Test
    fun fromString_123() {
        assertThat(Converters.fromString("1.0!!2.1!!3.2").toString(),
                `is`(equalToIgnoringWhiteSpace(Vector3(1.0f, 2.1f, 3.2f).toString())))
    }

    @Test
    fun vector3ToString_111() {
        assertThat(Converters.vector3ToString(Vector3.one()), `is`(equalTo("1.0!!1.0!!1.0")))
    }

    @Test
    fun vector3ToString_negative123() {
        assertThat(Converters.vector3ToString(Vector3(-1f, -2f, -3f)),
                `is`(equalTo("-1.0!!-2.0!!-3.0")))
    }

    @Test
    fun fromTimestamp() {
    }

    @Test
    fun dateToTimestamp() {
    }
}