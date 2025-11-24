package com.projeto.apigateway.service;

import com.projeto.apigateway.config.ComplaintClient;
import com.projeto.apigateway.controller.dto.*;
import com.projeto.apigateway.exceptions.ComplaintException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class ComplaintService {
    private final ComplaintClient complaintClient;

    public ComplaintCreateResponse createComplaint(ComplaintCreateRequest complaintRequest) {
        try {

            return complaintClient.createComplaint(complaintRequest);
        } catch (ComplaintException complaintException) {
            log.error("Error creating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }
    }

    public ComplaintResponse getComplaintById(String id) {
        try {

            return complaintClient.getComplaintById(id);
        } catch (ComplaintException complaintException) {
            log.error("Error fetching complaint: {}", complaintException.getMessage());
            throw complaintException;
        }

    }

    public ComplaintUpdateResponse updateComplaint(String id, ComplaintUpdateRequest complaintRequest) {
        try {
            return complaintClient.updateComplaint(id, complaintRequest);
        } catch (ComplaintException complaintException) {
            log.error("Error updating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }

    }

    public List<ComplaintListResponse> getAllComplaints() {
        return complaintClient.getComplaints();
    }

    public ComplaintDetailResponse getComplaintDetailsByCustomerId(UUID id) {
        return complaintClient.getComplaintDetailsByCustomerId(id);
    }
}
