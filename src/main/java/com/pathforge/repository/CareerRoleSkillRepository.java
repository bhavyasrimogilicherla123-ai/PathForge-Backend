package com.pathforge.repository;

import com.pathforge.model.CareerRole;
import com.pathforge.model.CareerRoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRoleSkillRepository extends JpaRepository<CareerRoleSkill, Long> {
    List<CareerRoleSkill> findByCareerRole(CareerRole careerRole);
    List<CareerRoleSkill> findByCareerRoleId(Long careerRoleId);
}
