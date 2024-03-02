package com.rollcall.web.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupDto {

    private Long id;
    private String title;
    private String photoURL;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
