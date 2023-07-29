package com.superapi.gamerealm.dto;

public class VillageDTO {
        private Integer xCoordinate;
        private Integer yCoordinate;
        private Long accountId;
        private Long id;
        private String name;

        public void setCoordinates(int xCoordinate, int yCoordinate) {
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
        }
        public Integer getxCoordinate() {
                return xCoordinate;
        }

        public void setxCoordinate(Integer xCoordinate) {
                this.xCoordinate = xCoordinate;
        }

        public Integer getyCoordinate() {
                return yCoordinate;
        }

        public void setyCoordinate(Integer yCoordinate) {
                this.yCoordinate = yCoordinate;
        }

        public Long getAccountId() {
                return accountId;
        }

        public void setAccountId(Long accountId) {
                this.accountId = accountId;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }
}


