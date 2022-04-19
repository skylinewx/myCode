package com.example.cache2.controller;

import com.example.cache2.domain.GroupDO;
import com.example.cache2.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/get/{id}")
    public GroupDO get(@PathVariable String id){
        return groupService.get(id);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody GroupDO param){
        groupService.save(param);
        return true;
    }

    @GetMapping("/delete/{id}")
    public boolean delete(@PathVariable String id){
        groupService.delete(id);
        return true;
    }
}
