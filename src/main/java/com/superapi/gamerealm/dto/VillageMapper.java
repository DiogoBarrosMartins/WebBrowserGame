package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.Village;
import org.springframework.stereotype.Component;

@Component
public class VillageMapper {

    public VillageDTO mapToResponseDTO(Village village) {
        VillageDTO dto = new VillageDTO();
        dto.setxCoordinate(village.getXCoordinate());
        dto.setyCoordinate(village.getYCoordinate());
        dto.setAccountId(village.getAccount().getId());
        dto.setId(village.getId());
        dto.setName(village.getName());
        return dto;
    }

    // You can add more mapping methods if needed for other use cases

}

