package com.projeto.complaintservice.repository;

import com.projeto.complaintservice.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, UUID> {

    ComplaintEntity findByProtocolNumber(String protocolNumber);

    Long countByStatusComplaint(String open);
    
    //Query para relatório detalhado de contagem de reclamações por mês desde uma data específica
    // Usado para gerar relatórios mensais de reclamações
    @Query(value = """
SELECT
    year_month,
    year,
    month,
    month_name,
    COUNT(*) AS total,
    COUNT(*) FILTER (WHERE status_complaint = 'PENDING') AS pending_count,
    COUNT(*) FILTER (WHERE status_complaint = 'ABERTA') AS open_count,
    COUNT(*) FILTER (WHERE status_complaint = 'EM ANÁLISE') AS in_progress_count,
    COUNT(*) FILTER (WHERE status_complaint = 'VA' ||
     '') AS validated_count,
    COUNT(*) FILTER (WHERE status_complaint = 'INCONSISTENT') AS inconsistent_count,
    ROUND(COUNT(*)::::numeric / days_in_month::::numeric, 2) AS avg_per_day
FROM (
    SELECT
        to_char(CAST(created_date AS date), 'YYYY-MM') AS year_month,
        EXTRACT(YEAR FROM CAST(created_date AS date))::::int AS year,
        EXTRACT(MONTH FROM CAST(created_date AS date))::::int AS month,
        to_char(CAST(created_date AS date), 'Month') AS month_name,
        status_complaint,
        EXTRACT(DAY FROM date_trunc('month', CAST(created_date AS date)) + interval '1 month' - interval '1 day')::::int AS days_in_month
    FROM complaints
    WHERE CAST(created_date AS date) >= CAST(:startDate AS date)
) AS monthly_data
GROUP BY year_month, year, month, month_name, days_in_month
ORDER BY year, month
""", nativeQuery = true)
    List<Object[]> countComplaintsSinceDetailed(@Param("startDate") String startDate);

    @Query(value = "SELECT * FROM complaints WHERE CAST(created_date AS date) BETWEEN CAST(:start AS date) AND CAST(:end AS date) ORDER BY created_date", nativeQuery = true)
    List<com.projeto.complaintservice.entity.ComplaintEntity> findByCreatedDateBetween(@org.springframework.data.repository.query.Param("start") String start, @org.springframework.data.repository.query.Param("end") String end);


}
