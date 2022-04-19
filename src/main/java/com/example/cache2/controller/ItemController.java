package com.example.cache2.controller;

import com.example.cache2.domain.ItemDO;
import com.example.cache2.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/get/{id}")
    public ItemDO get(@PathVariable String id){
        return itemService.get(id);
    }

    @PostMapping("/list")
    public List<ItemDO> list(@RequestBody ItemDO param){
        return itemService.list(itemDO -> param.getGroupId().equals(itemDO.getGroupId()));
    }

    @PostMapping("/save")
    public boolean save(@RequestBody ItemDO param){
        itemService.save(param);
        return true;
    }

    @GetMapping("/delete/{id}")
    public boolean delete(@PathVariable String id){
        itemService.delete(id);
        return true;
    }
}
