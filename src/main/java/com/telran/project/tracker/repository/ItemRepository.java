package com.telran.project.tracker.repository;

import com.telran.project.tracker.model.entity.Item;
import com.telran.project.tracker.model.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByProjectUser(ProjectUser projectUser);
    List<Item> findAllByItemNameContaining(String name);
}
