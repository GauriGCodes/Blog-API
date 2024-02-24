package com.springboot.blog.springbootblogrestapi.service;

import com.springboot.blog.springbootblogrestapi.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId,CommentDto commentDto);
    List<CommentDto> getAllCommentsByPostId(long postId);
    CommentDto getCommentByCommentId(long postId, long commentId);
    CommentDto updateCommentByCommentId(long postId, long commentId,CommentDto commentDto);
    String deleteCommentByCommentId(long postId, long commentId);

}
