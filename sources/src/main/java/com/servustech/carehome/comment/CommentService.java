package com.servustech.carehome.comment;

import com.servustech.carehome.business.BusinessDAO;
import com.servustech.carehome.persistence.model.Business;
import com.servustech.carehome.persistence.model.Comment;
import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.persistence.model.UserProfile;
import com.servustech.carehome.user.UserSummaryDTO;
import com.servustech.carehome.user.UserDAO;
import com.servustech.carehome.user.UserProfileDAO;
import com.servustech.mongo.utils.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private final CommentValidator commentValidator;

    private final CommentConverter commentConverter;

    private final CommentDAO commentDAO;

    private final UserDAO userDAO;

    private final UserProfileDAO profileDAO;

    private final BusinessDAO businessDAO;

    @Autowired
    public CommentService (final CommentValidator commentValidator, final CommentConverter commentConverter, final CommentDAO commentDAO, final UserDAO userDAO, final UserProfileDAO profileDAO, final BusinessDAO businessDAO){
        this.commentValidator = commentValidator;
        this.commentConverter = commentConverter;
        this.commentDAO = commentDAO;
        this.userDAO = userDAO;
        this.profileDAO = profileDAO;
        this.businessDAO = businessDAO;
    }

    /**
     * Add a comment for a certain carehome
     * @param dto
     * @return
     */
    public CommentDTO createComment(CommentDTO dto) {
        commentValidator.validate(dto);

        Comment comment = new Comment();
        comment.prepareForCreate();
        commentConverter.toEntity(dto, comment);

        LOGGER.trace("Create comment");

        return commentConverter.toDTO(commentDAO.save(comment));
    }

    /**
     * Update a comment
     * @param commentUUID
     * @param dto
     * @return
     */
    public CommentDTO updateComment(String commentUUID, CommentDTO dto) {
        commentValidator.validate(dto);

        Comment comment = commentDAO.findByUUID(commentUUID);
        if (!comment.getUserUUID().equalsIgnoreCase(dto.getUserUUID())) {
            throw new EntityNotFoundException("Comment does not belong to this user");
        }

        comment.prepareForUpdate();
        commentConverter.toEntity(dto, comment);

        LOGGER.trace(String.format("Updating comment with UUID : %s", commentUUID));
        return commentConverter.toDTO(commentDAO.update(comment));
    }

    /**
     * Retrieve all comments for a given carehome UUID
     * @param carehomeUUID
     * @return
     */
    public CommentsDTO getComments(String carehomeUUID) {

        Collection<Comment> comments = commentDAO.findAllByCareHomeUUID(carehomeUUID);
        Set<String> usersUUIDs = new HashSet<>();

        comments.forEach(comment -> usersUUIDs.add(comment.getUserUUID()));

        Collection<User> users = userDAO.findByUUIDs(usersUUIDs);
        Collection<UserProfile> profiles = profileDAO.getSummaryByUserUUIDs(usersUUIDs);

        // put profiles in a map
        HashMap<String, UserProfile> userProfileHashMap = new HashMap<>();
        profiles.forEach(profile -> userProfileHashMap.put(profile.getUserUUID(), profile));

        Collection<CommentDTO> commentDTOs = commentConverter.toDTOList(comments);
        Collection<UserSummaryDTO> smallUsers = new ArrayList<>();

        // put business in a map
        Collection<Business> businesses = businessDAO.getSummaryByUserUUIDs(usersUUIDs);
        HashMap<String, Business> businessHashMap = new HashMap<>();
        businesses.forEach(business -> businessHashMap.put(business.getUserUUID(), business));

        for (User user : users) {
            UserSummaryDTO summaryDTO = new UserSummaryDTO(user.getUUID());

            if (user.getRole().equals(User.Role.USER)) {
                // if the role is user
                UserProfile profile = userProfileHashMap.get(user.getUUID());
                if (profile != null) {
                    summaryDTO.setName(profile.getFirstname() + " " + profile.getSurname());
                    summaryDTO.setAvatar(profile.getSmallAvatar());
                }
            } else if (user.getRole().equals(User.Role.BUSINESS)) {
                // if the role is business
                Business business = businessHashMap.get(user.getUUID());
                if (business != null) {
                    summaryDTO.setName(business.getName());
                    summaryDTO.setAvatar(business.getLogo());
                }
            }
            smallUsers.add(summaryDTO);
        }

        return new CommentsDTO(smallUsers, commentDTOs);
    }
}
