package com.example.designpatterns.faced;

import org.springframework.stereotype.Service;

/**
 * 辅导员
 *
 * @author skyline
 */
@Service
public class InstructorService {

    void checkIn(Student student) {
        System.out.println(student.getName() + "【" + student.getMajor() + "】来辅导员这里报道了，辅导员给讲了一些注意事项");
    }
}
