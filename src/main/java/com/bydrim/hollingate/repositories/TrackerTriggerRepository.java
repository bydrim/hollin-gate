package com.bydrim.hollingate.repositories;

import com.bydrim.hollingate.entities.TrackerTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackerTriggerRepository extends JpaRepository<TrackerTrigger, Long> {
    @Query("select tt from TrackerTrigger tt where tt.tracker.id = ?1 order by tt.triggerDate desc")
    List<TrackerTrigger> findAllDesc(String trackerId);
    @Query("select tt from TrackerTrigger tt where tt.tracker.id = ?1 order by tt.triggerDate desc limit 1")
    Optional<TrackerTrigger> findLast(String trackerId);
}
