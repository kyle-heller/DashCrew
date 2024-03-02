package com.dashcrew.web.repository;

import com.dashcrew.web.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
