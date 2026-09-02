package com.bydrim.hollingate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tracker {
    @Id
    private String id;
    private String description;
    private OffsetDateTime createDate;
    @OneToMany(mappedBy = "tracker", fetch = FetchType.LAZY)
    private Set<TrackerTrigger> trackerTriggers = new HashSet<>();
    private OffsetDateTime lastTriggerDate;

    public Tracker() {}

    public Tracker(String id, String description, OffsetDateTime createDate) {
        this.id = id;
        this.description = description;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public Set<TrackerTrigger> getTrackerTriggers() {
        return trackerTriggers;
    }

    public void setTrackerTriggers(Set<TrackerTrigger> trackerTriggers) {
        this.trackerTriggers = trackerTriggers;
    }

    public OffsetDateTime getLastTriggerDate() {
        return lastTriggerDate;
    }

    public void setLastTriggerDate(OffsetDateTime lastTriggerDate) {
        this.lastTriggerDate = lastTriggerDate;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", lastTriggerDate=" + lastTriggerDate +
                '}';
    }
}
