package com.rollcall.web.models;

import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserCommentDto {


    private Long id;
    private String content;
    private Group group;
    private Event event;
    private UserEntity user;

}
