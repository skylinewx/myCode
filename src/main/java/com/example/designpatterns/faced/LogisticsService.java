package com.example.designpatterns.faced;

import org.springframework.stereotype.Service;

/**
 * 后勤处
 *
 * @author skyline
 */
@Service
public class LogisticsService {

    void checkIn(Student student) {
        System.out.println(student.getName() + "【" + student.getMajor() + "】来后勤处报道了，领走了饭卡");
    }
}
