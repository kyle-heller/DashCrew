package com.rollcall.web.dto;

import com.rollcall.web.models.Game;
import com.rollcall.web.models.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EventDto {

    private Long id;
    private String name;
    private int zip;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    private String type;
    private String photoURL;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Game game;
    private Group group;

}
