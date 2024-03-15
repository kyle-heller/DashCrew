package com.rollcall.web.repository;

import com.rollcall.web.models.Event;
import com.rollcall.web.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT g FROM Event g WHERE " +
            "LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.type) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.zip) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Group> searchGroups(@Param("query") String query);

}
