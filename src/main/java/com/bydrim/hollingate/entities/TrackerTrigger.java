package com.bydrim.hollingate.entities;

import jakarta.persistence.*;

import java.net.URL;
import java.time.OffsetDateTime;

@Entity
public class TrackerTrigger {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "tracker_id", nullable = false)
    private Tracker tracker;
    private OffsetDateTime triggerDate;
    private URL triggerUrl;

    public TrackerTrigger() {}

    public TrackerTrigger(Tracker tracker, OffsetDateTime triggerDate, URL triggerUrl) {
        this.tracker = tracker;
        this.triggerDate = triggerDate;
        this.triggerUrl = triggerUrl;
    }

    public TrackerTrigger(Long id, Tracker tracker, OffsetDateTime triggerDate, URL triggerUrl) {
        this.id = id;
        this.tracker = tracker;
        this.triggerDate = triggerDate;
        this.triggerUrl = triggerUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public OffsetDateTime getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(OffsetDateTime triggerDate) {
        this.triggerDate = triggerDate;
    }

    public URL getTriggerUrl() {
        return triggerUrl;
    }

    public void setTriggerUrl(URL triggerUrl) {
        this.triggerUrl = triggerUrl;
    }
}