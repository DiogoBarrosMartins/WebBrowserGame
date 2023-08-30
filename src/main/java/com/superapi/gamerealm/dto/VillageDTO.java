package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.dto.building.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.building.ResourceBuildingDTO;

import java.util.Date;
import java.util.List;

public class VillageDTO{
        private Long id;
        private int x;
        private int y;
        private String name;
        private Long accountId;
        private Date lastUpdated;
        private List<ResourcesDTO> resourcesDTO;
        private List<ResourceBuildingDTO> resourceBuildings;
        private List<NonResourceBuildingDTO> nonResourceBuildings;
        private List<ConstructionDTO> constructionDTOS;

        private List<TroopTrainingQueueDTO> troopTrainingQueueDTOS;
        private List<VillageTroopsDTO> villageTroopDTOS;
        public VillageDTO() {
        }

        public VillageDTO(Long id, int x, int y, String name, Long accountId, Date lastUpdated) {
                this.id = id;
                this.x = x;
                this.y = y;
                this.name = name;
                this.accountId = accountId;
                this.lastUpdated = lastUpdated;

        }

    public VillageDTO(long id, int x, int y, String name) { this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
    }


    public void setLastUpdated(Date lastUpdated) {
                this.lastUpdated = lastUpdated;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public int getX() {
                return x;
        }

        public void setX(int x) {
                this.x = x;
        }

        public int getY() {
                return y;
        }

        public void setY(int y) {
                this.y = y;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Long getAccountId() {
                return accountId;
        }

        public void setAccountId(Long accountId) {
                this.accountId = accountId;
        }

        public Date getLastUpdated() {
                return lastUpdated;
        }

        public List<ResourcesDTO> getResourcesDTO() {
                return resourcesDTO;
        }

        public void setResourcesDTO(List<ResourcesDTO> resourcesDTO) {
                this.resourcesDTO = resourcesDTO;
        }

        public List<ResourceBuildingDTO> getResourceBuildings() {
                return resourceBuildings;
        }

        public void setResourceBuildings(List<ResourceBuildingDTO> resourceBuildings) {
                this.resourceBuildings = resourceBuildings;
        }

        public List<ConstructionDTO> getConstructionDTOS() {
                return constructionDTOS;
        }

        public void setConstructionDTOS(List<ConstructionDTO> constructionDTOS) {
                this.constructionDTOS = constructionDTOS;
        }

        public List<NonResourceBuildingDTO> getNonResourceBuildings() {
                return nonResourceBuildings;
        }

        public void setNonResourceBuildings(List<NonResourceBuildingDTO> nonResourceBuildings) {
                this.nonResourceBuildings = nonResourceBuildings;
        }

    public List<TroopTrainingQueueDTO> getTroopTrainingQueueDTOS() {
        return troopTrainingQueueDTOS;
    }

    public void setTroopTrainingQueueDTOS(List<TroopTrainingQueueDTO> troopTrainingQueueDTOS) {
        this.troopTrainingQueueDTOS = troopTrainingQueueDTOS;
    }

    public List<VillageTroopsDTO> getVillageTroopDTOS() {
        return villageTroopDTOS;
    }

    public void setVillageTroopDTOS(List<VillageTroopsDTO> villageTroopDTOS) {
        this.villageTroopDTOS = villageTroopDTOS;
    }
}
