package com.superapi.gamerealm.dto;

import java.util.Date;

public class VillageDTO {
        private Long id;
        private int x;
        private int y;
        private String name;
        private Long accountId;
        private Date lastUpdated;

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

        public void setLastUpdated(Date lastUpdated) {
                this.lastUpdated = lastUpdated;
        }
}
