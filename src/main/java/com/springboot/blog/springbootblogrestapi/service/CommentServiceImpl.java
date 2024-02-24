package com.springboot.blog.springbootblogrestapi.service;

import com.springboot.blog.springbootblogrestapi.entity.Comment;
import com.springboot.blog.springbootblogrestapi.entity.Post;
import com.springboot.blog.springbootblogrestapi.exceptions.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exceptions.ResourceNotFoundException;
import com.springboot.blog.springbootblogrestapi.payload.CommentDto;
import com.springboot.blog.springbootblogrestapi.repository.CommentRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper modelMapper){
        this.commentRepository=commentRepository;
        this.postRepository=postRepository;
        this.modelMapper=modelMapper;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment= mapToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return mapToDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtoList = commentList.stream().map(this::mapToDto).toList();
        return commentDtoList;
    }

    @Override
    public CommentDto getCommentByCommentId(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","id",commentId));
        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateCommentByCommentId(long postId, long commentId,CommentDto commentDto){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));
        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
            comment.setBody(commentDto.getBody());
            comment.setEmail(commentDto.getEmail());
            comment.setName(commentDto.getName());

           Comment updatedComment =  commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public String deleteCommentByCommentId(long postId, long commentId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));
        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.deleteById(commentId);
        return "Comment has been deleted successfully";
    }

    private CommentDto mapToDto(Comment comment){
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto){
        return modelMapper.map(commentDto, Comment.class);
    }
}
