package com.bydrim.hollingate.repositories;

import com.bydrim.hollingate.entities.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackerRepository extends JpaRepository<Tracker, String> {
    @Query("select t from Tracker t order by t.createDate desc")
    List<Tracker> findAllDesc();

    @Query("select t from Tracker t order by t.createDate desc limit ?1")
    List<Tracker> findLast(long count);
}
