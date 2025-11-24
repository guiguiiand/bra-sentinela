package com.projeto.complaintservice.repository;

import com.projeto.complaintservice.entity.LocationCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCityRepository extends JpaRepository<LocationCity, Long> {
}
