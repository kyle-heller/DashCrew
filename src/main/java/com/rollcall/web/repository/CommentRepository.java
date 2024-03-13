package com.rollcall.web.repository;


import com.rollcall.web.models.Game;
import com.rollcall.web.models.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<UserComment, Long> {
    @Query("SELECT c FROM comments c WHERE c.event.id = :eventId")
    List<UserComment> findEventCommentsById(@Param("eventId") Long eventId);

    // Find comments by a specific group ID
    @Query("SELECT c FROM comments c WHERE c.group.id = :groupId")
    List<UserComment> findGroupCommentsById(@Param("groupId") Long groupId);
}