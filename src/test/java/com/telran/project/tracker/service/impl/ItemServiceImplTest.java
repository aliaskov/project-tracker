package com.telran.project.tracker.service.impl;

import com.telran.project.tracker.model.entity.Item;
import com.telran.project.tracker.model.entity.ItemStatus;
import com.telran.project.tracker.model.entity.ItemType;
import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.model.web.ItemRequest;
import com.telran.project.tracker.model.web.ItemResponse;
import com.telran.project.tracker.repository.ItemRepository;
import com.telran.project.tracker.repository.ProjectUserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    private static final ProjectUser DEFAULT_USER =

            ProjectUser.builder()
                    .id(1L)
                    .firstName("FirstName")
                    .lastName("LastName")
                    .password("123456")
                    .username("username")
                    .build();


    private static final Item DEFAULT_ITEM =

            Item.builder()
                    .itemName("Default_item")
                    .itemStatus(ItemStatus.TODO)
                    .itemType(ItemType.TASK)
                    .createdDate(new Date())
                    .projectUser(DEFAULT_USER)
                    .id(1L)
                    .build();

    private static final Item DEFAULT_ITEM_2 =

            Item.builder()
                    .itemName("Default_item_2")
                    .itemStatus(ItemStatus.TODO)
                    .itemType(ItemType.BUG)
                    .createdDate(new Date())
                    .projectUser(DEFAULT_USER)
                    .id(2L)
                    .build();

    private static final List<Item> DEFAULT_ITEMS_LIST =

            Stream
            .of(DEFAULT_ITEM, DEFAULT_ITEM_2)
            .collect(Collectors.toList());


    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ProjectUserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;


    @Test
    public void createItem_positive() {
        ItemRequest itemRequest = ItemRequest.builder()
                .itemName(DEFAULT_ITEM.getItemName())
                .itemType(DEFAULT_ITEM.getItemType())
                .build();

        Mockito.when(userRepository.findByUsername(DEFAULT_USER.getUsername())).thenReturn(DEFAULT_USER);
        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(DEFAULT_ITEM);

        ItemResponse response = itemService.createItem(itemRequest, DEFAULT_USER.getUsername());

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCreatedBy(), DEFAULT_USER.getUsername());
        Assert.assertEquals(response.getItemName(), DEFAULT_ITEM.getItemName());
        Assert.assertEquals(response.getItemStatus(), DEFAULT_ITEM.getItemStatus());
        Assert.assertEquals(response.getItemType(), DEFAULT_ITEM.getItemType());
        Assert.assertEquals(response.getId(), DEFAULT_ITEM.getId());
    }

    @Test
    public void updateItem_positive() {

        Item changedItem = Item.builder()
                ///Changed
                .itemName("Another_item_name")
                .itemType(ItemType.BUG)

                //Same
                .itemStatus(DEFAULT_ITEM.getItemStatus())
                .createdDate(DEFAULT_ITEM.getCreatedDate())
                .projectUser(DEFAULT_ITEM.getProjectUser())
                .id(DEFAULT_ITEM.getId())
                .build();


        Mockito.when(itemRepository.findById(DEFAULT_ITEM.getId())).thenReturn(Optional.of(DEFAULT_ITEM));
        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(changedItem);

        ItemRequest itemRequest = ItemRequest.builder()
                .itemName(changedItem.getItemName())
                .itemType(changedItem.getItemType())
                .build();


        ItemResponse response = itemService.updateItem(itemRequest, changedItem.getId());
        Assert.assertNotNull(response);

        //New changes
        Assert.assertEquals(response.getItemType(), changedItem.getItemType());
        Assert.assertEquals(response.getItemName(), changedItem.getItemName());

        ////Old changes
        Assert.assertEquals(response.getCreatedBy(), changedItem.getProjectUser().getUsername());
        Assert.assertEquals(response.getItemStatus(), changedItem.getItemStatus());
        Assert.assertEquals(response.getId(), changedItem.getId());
    }

    @Test
    public void deleteItem_positive() {
        Mockito
                .doNothing()
                .when(itemRepository)
                .deleteById(DEFAULT_ITEM.getId());

        itemService.deleteItem(DEFAULT_ITEM.getId());
    }

    @Test
    public void getItemsWithItemName_positive() {

        Mockito
                .when(itemRepository.findAllByItemNameContaining(DEFAULT_ITEM_2.getItemName()))
                .thenReturn(Collections.singletonList(DEFAULT_ITEM_2));

        List<ItemResponse> items = itemService.getItems(DEFAULT_ITEM_2.getItemName());
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(DEFAULT_ITEM_2.getItemName(), items.get(0).getItemName());
        Assert.assertEquals(DEFAULT_ITEM_2.getItemType(), items.get(0).getItemType());
    }

    @Test
    public void getItemsWithoutItemName_positive() {
        Mockito.when(itemRepository.findAll()).thenReturn(DEFAULT_ITEMS_LIST);

        List<ItemResponse> items = itemService.getItems(null);
        Assert.assertEquals(2, items.size());
    }


    @Test
    public void getMyItems_positive() {
        Mockito.when(userRepository.findByUsername(DEFAULT_USER.getUsername())).thenReturn(DEFAULT_USER);
        Mockito.when(itemRepository.findAllByProjectUser(DEFAULT_USER)).thenReturn(DEFAULT_ITEMS_LIST);

        List<ItemResponse> myItems = itemService.getMyItems(DEFAULT_USER.getUsername());

        Assert.assertEquals(2, myItems.size());
        Assert.assertEquals(myItems.get(0).getCreatedBy(), DEFAULT_USER.getUsername());
        Assert.assertEquals(myItems.get(1).getCreatedBy(), DEFAULT_USER.getUsername());
    }

    @Test
    public void proceedItem_positive() {
        Item changedItem = Item.builder()
                //To be changed
                .itemStatus(ItemStatus.IN_PROGRESS)

                //Same
                .itemName(DEFAULT_ITEM.getItemName())
                .itemType(DEFAULT_ITEM.getItemType())
                .createdDate(DEFAULT_ITEM.getCreatedDate())
                .projectUser(DEFAULT_ITEM.getProjectUser())
                .id(DEFAULT_ITEM.getId())
                .build();

        Mockito.when(itemRepository.findById(DEFAULT_ITEM.getId())).thenReturn(Optional.of(changedItem));
        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(changedItem);

        ItemResponse itemResponse = itemService.proceedItem(DEFAULT_ITEM.getId());
        Assert.assertNotNull(itemResponse);
        Assert.assertEquals(itemResponse.getItemStatus(), changedItem.getItemStatus());
    }

}
