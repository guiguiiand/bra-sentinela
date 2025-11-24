package com.projeto.complaintservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Table(name = "complaints")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @Column(name = "complaint_date")
    private String date;

    @Column(name = "complaint_time")
    private String time;

    private String channel;
    private String attackerName;
    private String value;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_city_id")
    private LocationCity locationCity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "complaint_files",
            joinColumns = @JoinColumn(name = "complaint_id"))
    @Column(name = "file_path")
    private List<String> files = new ArrayList<>();

    @Column(name = "customer_id")
    private String customerId;

    String statusComplaint = "PENDING";
    String createdDate = LocalDate.now().toString();
    String descriptionComplaint;
    String protocolNumber = genaredProtocolNumber();
    String message;
    String internalMessage;

    private String genaredProtocolNumber() {
        return "PROT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

