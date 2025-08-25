package com.rainbow.demo.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.github.benmanes.caffeine.cache.Cache;
import com.rainbow.demo.entity.Student;
import com.rainbow.demo.mapper.StudentMapper;

class StudentServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private Cache<String, Object> caffeineCache;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetAllStudents() {
        // 准备模拟数据
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("Alice");

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("Bob");

        List<Student> mockList = Arrays.asList(s1, s2);

        // 当调用 studentMapper.selectAll() 返回 mockList
        when(studentMapper.selectAll()).thenReturn(mockList);

        // 调用测试方法
        List<Student> result = studentService.getAllStudents();

        // 断言结果
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());

        Long id = 1L;
        String key = "stu_" + id;
        Student cachedStudent = new Student();
        cachedStudent.setId(id);
        cachedStudent.setName("rainbow");

        // mock getIfPresent 返回缓存数据
        when(caffeineCache.getIfPresent(key)).thenReturn(cachedStudent);

        Student stu = studentService.getStudent(id);
        assertEquals(cachedStudent.getName(), stu.getName());
    }
}
