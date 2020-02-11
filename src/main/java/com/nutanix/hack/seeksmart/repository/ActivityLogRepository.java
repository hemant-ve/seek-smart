package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.ActivityLog;
import com.nutanix.hack.seeksmart.pojo.response.ActivityLogCustomObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
     
     @Query(value = "SELECT id , count(id) , sum(isConcur) FROM ActivityLog  where timeStamp >= ?1 and timeStamp <= ?2 order by postId",nativeQuery = true)
     List<ActivityLogCustomObject> findActivitiesInTimeRange(Long from , Long to);
}
