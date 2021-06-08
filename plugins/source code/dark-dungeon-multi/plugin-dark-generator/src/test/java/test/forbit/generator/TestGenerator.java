package test.forbit.generator;

import dev.forbit.generator.generator.Floor;
import dev.forbit.generator.generator.Generator;
import jdk.jfr.Name;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TestGenerator {
    Generator generator = new Generator();

    @Target({ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("slow")
    public @interface IntegrationTest {
    }

    @Test
    @DisplayName("Should Generate Floor with level 0")
    void shouldGenerateFloor() {
        Floor f = generator.generate(0);
        Assertions.assertNotNull(f);
        Assertions.assertEquals(f.getLevel(), 0);
    }

    @ParameterizedTest(name = "Should generator floor with size {0}")
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15})
    void shouldGenerateAllFloors( int level) {
        Floor f = generator.generate(level);
        Assertions.assertNotNull(f);
        Assertions.assertEquals(f.getLevel(), level);
        Assertions.assertNotNull(f.getStart());
        Assertions.assertTrue(f.getSize() >= Math.floor(4+(level / 2f)));
        Assertions.assertTrue(f.getTiles().size() >= (int) Math.pow(f.getSize(), 2));
    }

    @Test
    @DisplayName("Should generate large floor")
    @Tag("slow")
    void generateEdgeCase() {
        Floor f = generator.generate(100);
        Assertions.assertNotNull(f);
        Assertions.assertEquals(f.getLevel(), 100);
        Assertions.assertNotNull(f.getStart());
    }

    @ParameterizedTest(name = "Should throw error trying to make floor with size {0}")
    @ValueSource(ints = {1000,100000,Integer.MAX_VALUE})
    void shouldRunErrors(int level) {
        Assertions.assertThrows(AssertionError.class, () -> generator.generate(level));
    }




}
