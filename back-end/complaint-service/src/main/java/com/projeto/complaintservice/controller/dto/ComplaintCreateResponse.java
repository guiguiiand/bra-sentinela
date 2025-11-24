package com.projeto.complaintservice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ComplaintCreateResponse {
    private String protocol;
}
