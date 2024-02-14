package com.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.repository.CommentJpaRepository;
import com.blog.vo.Comment;

@Service
public class CommentService {
	@Autowired
	CommentJpaRepository commentJpaRepository;
	public boolean saveComment(Comment comment) {
		Comment result = commentJpaRepository.save(comment);
		boolean isSuccess = true;
		if(result == null) {
			isSuccess = false;
		}
		return isSuccess;
	}
	
	public List<Comment> getCommentList(Long postId) {
        return commentJpaRepository.findByPostIdOrderByRegDateDesc(postId);
    }
	
	public Comment getComment(Long id) {
        Optional<Comment> commentOptional = Optional.ofNullable(commentJpaRepository.findById(id));
        return commentOptional.orElse(null);
    }
	
	public boolean deleteComment(Long id) {
        if (commentJpaRepository.existsById(id)) {
            commentJpaRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
	public List<Comment> searchComments(Long postId, String query) {
		List<Comment> comments = commentJpaRepository.findByPostIdAndCommentContainingOrderByRegDateDesc(postId, query);
		return comments;
	}

}