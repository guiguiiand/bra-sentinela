package com.projeto.complaintservice.controller;



import com.projeto.complaintservice.controller.dto.*;
import com.projeto.complaintservice.service.ComplaintService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/complaint")
@Slf4j
@AllArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintCreateResponse> createComplaint(@RequestBody ComplaintCreateRequest complaintRequest) {

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(complaintService.createComplaint(complaintRequest));

    }
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(@PathVariable String id) {
        ComplaintResponse complaintResponse = complaintService.getComplaintById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(complaintResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ComplaintUpdateResponse> updateComplaint(@PathVariable String id,
                                                                   @RequestBody ComplaintUpdateRequest complaintRequest) {
        ComplaintUpdateResponse complaintUpdateResponse = complaintService.updateComplaint(id, complaintRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(complaintUpdateResponse);
    }
    @GetMapping
    public ResponseEntity<List<ComplaintListResponse>> getAllComplaints() {

        return ResponseEntity.status(HttpStatus.OK).body(complaintService.getAllComplaints());
    }

    @GetMapping("/customer/{id}/details")
    public ComplaintDetailResponse getComplaintDetailsByCustomerId(@PathVariable UUID id) {
        return complaintService.getComplaintDetailsByCustomerId(id);
    }

}
