package com.example.designpatterns.faced;

import org.springframework.stereotype.Service;

/**
 * 宿舍
 *
 * @author skyline
 */
@Service
public class DormitoryService {

    void checkIn(Student student) {
        System.out.println(student.getName() + "【" + student.getMajor() + "】来宿舍阿姨这里报道了，领走了宿舍钥匙");
    }
}
