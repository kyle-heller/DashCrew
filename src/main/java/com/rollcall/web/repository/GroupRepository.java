package com.rollcall.web.repository;

import com.rollcall.web.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByTitle(String url);
    @Query("SELECT g FROM Group g WHERE " +
            "LOWER(g.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.createdBy.username) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Group> searchGroups(@Param("query") String query);

}
