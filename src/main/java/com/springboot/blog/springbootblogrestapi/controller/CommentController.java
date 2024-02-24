package com.springboot.blog.springbootblogrestapi.controller;

import com.springboot.blog.springbootblogrestapi.payload.CommentDto;
import com.springboot.blog.springbootblogrestapi.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable(name = "postId",required = true)long postId){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable(name = "postId")long postId){
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentByCommentId(@PathVariable(name="postId") long postId,
        @PathVariable(name="id") long commentId){
        return ResponseEntity.ok(commentService.getCommentByCommentId(postId,commentId));
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateCommentByCommentId(@Valid @RequestBody CommentDto commentDto, @PathVariable(name="postId") long postId,
                                                               @PathVariable(name="id") long commentId){
        return new ResponseEntity<>(commentService.updateCommentByCommentId(postId,commentId,commentDto),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String>  deleteCommentByCommentId(@PathVariable(name="postId") long postId,
                                                                @PathVariable(name="id") long commentId){
        return new ResponseEntity<>(commentService.deleteCommentByCommentId(postId,commentId),HttpStatus.OK);
    }

}
