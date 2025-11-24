package com.projeto.complaintservice.controller;

import com.projeto.complaintservice.controller.dto.*;
import com.projeto.complaintservice.service.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComplaintControllerTest {

    private ComplaintService complaintService;
    private ComplaintController controller;

    @BeforeEach
    void setUp() {
        complaintService = mock(ComplaintService.class);
        controller = new ComplaintController(complaintService);
    }

    @Test
    void createComplaint_returnsCreated() {
        var req = ComplaintCreateRequest.builder()
            .description("desc")
            .date("2025-11-24")
            .time("10:00")
            .channel("web")
            .attackerName("John Doe")
            .value("1000")
            .locationCity(null)
            .files(List.of())
            .customerId("customer-1")
            .build();
        var resp = ComplaintCreateResponse.builder().protocol("PROT-123").build();
        when(complaintService.createComplaint(any())).thenReturn(resp);

        var result = controller.createComplaint(req);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }

    @Test
    void getComplaint_returnsOk() {
        var resp = ComplaintResponse.builder()
            .statusComplaint("ABERTA")
            .createdDate("2025-11-24")
            .descriptionComplaint("desc")
            .protocolNumber("PROT-1")
            .message("msg")
            .build();
        when(complaintService.getComplaintById("PROT-1")).thenReturn(resp);

        var result = controller.getComplaint("PROT-1");

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }

    @Test
    void getAllComplaints_returnsList() {
        when(complaintService.getAllComplaints()).thenReturn(List.of(ComplaintListResponse.builder()
            .id(UUID.randomUUID())
            .protocol("PROT-1")
            .date("2025-11-24")
            .channel("web")
            .status("ABERTA")
            .build()));

        var result = controller.getAllComplaints();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void getComplaintDetailsByCustomerId_returnsDetail() {
        UUID id = UUID.randomUUID();
        var detail = ComplaintDetailResponse.builder()
            .protocol("PROT-1")
            .status("ABERTA")
            .createdAt("2025-11-24")
            .description("desc")
            .channel("web")
            .attackerName("John Doe")
            .value("1000")
            .locationCity(null)
            .files(List.of())
            .build();
        when(complaintService.getComplaintDetailsByCustomerId(id)).thenReturn(detail);

        var result = controller.getComplaintDetailsByCustomerId(id);

        assertEquals(detail, result);
    }
}
