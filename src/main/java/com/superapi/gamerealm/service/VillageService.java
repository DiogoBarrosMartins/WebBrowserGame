package com.superapi.gamerealm.service;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VillageService {
    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final GridService gridService;
    private final ResourceService resourceService;
    private final ResourcesRepository resourcesRepository;

    @Autowired
    public VillageService(VillageRepository villageRepository, GridService gridService, ModelMapper modelMapper, ResourceService resourceService, ResourcesRepository resourcesRepository) {
        this.villageRepository = villageRepository;
        this.gridService = gridService;
        this.modelMapper = modelMapper;
        this.resourceService = resourceService;
        this.resourcesRepository = resourcesRepository;
    }

    // wtf this is the grid responsibility
    public List<Coordinates> getAllAvailableSpots(Grid grid) {
        List<Coordinates> availableSpots = new ArrayList<>();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Coordinates coordinates = getCoordinatesAt(grid, x, y);
                if (coordinates != null && !coordinates.hasVillage()) {
                    availableSpots.add(coordinates);
                }
            }
        }

        return availableSpots;
    }

    public Village createVillageForAccount(Account account) {
        Grid grid = gridService.getGrid();
        List<Coordinates> availableSpots = getAllAvailableSpots(grid);

        if (availableSpots.isEmpty()) {
            throw new RuntimeException("No available spot for village.");
        }

        // Select a random available spot from the list
        int randomIndex = (int) (Math.random() * availableSpots.size());
        Coordinates spot = availableSpots.get(randomIndex);

        spot.setHasVillage(true); // Update the grid to indicate that a village has been created at the selected coordinate

        // Create the new Village entity with the selected coordinates
        Village newVillage = new Village(spot);
        newVillage.setAccount(account);
        initializeDefaultResources(newVillage);
        initializeDefaultBuildings(newVillage);

        villageRepository.save(newVillage);


        // Remove the selected spot from the list to avoid duplicates
        availableSpots.remove(randomIndex);

        return newVillage;
    }

    private void initializeDefaultBuildings(Village village) {
        List<Building> buildings = new ArrayList<>();

        for (BuildingType type : BuildingType.values()) {
            int numberOfBuildings = (type.ordinal() < 4) ? 4 : 1;
            for (int i = 0; i < numberOfBuildings; i++) {
                Building building = new Building(type, village);
                buildings.add(building);
            }
        }

        village.setBuildings(buildings);
    }


    private void initializeDefaultResources(Village village) {
        Resources resources = new Resources(TypeOfResource.WHEAT, BigDecimal.valueOf(1000L));
        resources.setAmount(TypeOfResource.WOOD, BigDecimal.valueOf(1000L));
        resources.setAmount(TypeOfResource.STONE, BigDecimal.valueOf(500L));
        resources.setAmount(TypeOfResource.GOLD, BigDecimal.valueOf(500L));
        village.setResources(resources);
    }

    private Coordinates getCoordinatesAt(Grid grid, int x, int y) {
        return grid.getVillageCoordinates().stream()
                .filter(coordinates -> coordinates.getX() == x && coordinates.getY() == y)
                .findFirst()
                .orElse(null);
    }


    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        Village createdVillage = villageRepository.save(village);
        return modelMapper.map(createdVillage, VillageDTO.class);
    }


    // Method to get all villages as DTOs
    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(VillageMapper::toDTO) // Use VillageMapper.toDTO here
                .collect(Collectors.toList());
    }



    /**
     *
     *
     VILLAGE NO ARGS CONSTRUCTOR
     2023-08-01T23:25:19.850+01:00 ERROR 17244 --- [nio-8080-exec-4] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.IllegalArgumentException: Unsupported building type: PUB] with root cause

     java.lang.IllegalArgumentException: Unsupported building type: PUB
     at com.superapi.gamerealm.model.buildings.BuildingType.getResourceName(BuildingType.java:38) ~[classes/:na]
     at com.superapi.gamerealm.service.ResourceService.updateResourcesAndLastUpdated(ResourceService.java:41) ~[classes/:na]
     at com.superapi.gamerealm.service.VillageService.getVillageByAccountUsername(VillageService.java:142) ~[classes/:na]
     at com.superapi.gamerealm.controller.VillageController.getVillageByAccountUsername(VillageController.java:36) ~[classes/:na]
     at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
     at java.base/java.lang.reflect.Method.invoke(Method.java:578) ~[na:na]
     at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:253) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:181) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:918) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:830) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1086) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1011) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564) ~[tomcat-embed-core-10.1.11.jar:6.0]
     at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885) ~[spring-webmvc-6.1.0-M3.jar:6.1.0-M3]
     at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.11.jar:6.0]
     at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51) ~[tomcat-embed-websocket-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.0-M3.jar:6.1.0-M3]
     at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:166) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:341) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:894) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1740) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.11.jar:10.1.11]
     at java.base/java.lang.Thread.run(Thread.java:1623) ~[na:na]
**/


    public VillageDTO getVillageByAccountUsername(String username) {
        List<Village> villages = villageRepository.findByAccountUsername(username);

        if (villages.isEmpty()) {
            throw new RuntimeException("Village not found for account username: " + username);
        }

        Village village = villages.get(0);
        // Calculate resources produced during the elapsed hours
        resourceService.updateResourcesAndLastUpdated(village);

        return VillageMapper.toDTO(village);
    }
// {
//    "id": 1,
//    "x": 2,
//    "y": 0,
//    "name": "default name",
//    "accountId": 1,
//    "lastUpdated": "2023-08-01T22:22:46.947+00:00",
//    "wood": 0,
//    "wheat": 1000.00,
//    "stone": 0,
//    "gold": 0
//}
    public VillageDTO getVillageById(Long villageId) {
        Village village = villageRepository.findById(villageId)
                .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId));
        return VillageMapper.toDTO(village);
    }


}
