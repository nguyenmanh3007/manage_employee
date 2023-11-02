package com.repository;

import com.dto.CommentDTO;
import com.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findCommentByCommentId(int idComment);
    @Query(value = "SELECT co from Comment co INNER JOIN Project pj ON co.project.projectId=pj.projectId" +
            " INNER JOIN Employee em ON em.employeeId=co.employee.employeeId" +
            " WHERE pj.code=?1")
    List<Comment> filterCommentWithProject(String codeProject);
    @Query(value = "SELECT co from Comment co INNER JOIN Project pj ON co.project.projectId=pj.projectId" +
            " INNER JOIN Employee em ON em.employeeId=co.employee.employeeId" +
            " WHERE em.code=?1")
    List<Comment> filterCommentWithEmployee(int codeEmployee);

    @Query(value = "SELECT co from Comment co INNER JOIN Project pj ON co.project.projectId=pj.projectId" +
            " INNER JOIN Employee em ON em.employeeId=co.employee.employeeId" +
            " WHERE (co.createTime BETWEEN ?1 AND ?2) OR (co.updateTime BETWEEN ?1 AND ?2) ")
    List<Comment> filterCommentWithTime(String start,String end);
}
