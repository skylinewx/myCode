package com.example.spring.beans3;

import com.example.spring.annotations.SkylineComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SkylineComponent
public class MySkyComponent3 {

    @Autowired
    private MySkyComponent1 mySkyComponent1;

    public MySkyComponent1 getMySkyComponent1() {
        return mySkyComponent1;
    }
}
