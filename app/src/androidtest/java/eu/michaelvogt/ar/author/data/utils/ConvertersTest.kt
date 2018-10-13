package eu.michaelvogt.ar.author.data.utils

import android.support.test.runner.AndroidJUnit4
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConvertersTest {

    @Test
    fun vector3FromString_null() {
        assertThat(Converters().vector3FromString(null), `is`(equalTo<Vector3>(null)))
    }

    @Test
    fun vector3FromString_vector() {
        assertThat(Converters().vector3FromString("${VECTOR3_TAG}1.0!!1.0!!1.0"), `is`(instanceOf(Vector3::class.java)))
    }

    @Test
    fun vector3FromString_111() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("${VECTOR3_TAG}1.0!!1.0!!1.0"), Vector3(1.0f, 1.0f, 1.0f)))
    }

    @Test
    fun vector3FromString_123() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("${VECTOR3_TAG}1.0!!2.1!!3.2"), Vector3(1.0f, 2.1f, 3.2f)))
    }

    @Test
    fun vector3ToString_111() {
        assertThat(Converters()
                .vector3ToString(Vector3.one()), `is`(equalTo("${VECTOR3_TAG}1.0!!1.0!!1.0")))
    }

    @Test
    fun vector3ToString_negative123() {
        assertThat(Converters()
                .vector3ToString(Vector3(-1f, -2f, -3f)), `is`(equalTo("${VECTOR3_TAG}-1.0!!-2.0!!-3.0")))
    }

    @Test
    fun quaternionFromString_null() {
        assertThat(Converters()
                .quaternionFromString(null), `is`(equalTo<Quaternion>(null)))
    }

    @Test
    fun quaternionFromString_quaternion() {
        assertThat(Converters()
                .quaternionFromString("${QUATERNION_TAG}1.0!!1.0!!1.0!!1.0"), `is`(instanceOf(Quaternion::class.java)))
    }

    @Test
    fun quaternionFromString_1111() {
        assertThat(Converters().quaternionFromString("${QUATERNION_TAG}1.0!!1.0!!1.0!!1.0").toString(),
                `is`(equalTo("[x=1.0, y=1.0, z=1.0, w=1.0]")))
    }

    @Test
    fun quaternionFromString_negative1234() {
        assertThat(Converters().quaternionFromString("${QUATERNION_TAG}-1.0!!2.0!!-3.0!!4.0").toString(),
                `is`(equalTo("[x=-1.0, y=2.0, z=-3.0, w=4.0]")))
    }

    @Test
    fun quaternionToString_111() {
        assertThat(Converters().quaternionToString(setupQuaternion(1f, 1f, 1f, 1f)),
                `is`(equalTo("${QUATERNION_TAG}1.0!!1.0!!1.0!!1.0")))
    }

    @Test
    fun quaternionToString_negative123() {
        assertThat(Converters().quaternionToString(setupQuaternion(-1f, -2f, -3f, -4f)),
                `is`(equalTo("${QUATERNION_TAG}-1.0!!-2.0!!-3.0!!-4.0")))
    }

    @Test
    fun stringifyBoolean() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(true), IsEqual.equalTo("${BOOL_TAG}1"))
        Assert.assertThat(converter.stringify(false), IsEqual.equalTo("${BOOL_TAG}0"))
    }

    @Test
    fun stringifyNumbers() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(5), IsEqual.equalTo("${INT_TAG}5"))
        Assert.assertThat(converter.stringify(1.5f), IsEqual.equalTo("${FLOAT_TAG}1.5"))
    }

    @Test
    fun stringifyCollections() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(listOf(5, 1.5)), IsEqual.equalTo("[5, 1.5]"))
        Assert.assertThat(converter.stringify(arrayListOf(5, 1.5)), IsEqual.equalTo("[5, 1.5]"))
        Assert.assertThat(converter.stringify(mapOf(Pair(1, "first"), Pair(2, "second"))), IsEqual.equalTo("{1=first, 2=second}"))
    }

    @Test
    fun stringifyVector() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(Vector3.one()), IsEqual.equalTo("${VECTOR3_TAG}1.0!!1.0!!1.0"))
        Assert.assertThat(converter.stringify(Vector3(3.1f, -2.1f, 1.1f)), IsEqual.equalTo("${VECTOR3_TAG}3.1!!-2.1!!1.1"))
    }

    @Test
    fun stringifyQuaternion() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(Quaternion.identity()), IsEqual.equalTo("${QUATERNION_TAG}0.0!!0.0!!0.0!!1.0"))
        Assert.assertThat(converter.stringify(setupQuaternion(0.1f, 0.2f, -0.3f, 0.4f)), IsEqual.equalTo("${QUATERNION_TAG}0.1!!0.2!!-0.3!!0.4"))
    }

    @Test
    fun objectifyBoolean() {
        val converter = Converters()

        Assert.assertThat(converter.objectify("${BOOL_TAG}1"), IsInstanceOf.instanceOf(Boolean::class.java))
        Assert.assertThat(converter.objectify("${BOOL_TAG}1") as Boolean, IsEqual.equalTo(true))
        Assert.assertThat(converter.objectify("${BOOL_TAG}0") as Boolean, IsEqual.equalTo(false))
    }

    @Test
    fun objectifyInt() {
        val converter = Converters()

        Assert.assertThat(converter.objectify("${INT_TAG}5"), IsInstanceOf.instanceOf(Int::class.java))
        Assert.assertThat(converter.objectify("${INT_TAG}-5") as Int, IsEqual.equalTo(-5))
    }

    @Test
    fun objectifyFloat() {
        val converter = Converters()

        Assert.assertThat(converter.objectify("${FLOAT_TAG}5"), IsInstanceOf.instanceOf(Float::class.java))
        Assert.assertThat(converter.objectify("${FLOAT_TAG}-1.23") as Float, IsEqual.equalTo(-1.23f))
    }

    @Test
    fun objectifyCollection() {
        val converter = Converters()

        Assert.assertThat(converter.objectify("[1, 2, 3]") as List<Int>, IsEqual.equalTo(listOf<Int>(1, 2, 3)))
    }

    private fun setupQuaternion(x: Float, y: Float, z: Float, w: Float): Quaternion {
        val result = Quaternion()
        result.x = x
        result.y = y
        result.z = z
        result.w = w

        return result
    }
}