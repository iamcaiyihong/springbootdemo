package com.rainbow.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.rainbow.demo.entity.Student;
import com.rainbow.demo.mapper.StudentMapper;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    Cache<String, Object> caffeineCache;

    private String buildKey(Long id) {
        return "stu_"+id;
    }

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Student getStudent(Long id) {
        String key = buildKey(id);
        Student stu = (Student)caffeineCache.getIfPresent(key);
        if(stu != null) {
            System.out.printf("local cache hit, id=%d, key=%s\n", id, key);
            return stu;
        }

        stu = (Student) redisTemplate.opsForValue().get(key);
        if (stu != null) {
            System.out.printf("student found, id=%d key=%s\n", id, key);
            caffeineCache.put(key, stu);
            return stu;
        }
        stu = studentMapper.selectById(id);
        if(stu != null) {
            redisTemplate.opsForValue().set(key, stu);
            caffeineCache.put(key, stu);
        }
        return stu;
    }

    public List<Student> getAllStudents() {
        return studentMapper.selectAll();
    }

    public Student createStudent(Student student) {
        studentMapper.insert(student);
        String key = buildKey(student.getId());
        redisTemplate.delete(key);
        caffeineCache.invalidate(key);
        return student;
    }

    public int updateStudent(Student student) {
        int result = studentMapper.update(student);
        String key = buildKey(student.getId());
        redisTemplate.delete(key);
        caffeineCache.invalidate(key);
        return result;
    }

    public int deleteStudent(Long id) {
        int result = studentMapper.delete(id);
        String key = buildKey(id);
        redisTemplate.delete(key);
        caffeineCache.invalidate(key);
        return result;
    }
}
