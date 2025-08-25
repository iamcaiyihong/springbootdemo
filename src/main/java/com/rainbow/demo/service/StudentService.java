package com.rainbow.demo.service;


import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.rainbow.demo.entity.Student;
import com.rainbow.demo.entity.StudentDocument;
import com.rainbow.demo.mapper.StudentMapper;
import com.rainbow.demo.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private final Cache<String, Object> caffeineCache;

    private final StudentRepository repository;

    private String buildKey(Long id) {
        return "stu_"+id;
    }

    public StudentService(StudentMapper studentMapper, 
    Cache<String, Object>  caffeineCache, 
    RedisTemplate<String, Object> redisTemplate,
    StudentRepository repository) {
        this.studentMapper = studentMapper;
        this.redisTemplate = redisTemplate;
        this.caffeineCache = caffeineCache;
        this.repository = repository;
    }

    public List<StudentDocument> searchByName(String keyword) {
        return repository.findByNameContaining(keyword);
    }

    public StudentDocument save(StudentDocument student) {
        return repository.save(student);
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
