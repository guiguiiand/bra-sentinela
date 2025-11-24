package com.projeto.notificationservice.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {
    
    @NotBlank(message = "Destinatário é obrigatório")
    @Email(message = "Email inválido")
    private String to;
    
    @NotBlank(message = "Assunto é obrigatório")
    private String subject;
    
    @NotBlank(message = "Conteúdo é obrigatório")
    private String content;
}