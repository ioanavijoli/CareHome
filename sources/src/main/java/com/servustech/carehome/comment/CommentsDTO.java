package com.servustech.carehome.comment;

import com.servustech.carehome.comment.CommentDTO;
import com.servustech.carehome.user.UserSummaryDTO;

import java.util.Collection;

public class CommentsDTO {

    private Collection<CommentDTO> commentDTOs;
    private Collection<UserSummaryDTO> users;

    public CommentsDTO(Collection<UserSummaryDTO> users, Collection<CommentDTO> commentDTOs) {
        this.users = users;
        this.commentDTOs = commentDTOs;
    }

    public Collection<CommentDTO> getCommentDTOs() {
        return commentDTOs;
    }

    public void setCommentDTOs(Collection<CommentDTO> commentDTOs) {
        this.commentDTOs = commentDTOs;
    }

    public Collection<UserSummaryDTO> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserSummaryDTO> users) {
        this.users = users;
    }
}
