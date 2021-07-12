package com.exam.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class StudentUtilsTest {

    @Test
    void applyProbability() {
        Map<Student,Student> map = new HashMap<>();
        for (int i = 0; i <4; i++) {
            map.put(mock(Student.class),mock(Student.class));
        }
        System.out.println(map.keySet().size());
        StudentUtils.applyProbability(map);
        System.out.println(map.keySet().size());
        assertEquals(2,map.keySet().size());
    }

    @Test
    void generateRandomNumbers() {
        assertEquals(0, (StudentUtils.generateRandomNumbers(5, 0)).size());
        assertThrows(IllegalArgumentException.class, () -> StudentUtils.generateRandomNumbers(9, 10));
    }

    @Test
    @DisplayName("Rounds to 2 decimal points.")
    void round() {
        assertEquals(0.2, StudentUtils.round(0.199999999));
        assertEquals(0.0, StudentUtils.round(0.00000001));
        assertEquals(0.1, StudentUtils.round(0.10000001));
    }
}