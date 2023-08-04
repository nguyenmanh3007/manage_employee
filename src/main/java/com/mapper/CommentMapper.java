package com.mapper;



import com.dto.CommentDTO;
import com.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper MAPPER= Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "content",target = "content")
    @Mapping(source = "comment.employee.userName",target = "nameEmployee")
    @Mapping(source = "comment.project.nameProject",target = "nameProject")
    CommentDTO commentToCommentDTO(Comment comment);

    @Mapping(source = "content",target = "content")
    Comment commentDTOToComment(CommentDTO commentDTO);
}
