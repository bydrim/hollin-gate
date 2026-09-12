package com.bydrim.hollingate.services;

import com.bydrim.hollingate.entities.Tracker;
import com.bydrim.hollingate.entities.TrackerTrigger;
import com.bydrim.hollingate.exceptions.TooManyTrialException;
import com.bydrim.hollingate.repositories.TrackerRepository;
import com.bydrim.hollingate.repositories.TrackerTriggerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class TrackerService {
    private static final Random rnd = new Random();
    private final TrackerRepository trackerRepository;
    private final TrackerTriggerRepository trackerTriggerRepository;

    public TrackerService(TrackerRepository trackerRepository, TrackerTriggerRepository trackerTriggerRepository) {
        this.trackerRepository = trackerRepository;
        this.trackerTriggerRepository = trackerTriggerRepository;
    }

    public List<Tracker> listTrackers() {
        return trackerRepository.findAllDesc();
    }

    public List<TrackerTrigger> listTriggers(String trackerId) {
        return trackerTriggerRepository.findAllDesc(trackerId);
    }

    /**
     * 3 bytes = 4 letters
     * 3 * 8 = 4 * 6
     * byte = 2^8 / base64 = 2^6
     * @return randomly generated 4 characters sized string
     */
    private String rndId() {
        byte[] bytes = new byte[3];
        rnd.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Tracker saveNewTracker(String description) throws TooManyTrialException {
        for(int i = 0; i < 10; i++) {
            String newId = rndId();
            if (trackerRepository.existsById(newId)) {
                continue;
            }
            return trackerRepository.save(new Tracker(newId, description, OffsetDateTime.now()));
        }
        throw new TooManyTrialException("Could not create a unique id for Tracker after 10 try!");
    }

    public boolean isEmpty() {
        return trackerRepository.count() == 0;
    }

    public List<Tracker> findLast(long count) {
        return trackerRepository.findLast(count);
    }

    @Transactional
    public void delete(String id) {
        trackerTriggerRepository.deleteByTrackerEfficiently(id);
        trackerRepository.deleteEfficiently(id);
    }
}
