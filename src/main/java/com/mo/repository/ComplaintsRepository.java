package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.Complaints;

public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {

}
