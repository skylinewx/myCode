package com.example.designpatterns.faced;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolFacade {

    @Autowired
    private DeanOfficeService deanOfficeService;
    @Autowired
    private DormitoryService dormitoryService;
    @Autowired
    private HeadmasterService headmasterService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private LogisticsService logisticsService;

    @Override
    public void entrance(Student student) {
        //先来教务处报道
        deanOfficeService.checkIn(student);
        //然后去后勤处报道
        logisticsService.checkIn(student);
        //再去班主任这里报道
        headmasterService.checkIn(student);
        //再来辅导员这里报道
        dormitoryService.checkIn(student);
        //最后去宿管阿姨这里报道
        instructorService.checkIn(student);
    }
}
