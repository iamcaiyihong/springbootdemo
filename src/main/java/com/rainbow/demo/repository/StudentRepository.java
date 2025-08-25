package com.rainbow.demo.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.rainbow.demo.entity.StudentDocument;

public interface StudentRepository extends ElasticsearchRepository<StudentDocument, String> {
    List<StudentDocument> findByNameContaining(String name);  // 模糊匹配
}

