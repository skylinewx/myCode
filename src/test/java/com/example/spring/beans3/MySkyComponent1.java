package com.example.spring.beans3;

import com.example.spring.annotations.SkylineComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SkylineComponent
public class MySkyComponent1 {
    @Autowired
    private MySkyComponent2 mySkyComponent2;

    public MySkyComponent2 getMySkyComponent2() {
        return mySkyComponent2;
    }
}
