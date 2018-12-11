package com.telran.project.tracker.model.entity;

import org.junit.Assert;
import org.junit.Test;

public class ItemStatusTest {

    @Test
    public void nextPhaseTest() {
        ItemStatus itemStatus = ItemStatus.TODO;

        ItemStatus nextPhaseStatus = itemStatus.getNextPhase();
        Assert.assertEquals(nextPhaseStatus, ItemStatus.IN_PROGRESS);
    }


    @Test
    public void nextPhaseTestFromInProgressToDone() {
        ItemStatus itemStatus = ItemStatus.IN_PROGRESS;

        ItemStatus nextPhaseStatus = itemStatus.getNextPhase();
        Assert.assertEquals(nextPhaseStatus, ItemStatus.DONE);
    }


    @Test
    public void nextPhaseTestDoneToDone() {
        ItemStatus itemStatus = ItemStatus.DONE;

        ItemStatus nextPhaseStatus = itemStatus.getNextPhase();
        Assert.assertEquals(nextPhaseStatus, ItemStatus.DONE);
    }
}
