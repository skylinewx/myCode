package com.example.spring.beans3;

import com.example.spring.annotations.SkylineComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SkylineComponent
public class MySkyComponent2 {

    @Autowired
    private MySkyComponent3 mySkyComponent3;

    public MySkyComponent3 getMySkyComponent3() {
        return mySkyComponent3;
    }
}
