package eu.michaelvogt.ar.author.data.utils

import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test

class ConvertersTest {

    @Test
    fun vector3FromString_null() {
        assertThat(Converters.vector3FromString(null), `is`(equalTo<Vector3>(null)))
    }

    @Test
    fun vector3FromString_vector() {
        assertThat(Converters.vector3FromString("0!!0!!0"), `is`(instanceOf(Vector3::class.java)))
    }

    @Test
    fun vector3FromString_111() {
        assertThat(Converters.vector3FromString("1.0!!1.0!!1.0").toString(),
                `is`(equalTo("[x=1.0, y=1.0, z=1.0]")))
    }

    @Test
    fun vector3FromString_123() {
        assertThat(Converters.vector3FromString("1.0!!2.1!!3.2").toString(),
                `is`(equalTo("[x=1.0, y=2.1, z=3.2]")))
    }

    @Test
    fun vector3ToString_111() {
        assertThat(Converters.vector3ToString(Vector3.one()), `is`(equalTo("1.0!!1.0!!1.0")))
    }

    @Test
    fun vector3ToString_negative123() {
        assertThat(Converters.vector3ToString(Vector3(-1f, -2f, -3f)), `is`(equalTo("-1.0!!-2.0!!-3.0")))
    }


    @Test
    fun quaternionFromString_null() {
        assertThat(Converters.quaternionFromString(null), `is`(equalTo<Quaternion>(null)))
    }

    @Test
    fun quaternionFromString_1111() {
        assertThat(Converters.quaternionFromString("1.0!!1.0!!1.0!!1.0").toString(),
                `is`(equalTo("[x=1.0, y=1.0, z=1.0, w=1.0]")))
    }

    @Test
    fun quaternionFromString_negative1234() {
        assertThat(Converters.quaternionFromString("-1.0!!2.0!!-3.0!!4.0").toString(),
                `is`(equalTo("[x=-1.0, y=2.0, z=-3.0, w=4.0]")))
    }

    @Test
    fun quaternionToString_111() {
        assertThat(Converters.quaternionToString(setupQuaternion(1f, 1f, 1f, 1f)),
                `is`(equalTo("1.0!!1.0!!1.0!!1.0")))
    }

    @Test
    fun quaternionToString_negative123() {
        assertThat(Converters.quaternionToString(setupQuaternion(-1f, -2f, -3f, -4f)),
                `is`(equalTo("-1.0!!-2.0!!-3.0!!-4.0")))
    }


    @Test
    fun fromTimestamp() {
    }

    @Test
    fun dateToTimestamp() {
    }

    fun setupQuaternion(x: Float, y: Float, z: Float, w: Float): Quaternion {
        val result = Quaternion()
        result.x = x;
        result.y = y;
        result.z = z;
        result.w = w;

        return result

    }
}