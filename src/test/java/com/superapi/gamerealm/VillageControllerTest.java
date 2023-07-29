package com.superapi.gamerealm;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.VillageRepository;
import com.superapi.gamerealm.service.VillageService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, TestResultLoggerExtension.class})
class VillageServiceTest {

    @InjectMocks
    private VillageService villageService;

    @Mock
    private VillageRepository villageRepository;

    @Before("")
    public void setup() {
        // Create mock for VillageRepository
        villageRepository = mock(VillageRepository.class);

        // Inject the mock into VillageService
        villageService = new VillageService(villageRepository);
    }

    @Test
    void testCalculateElapsedHours() {
        Date now = new Date();
        Date lastUpdateTime = new Date(now.getTime() - 1000 * 3600); // 1 hour ago
        long elapsedHours = villageService.calculateElapsedHours(lastUpdateTime, now);
        assertEquals(1, elapsedHours);
    }

    @Test
    void testCalculateResourcesProduced() {
        Village village = new Village();

        // Set the village's building levels
        Map<String, Integer> buildingLevels = new HashMap<>();
        buildingLevels.put("farms", 2);
        buildingLevels.put("lumbers", 3);
        village.setBuildings(buildingLevels);

        // Calculate resources produced for 3 hours
        long elapsedHours = 3;
        Map<String, Long> resourcesProduced = villageService.calculateResourcesProduced(village, elapsedHours);

        // Verify the calculated resources for each building type
        assertEquals(6000, resourcesProduced.get("farms"));   // 1000 (rate) * 2 (level) * 3 (hours)
        assertEquals(4500, resourcesProduced.get("lumbers")); // 500 (rate) * 3 (level) * 3 (hours)
        assertEquals(0, resourcesProduced.getOrDefault("rockMines", 0L)); // The village has no rockMines
        assertEquals(0, resourcesProduced.getOrDefault("goldMines", 0L)); // The village has no goldMines
    }

    @Test
    void testUpdateVillageResources() {
        Village village = new Village();

        // Set the village's initial resources
        Map<String, Long> currentResources = new HashMap<>();
        currentResources.put("farms", 1000L);
        currentResources.put("lumbers", 500L);
        village.setResources(currentResources);

        // Resources produced for 3 hours
        Map<String, Long> resourcesProduced = new HashMap<>();
        resourcesProduced.put("farms", 6000L);
        resourcesProduced.put("lumbers", 4500L);

        // Update the village resources
        Date now = new Date();
        villageService.updateVillageResources(village, resourcesProduced, now);

        // Verify the updated resources
        Map<String, Long> updatedResources = village.getResources();
        assertEquals(7000L, updatedResources.get("farms"));   // 1000 (initial) + 6000 (produced)
        assertEquals(5000L, updatedResources.get("lumbers")); // 500 (initial) + 4500 (produced)
        assertEquals(0L, updatedResources.getOrDefault("rockMines", 0L)); // The village has no rockMines
        assertEquals(0L, updatedResources.getOrDefault("goldMines", 0L)); // The village has no goldMines
    }
}


