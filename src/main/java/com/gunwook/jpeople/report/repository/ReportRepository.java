package com.gunwook.jpeople.report.repository;

import com.gunwook.jpeople.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
