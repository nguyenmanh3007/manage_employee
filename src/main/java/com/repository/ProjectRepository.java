package com.repository;

import com.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
        Project findByProjectId(int id);

        Project findByCode(String code);
        boolean existsByCode(String code);
}
