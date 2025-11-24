package com.projeto.complaintservice.service;


import com.projeto.complaintservice.config.ComplaintProducer;
import com.projeto.complaintservice.controller.dto.*;
import com.projeto.complaintservice.entity.ComplaintEntity;
import com.projeto.complaintservice.entity.LocationCity;
import com.projeto.complaintservice.exception.ComplaintException;
import com.projeto.complaintservice.repository.ComplaintRepository;
import com.projeto.complaintservice.repository.LocationCityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class ComplaintService {
    public static final String PROT = "PROT-";
    private final ComplaintRepository complaintRepository;
    private final LocationCityRepository locationCityRepository;
    private final ComplaintProducer complaintProducer;

    public ComplaintCreateResponse createComplaint(ComplaintCreateRequest complaintRequest) {
        try {
            return saveComplaint(complaintRequest);
        } catch (ComplaintException complaintException) {
            log.error("Error creating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }
    }

    public ComplaintResponse getComplaintById(String id) {
        try {

            return getCompaint(id);
        } catch (ComplaintException complaintException) {
            log.error("Error fetching complaint: {}", complaintException.getMessage());
            throw complaintException;
        }

    }

    private ComplaintCreateResponse saveComplaint(ComplaintCreateRequest complaintRequest) {
        try {
            String protocolNumber = getString();


            LocationCity locationCity = null;
            if (complaintRequest.getLocationCity() != null) {

                if (complaintRequest.getLocationCity().getId() != null && complaintRequest.getLocationCity().getId() > 0) {
                    locationCity = locationCityRepository.findById(complaintRequest.getLocationCity().getId())
                            .orElse(null);
                }

                if (locationCity == null) {
                    LocationCity newLocationCity = new LocationCity();
                    newLocationCity.setCity(complaintRequest.getLocationCity().getCity());
                    newLocationCity.setState(complaintRequest.getLocationCity().getState());
                    locationCity = locationCityRepository.save(newLocationCity);
                }
            }


            ComplaintEntity complaintEntity = ComplaintEntity.builder()
                    .customerId(complaintRequest.getCustomerId())
                    .descriptionComplaint(complaintRequest.getDescription())
                    .description(complaintRequest.getDate())
                    .message("Sua solicitação está aguardando análise!")
                    .channel(complaintRequest.getChannel())
                    .attackerName(complaintRequest.getAttackerName())
                    .value(complaintRequest.getValue())
                    .locationCity(locationCity)
                    .statusComplaint("ABERTA")
                    .createdDate(LocalDateTime.now().toString())
                    .protocolNumber(protocolNumber)
                    .files(complaintRequest.getFiles())
                    .build();


            ComplaintEntity savedEntity = complaintRepository.save(complaintEntity);

            log.info("Complaint created with protocol: {}", savedEntity.getProtocolNumber());

            ComplaintCreateResponse response = ComplaintCreateResponse.builder()
                    .protocol(savedEntity.getProtocolNumber())
                    .build();

            ProtocolGeneratedEvent event = ProtocolGeneratedEvent.builder()
                    .protocolNumber(savedEntity.getProtocolNumber())
                    .complaintId("123123123124")
                    .userName("Maria Silva")
                    .userEmail("mail@gmail.com")
                    .generatedAt(LocalDateTime.now().toString())
                    .build();
            complaintProducer.sendComplaintCreated(event);
            return response;

        } catch (Exception e) {
            log.error("Error saving complaint: {}", e.getMessage());
            throw new ComplaintException("Failed to create complaint", "NotCreatedError");
        }
    }

    public ComplaintUpdateResponse updateComplaint(String id, ComplaintUpdateRequest complaintRequest) {
        ComplaintEntity complaintEntity = complaintRepository.findByProtocolNumber(id);
        if (complaintEntity == null) {
            try {
                java.util.UUID uuid = java.util.UUID.fromString(id);
                complaintEntity = complaintRepository.findById(uuid).orElse(null);
            } catch (IllegalArgumentException e) {
            }
        }
        if (complaintEntity == null) {
            throw new ComplaintException("Complaint not found", "NotFoundError");
        }
        complaintEntity.setStatusComplaint(complaintRequest.statusComplaint());
        complaintEntity.setMessage(complaintRequest.complaintMessage());
        complaintEntity.setInternalMessage(complaintRequest.internalComment());

        complaintRepository.save(complaintEntity);
        return new ComplaintUpdateResponse("Complaint updated successfully");
    }

    public List<ComplaintListResponse> getAllComplaints() {
        return findAllMapping();
    }

    private static String getString() {
        return PROT + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private List<ComplaintListResponse> findAllMapping() {
        List<ComplaintEntity> complaintEntitys = complaintRepository.findAll();

        return complaintEntitys.stream().map(complaintEntity -> {
                String location = null;
                if (complaintEntity.getLocationCity() != null) {
                    location = complaintEntity.getLocationCity().getCity() + " - " + complaintEntity.getLocationCity().getState();
                }
                return ComplaintListResponse.builder()
                        .protocol(complaintEntity.getProtocolNumber())
                        .status(complaintEntity.getStatusComplaint())
                        .date(complaintEntity.getCreatedDate())
                        .id(complaintEntity.getId())
                        .channel(complaintEntity.getChannel())
                        .location(location)
                        .description(complaintEntity.getDescriptionComplaint())
                        .createdAt(complaintEntity.getCreatedDate())
                        .files(complaintEntity.getFiles())
                        .build();
        }).toList();
    }

    private ComplaintResponse getCompaint(String id) {
        ComplaintEntity complaintEntity = complaintRepository.findByProtocolNumber(id);

        if (complaintEntity == null) {
            throw new ComplaintException("Complaint not found", "NotFoundError");
        }
        return ComplaintResponse.builder()
                .statusComplaint(complaintEntity.getStatusComplaint())
                .createdDate(complaintEntity.getCreatedDate())
                .descriptionComplaint(complaintEntity.getDescriptionComplaint())
                .protocolNumber(complaintEntity.getProtocolNumber())
                .message(complaintEntity.getMessage())
                .build();
    }

    public ComplaintDetailResponse getComplaintDetailsByCustomerId(UUID id) {
        Optional<ComplaintEntity> complaint = complaintRepository.findById(id);

        if (complaint.isPresent()) {
            ComplaintEntity complaintEntity = complaint.get();
            return ComplaintDetailResponse.builder()
                    .protocol(complaintEntity.getProtocolNumber())
                    .status(complaintEntity.getStatusComplaint())
                    .description(complaintEntity.getDescriptionComplaint())
                    .createdAt(complaintEntity.getCreatedDate())
                    .channel(complaintEntity.getChannel())
                    .attackerName(complaintEntity.getAttackerName())
                    .locationCity(complaintEntity.getLocationCity())
                    .files(complaintEntity.getFiles())
                    .value(complaintEntity.getValue())
                    .build();
        } else {
            throw new ComplaintException("Complaint not found for customer ID: " + id, "NotFoundError");
        }

    }
}


