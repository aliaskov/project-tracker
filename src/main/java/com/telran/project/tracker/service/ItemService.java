package com.telran.project.tracker.service;

import com.telran.project.tracker.model.web.ItemRequest;
import com.telran.project.tracker.model.web.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(ItemRequest itemRequest, String username);
    ItemResponse updateItem(ItemRequest itemRequest, Long itemId);
    void deleteItem(Long itemId);
    List<ItemResponse> getItems(String name);
    List<ItemResponse> getMyItems(String username);
    ItemResponse proceedItem(Long itemId);
}
