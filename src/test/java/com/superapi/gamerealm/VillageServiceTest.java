package com.superapi.gamerealm;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.VillageRepository;
import com.superapi.gamerealm.service.GridService;
import com.superapi.gamerealm.service.VillageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class VillageServiceTest {
}
  /**

    @Mock
    private VillageRepository villageRepository;

    @Mock
    private GridService gridService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VillageService villageService;

    private List<Coordinates> availableSpots;

    @Before
    public void setUp() {
        // Initialize the mock grid and available spots before each test

        Grid mockGrid = new Grid();
        mockGrid.setWidth(5);
        mockGrid.setHeight(5);

        // Stub the behavior of the GridService.getGrid() method to return the mockGrid
        when(gridService.getGrid()).thenReturn(mockGrid);


        availableSpots = new ArrayList<>();
        for (int x = 0; x < mockGrid.getWidth(); x++) {
            for (int y = 0; y < mockGrid.getHeight(); y++) {
                Coordinates spot = new Coordinates(x, y);
                availableSpots.add(spot);
            }
        }

        // Mock the behavior of the GridService
        when(gridService.getGrid()).thenReturn(mockGrid);

        // Mock the behavior of the VillageService to return the available spots
        when(villageService.getAllAvailableSpots(mockGrid)).thenReturn(availableSpots);
    }
    @Test
    public void testCreateVillageForAccount() {
        // Mock the VillageRepository save method
        when(villageRepository.save(any(Village.class))).thenAnswer(invocation -> {
            Village savedVillage = invocation.getArgument(0);
            // In a real scenario, you might set an ID or perform other operations here.
            return savedVillage;
        });

        // Create an account (you may need to mock the account creation logic if needed)
        Account mockAccount = new Account();
        // Set up other properties for the account as needed for the test

        // Call the method to create the village
        Village createdVillage = villageService.createVillageForAccount(mockAccount);

        // Assertion: Verify that the village is created and saved with correct coordinates
        assertNotNull(createdVillage);
        assertEquals(2, createdVillage.getCoordinates().getX()); // Center X coordinate for the 5x5 grid
        assertEquals(2, createdVillage.getCoordinates().getY()); // Center Y coordinate for the 5x5 grid

        // Verify that the villageRepository.save() method is called the expected number of times
        int expectedVillageCount = Math.min(25, availableSpots.size());
        verify(villageRepository, times(expectedVillageCount)).save(any(Village.class));
    }

    @Test
    public void testGetAllAvailableSpots() {
        // Call the method to get all available spots
        List<Coordinates> result = villageService.getAllAvailableSpots(gridService.getGrid());

        // Assertion: Verify that the list of available spots is correct
        assertNotNull(result);
        assertEquals(availableSpots.size(), result.size());
    }
}



**/


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

