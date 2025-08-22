package com.rainbow.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 标记此类为REST控制器，返回JSON响应
@RestController
public class HelloController {

    // 处理根路径的GET请求
    @GetMapping("/")
    public String home() {
        return "欢迎使用Spring Boot！访问 /hello 查看问候语";
    }

    // 处理/hello路径的GET请求
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot!";
    }

    // 处理带参数的GET请求，例如 /hello?name=Aris
    @GetMapping("/hello/param")
    public String sayHelloWithParam(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    // 处理路径变量的GET请求，例如 /hello/Aris
    @GetMapping("/hello/{name}")
    public String sayHelloWithPath(@PathVariable String name) {
        return "Hello from path, " + name + "!";
    }
}
