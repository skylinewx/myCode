package com.example.designpatterns.faced;

import org.springframework.stereotype.Service;

/**
 * 教务处
 *
 * @author skyline
 */
@Service
public class DeanOfficeService {

    void checkIn(Student student) {
        System.out.println(student.getName() + "【" + student.getMajor() + "】来教务处报道了");
    }
}
