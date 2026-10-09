package com.pathforge.repository;

import com.pathforge.model.Roadmap;
import com.pathforge.model.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, Long> {
    List<RoadmapItem> findByRoadmapOrderByPhaseNumberAscOrderIndexAsc(Roadmap roadmap);
    List<RoadmapItem> findByRoadmapAndStatus(Roadmap roadmap, String status);
}
