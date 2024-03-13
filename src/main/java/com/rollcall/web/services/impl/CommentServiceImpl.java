package com.rollcall.web.services.impl;

import com.rollcall.web.models.Event;
import com.rollcall.web.models.UserComment;
import com.rollcall.web.models.UserCommentDto;
import com.rollcall.web.repository.CommentRepository;
import com.rollcall.web.services.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.rollcall.web.mapper.EventMapper.mapToEventDto;
import static com.rollcall.web.mapper.UserCommentMapper.*;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(UserCommentDto userCommentDto) {
        UserComment userComment = mapToUserComment(userCommentDto);
        commentRepository.save(userComment);
    }


    @Override
    public void removeComment(UserCommentDto userCommentDto) {

    }

    @Override
    public void editComment(UserCommentDto userCommentDto) {

    }

    @Override
    public List<UserCommentDto> findEventCommentById(Long eventId) {
        List<UserComment> eventComments = commentRepository.findEventCommentsById(eventId);
        return eventComments.stream().map(eventComment -> mapToUserCommentDto(eventComment)).collect(Collectors.toList());
    }

    @Override
    public List<UserCommentDto> findGroupCommentById(Long groupId) {
        List<UserComment> groupComments = commentRepository.findGroupCommentsById(groupId);
        return groupComments.stream().map(eventComment -> mapToUserCommentDto(eventComment)).collect(Collectors.toList());
    }
}
