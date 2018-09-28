package eu.michaelvogt.ar.author.data.utils;

import com.google.ar.sceneform.math.Vector3;

import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.*;

public class ConvertersTest {

  @Test
  public void fromString_null() {
    assertThat(Converters.fromString(null), is(equalTo(null)));
  }

  @Test
  public void fromString_vector() {
    assertThat(Converters.fromString("1.0!!1.0!!1.0"), is(instanceOf(Vector3.class)));
  }

  @Test
  public void fromString_111() {
    assertThat(Converters.fromString("1.0!!1.0!!1.0").toString(),
        is(equalToIgnoringWhiteSpace(Vector3.one().toString())));
  }

  @Test
  public void fromString_123() {
    assertThat(Converters.fromString("1.0!!2.1!!3.2").toString(),
        is(equalToIgnoringWhiteSpace(new Vector3(1.0f, 2.1f, 3.2f).toString())));
  }

  @Test
  public void vector3ToString_111() {
    assertThat(Converters.vector3ToString(Vector3.one()), is(equalTo("1.0!!1.0!!1.0")));
  }

  @Test
  public void vector3ToString_negative123() {
    assertThat(Converters.vector3ToString(new Vector3(-1f, -2f, -3f)),
        is(equalTo("-1.0!!-2.0!!-3.0")));
  }

  @Test
  public void fromTimestamp() {
  }

  @Test
  public void dateToTimestamp() {
  }
}