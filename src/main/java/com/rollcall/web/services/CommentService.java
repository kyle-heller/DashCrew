package com.rollcall.web.services;

import com.rollcall.web.models.Event;
import com.rollcall.web.models.Group;
import com.rollcall.web.models.UserCommentDto;

import java.util.List;

public interface CommentService {
    public void addComment(UserCommentDto userCommentDto);
    public void removeComment(UserCommentDto userCommentDto);
    public void editComment(UserCommentDto userCommentDto);
    public List<UserCommentDto> findEventCommentById(Long eventId);
    public List<UserCommentDto> findGroupCommentById(Long groupId);
}
