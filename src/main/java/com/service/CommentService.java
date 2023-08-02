package com.service;

import com.dto.CommentDTO;
import com.entity.Comment;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);

    CommentDTO updateComment(CommentDTO commentDTO);

    void deleteComment(int commentId);
    Comment findCommentByCommentId(int idComment);
    List<CommentDTO> filterCommentWithProject(String codeProject);
    List<CommentDTO> filterCommentWithEmployee(int codeEmployee);
    List<CommentDTO> filterCommentWithTime(String start,String end);
}
