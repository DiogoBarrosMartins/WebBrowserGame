package com.superapi.gamerealm.dto;

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

        public List<NonResourceBuildingDTO> getNonResourceBuildings() {
                return nonResourceBuildings;
        }

        public void setNonResourceBuildings(List<NonResourceBuildingDTO> nonResourceBuildings) {
                this.nonResourceBuildings = nonResourceBuildings;
        }
}
