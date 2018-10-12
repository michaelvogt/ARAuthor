package eu.michaelvogt.ar.author.data.utils

import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class ConvertersTest {

    @Test
    fun vector3FromString_null() {
        assertThat(Converters().vector3FromString(null), `is`(equalTo<Vector3>(null)))
    }

    @Test
    fun vector3FromString_vector() {
        assertThat(Converters().vector3FromString("<vector3>1.0!!1.0!!1.0<vector3>"), `is`(instanceOf(Vector3::class.java)))
    }

    @Test
    fun vector3FromString_111() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("<vector3>1.0!!1.0!!1.0<vector3>"), Vector3(1.0f, 1.0f, 1.0f)))
    }

    @Test
    fun vector3FromString_123() {
        assertTrue(Vector3.equals(Converters()
                .vector3FromString("<vector3>1.0!!2.1!!3.2<vector3>"), Vector3(1.0f, 2.1f, 3.2f)))
    }

    @Test
    fun vector3ToString_111() {
        assertThat(Converters()
                .vector3ToString(Vector3.one()), `is`(equalTo("<vector3>1.0!!1.0!!1.0<vector3>")))
    }

    @Test
    fun vector3ToString_negative123() {
        assertThat(Converters()
                .vector3ToString(Vector3(-1f, -2f, -3f)), `is`(equalTo("<vector3>-1.0!!-2.0!!-3.0<vector3>")))
    }


    @Test
    fun quaternionFromString_null() {
        assertThat(Converters()
                .quaternionFromString(null), `is`(equalTo<Quaternion>(null)))
    }

    @Test
    fun quaternionFromString_quaternion() {
        assertThat(Converters()
                .quaternionFromString("<quaternion>1.0!!1.0!!1.0!!1.0<quaternion>"), `is`(instanceOf(Quaternion::class.java)))
    }

    @Test
    fun quaternionFromString_1111() {
        assertThat(Converters().quaternionFromString("<quaternion>1.0!!1.0!!1.0!!1.0<quaternion>").toString(),
                `is`(equalTo("[x=1.0, y=1.0, z=1.0, w=1.0]")))
    }

    @Test
    fun quaternionFromString_negative1234() {
        assertThat(Converters().quaternionFromString("<quaternion>-1.0!!2.0!!-3.0!!4.0<quaternion>").toString(),
                `is`(equalTo("[x=-1.0, y=2.0, z=-3.0, w=4.0]")))
    }

    @Test
    fun quaternionToString_111() {
        assertThat(Converters().quaternionToString(setupQuaternion(1f, 1f, 1f, 1f)),
                `is`(equalTo("<quaternion>1.0!!1.0!!1.0!!1.0<quaternion>")))
    }

    @Test
    fun quaternionToString_negative123() {
        assertThat(Converters().quaternionToString(setupQuaternion(-1f, -2f, -3f, -4f)),
                `is`(equalTo("<quaternion>-1.0!!-2.0!!-3.0!!-4.0<quaternion>")))
    }

    @Test
    fun stringifyNumbers() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(5), IsEqual.equalTo("5"))
        Assert.assertThat(converter.stringify(1.5f), IsEqual.equalTo("1.5"))
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

        Assert.assertThat(converter.stringify(Vector3.one()), IsEqual.equalTo("1.0!!1.0!!1.0"))
        Assert.assertThat(converter.stringify(Vector3(3.1f, -2.1f, 1.1f)), IsEqual.equalTo("3.1!!-2.1!!1.1"))
    }

    @Test
    fun stringifyQuaternion() {
        val converter = Converters()

        Assert.assertThat(converter.stringify(Quaternion.identity()), IsEqual.equalTo("<quaternion>0.0!!0.0!!0.0!!1.0<quaternion>"))
        Assert.assertThat(converter.stringify(setupQuaternion(0.1f, 0.2f, -0.3f, 0.4f)), IsEqual.equalTo("<quaternion>0.1!!0.2!!-0.3!!0.4<quaternion>"))
    }

    @Test
    fun objectifyNumber() {
        val converter = Converters()

        Assert.assertThat(converter.objectify("5"), IsInstanceOf.instanceOf(Int::class.java))
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