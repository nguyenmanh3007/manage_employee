package com.service.impl;

import com.dto.CommentDTO;
import com.entity.Comment;
import com.entity.Employee;
import com.entity.Project;
import com.mapper.CommentMapper;
import com.repository.CommentRepository;
import com.repository.EmployeeRepository;
import com.repository.ProjectRepository;
import com.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Date now= new Date();
        SimpleDateFormat smf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        commentDTO.setCreateTime(smf.format(now));
        commentDTO.setStatus(true);
        return CommentMapper.MAPPER.commentToCommentDTO(commentRepository.save(CommentMapper.MAPPER.commentDTOToComment(commentDTO)));
    }

    @Override
    public CommentDTO updateComment(CommentDTO commentDTO) {
        Comment oldComment= commentRepository.findCommentByCommentId(commentDTO.getCommentId());
        commentDTO=commentDTO.builder()
                .commentId(commentDTO.getCommentId())
                .content(commentDTO.getContent())
                .createTime(oldComment.getCreateTime())
                .updateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()))
                .point(commentDTO.getPoint())
                .employee(oldComment.getEmployee())
                .status(oldComment.isStatus())
                .project(oldComment.getProject())
                .build();
        return CommentMapper.MAPPER.commentToCommentDTO(commentRepository.save(CommentMapper.MAPPER.commentDTOToComment(commentDTO)));
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment findCommentByCommentId(int idComment) {
        return commentRepository.findCommentByCommentId(idComment);
    }

    @Override
    public List<CommentDTO> filterCommentWithProject(String codeProject) {
        List<CommentDTO> result=commentRepository.filterCommentWithProject(codeProject).stream()
                .map(comment -> CommentMapper.MAPPER.commentToCommentDTO(comment))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CommentDTO> filterCommentWithEmployee(int codeEmployee) {
        List<CommentDTO> result=commentRepository.filterCommentWithEmployee(codeEmployee).stream()
                .map(comment -> CommentMapper.MAPPER.commentToCommentDTO(comment))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CommentDTO> filterCommentWithTime(String start, String end) {
        List<CommentDTO> result=commentRepository.filterCommentWithTime(start,end).stream()
                .map(comment -> CommentMapper.MAPPER.commentToCommentDTO(comment))
                .collect(Collectors.toList());
        return result;
    }
}
