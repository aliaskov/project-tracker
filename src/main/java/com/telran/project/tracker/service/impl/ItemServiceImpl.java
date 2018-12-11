package com.telran.project.tracker.service.impl;

import com.telran.project.tracker.exception.ItemNotFoundException;
import com.telran.project.tracker.model.entity.Item;
import com.telran.project.tracker.model.entity.ItemStatus;
import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.model.web.ItemRequest;
import com.telran.project.tracker.model.web.ItemResponse;
import com.telran.project.tracker.repository.ItemRepository;
import com.telran.project.tracker.repository.ProjectUserRepository;
import com.telran.project.tracker.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProjectUserRepository userRepository;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest, String username) {
        ProjectUser projectUser = userRepository.findByUsername(username);

        Item item = Item.builder()
                .itemName(itemRequest.getItemName())
                .itemType(itemRequest.getItemType())
                .itemStatus(ItemStatus.TODO)
                .createdDate(new Date())
                .projectUser(projectUser)
                .build();

        ItemResponse itemResponse = new ItemResponse(itemRepository.save(item));

        log.info("New item created");
        return itemResponse;
    }

    @Override
    public ItemResponse updateItem(ItemRequest itemRequest, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));

        item.setItemName(itemRequest.getItemName());
        item.setItemType(itemRequest.getItemType());


        ItemResponse itemResponse = new ItemResponse(itemRepository.save(item));

        log.info("Item with ID = {} updated", itemId);
        return itemResponse;
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
        log.info("Item with ID = {} deleted", itemId);
    }

    @Override
    public List<ItemResponse> getItems(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {

            log.debug("Searching all items");
            return itemRepository.findAll()
                    .stream()
                    .map(ItemResponse::new)
                    .collect(Collectors.toList());
        }

        log.debug("Searching all items by name = {}", itemName);

        return itemRepository.findAllByItemNameContaining(itemName)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getMyItems(String username) {

        ProjectUser projectUser = userRepository.findByUsername(username);

        log.debug("Searching items of username = {}" ,username);
        return itemRepository.findAllByProjectUser(projectUser)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse proceedItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        item.setItemStatus(item.getItemStatus().getNextPhase());

        ItemResponse itemResponse = new ItemResponse(itemRepository.save(item));

        log.info("Item with ID = {} promoted", item);
        return itemResponse;
    }
}
