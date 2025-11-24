package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.*;
import com.projeto.apigateway.service.ComplaintService;
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
    void getAllComplaints_returnsList() {
        when(complaintService.getAllComplaints()).thenReturn(List.of(ComplaintListResponse.builder().protocol("P").build()));

        var resp = controller.getAllComplaints();

        assertEquals(200, resp.getStatusCodeValue());
        assertFalse(resp.getBody().isEmpty());
    }

    @Test
    void getComplaint_returnsItem() {
        when(complaintService.getComplaintById("P")).thenReturn(ComplaintResponse.builder().protocolNumber("P").build());

        var resp = controller.getComplaint("P");

        assertEquals(200, resp.getStatusCodeValue());
    }
}
