package com.telran.project.tracker.controller;

import com.telran.project.tracker.model.web.ItemRequest;
import com.telran.project.tracker.model.web.ItemResponse;
import com.telran.project.tracker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/create")
    public ItemResponse createItem(@RequestBody ItemRequest itemRequest, Principal principal) {
        return itemService.createItem(itemRequest, principal.getName());
    }

    @PutMapping("/update/{id}")
    public ItemResponse updateItem(@RequestBody ItemRequest itemRequest, @PathVariable("id") Long itemId) {
        return itemService.updateItem(itemRequest, itemId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/all")
    public List<ItemResponse> getItems(@RequestParam(value = "name", required = false) String name) {
        return itemService.getItems(name);
    }

    @GetMapping("/all/me")
    public List<ItemResponse> getMyItems(Principal principal) {
        return itemService.getMyItems(principal.getName());
    }

    @PostMapping("/proceed/{id}")
    public ItemResponse proceedItem(@PathVariable("id") Long id) {
        return itemService.proceedItem(id);
    }

}
