package com.superapi.gamerealm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.VillageRepository;
import com.superapi.gamerealm.service.GridService;
import com.superapi.gamerealm.service.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VillageServiceTest {

    @Mock
    private GridService gridService;

    @Mock
    private VillageRepository villageRepository;

    @InjectMocks
    private VillageService villageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateVillageWithUniqueCoordinates() {
        // Arrange: Create a 5x5 grid with 25 spaces
        Grid grid = new Grid(5, 5);
        when(gridService.getGrid()).thenReturn(grid);

        // Set to keep track of coordinates already used
        Set<Coordinates> usedCoordinates = new HashSet<>();

        // Act & Assert: Create 25 villages and verify their uniqueness
        for (int i = 0; i < 25; i++) {
            // Create a new account for each village (you can replace this with your account logic)
            Account account = new Account();

            // Call the method under test
            Village newVillage = villageService.createVillageForAccount(account);

            // Verify that the village has a unique set of coordinates
            Coordinates villageCoordinates = newVillage.getCoordinates();
            assertFalse(usedCoordinates.contains(villageCoordinates), "Duplicate coordinates found for village: " + newVillage.getId());
            usedCoordinates.add(villageCoordinates);

            // Mock the village repository to return the newly created village
            when(villageRepository.save(any(Village.class))).thenReturn(newVillage);
        }

        // Assert: Verify that the villageRepository.save() method was called 25 times
        verify(villageRepository, times(25)).save(any(Village.class));
    }

    @Test
    public void testCreateVillageNoAvailableSpot() {
        // Arrange: Create a 1x1 grid with no available spots
        Grid grid = new Grid(1, 1);
        when(gridService.getGrid()).thenReturn(grid);

        // Act & Assert: Creating a village should throw a RuntimeException
        assertThrows(RuntimeException.class, () -> villageService.createVillageForAccount(new Account()));
    }

    @Test
    public void testCreateVillageAtGridCenter() {
        // Arrange: Create a 3x3 grid with center spot available
        Grid grid = new Grid(3, 3);
        when(gridService.getGrid()).thenReturn(grid);

        // Act: Create a village and get its coordinates
        Account account = new Account();
        Village newVillage = villageService.createVillageForAccount(account);
        Coordinates villageCoordinates = newVillage.getCoordinates();

        // Assert: The village should be created at the center spot
        assertEquals(1, villageCoordinates.getX());
        assertEquals(1, villageCoordinates.getY());
    }

    // Add more test cases as needed to cover different scenarios
}






/**
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


**/

