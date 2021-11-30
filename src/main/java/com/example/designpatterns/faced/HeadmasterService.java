package com.example.designpatterns.faced;

import org.springframework.stereotype.Service;

/**
 * 班主任
 *
 * @author skyline
 */
@Service
public class HeadmasterService {

    void checkIn(Student student) {
        System.out.println(student.getName() + "【" + student.getMajor() + "】来班主任这里报道了，班主任给了一些宣传手册");
    }
}
