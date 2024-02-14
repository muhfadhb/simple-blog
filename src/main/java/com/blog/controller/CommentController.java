package com.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @PostMapping("/comment")
    public ResponseEntity<Result> savePost(@RequestBody Comment commentParam) {
        Comment comment = new Comment(commentParam.getPostId(), commentParam.getUser(), commentParam.getComment());
        boolean isSuccess = commentService.saveComment(comment);
        
        if(isSuccess) {
            return ResponseEntity.ok(new Result(200, "Success"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Result(500, "Fail"));
        }
    }
    
    @GetMapping("/comments")
    public List<Comment> getComments(@RequestParam("post_id") Long postId) {
        return commentService.getCommentList(postId);
    }
    
    @GetMapping("/comment")
    public Object getComment(@RequestParam("id") Long id) {
        Comment comment = commentService.getComment(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(404, "Comment not found"));
        }
    }
    
    @DeleteMapping("/comment")
    public ResponseEntity<Object> deleteComment(@RequestParam("id") Long id) {
        boolean isSuccess = commentService.deleteComment(id);
        if (isSuccess) {
            return ResponseEntity.ok().body(new Result(200, "Comment deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(404, "Comment not found"));
        }
    }
    
    @GetMapping("/comments/search")
    public List<Comment> searchComments(@RequestParam("post_id") Long postId, @RequestParam("query") String query) {
    	logger.info("Search Comments: Post ID = {}, Query = {}", postId, query);
    	List<Comment> comments = commentService.searchComments(postId, query);
    	return comments;
    }
}
