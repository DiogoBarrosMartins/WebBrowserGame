package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.TroopDTO;
import com.superapi.gamerealm.dto.TroopMapper;
import com.superapi.gamerealm.model.troop.Troop;
import com.superapi.gamerealm.repository.TroopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TroopServiceImpl {
        private final TroopRepository troopRepository;
private final ResourceService resourceService;

        public TroopServiceImpl(TroopRepository troopRepository, ResourceService resourceService) {
            this.troopRepository = troopRepository;
            this.resourceService = resourceService;
        }
    public List<Troop> getAvailableTroops() {
        // Get all troops that have finished training
        return troopRepository.findAll().stream()
                .filter(Troop::isTrained)
                .collect(Collectors.toList());
    }

        public TroopDTO createTroop(TroopDTO troopDTO) {
            Troop troop = TroopMapper.toEntity(troopDTO);
            troop = troopRepository.save(troop);
            return TroopMapper.toDTO(troop);
        }

        public List<TroopDTO> getAllTroops() {
            List<Troop> troops = troopRepository.findAll();
            return troops.stream()
                    .map(TroopMapper::toDTO)
                    .collect(Collectors.toList());
        }

        public TroopDTO getTroopById(Long id) {
            Troop troop = troopRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Troop not found"));
            return TroopMapper.toDTO(troop);
        }
    }
