package com.servustech.carehome.user;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.servustech.carehome.persistence.model.User;
import com.servustech.carehome.persistence.model.User_;
import com.servustech.mongo.utils.dao.impl.EntityDAOImpl;
import com.servustech.mongo.utils.exception.EntityNotFoundException;

/**
 * User DAO
 *
 * @author Andrei Groza
 *
 */
@Repository
public class UserDAO extends EntityDAOImpl<ObjectId, User> {

	public UserDAO() {
		super(User.class);
	}

	/**
	 * Retrieve user by username
	 *
	 * @param username
	 *            the user username
	 * @return
	 */
	public Optional<User> findByUsername(final String username) {
		final Query query = new Query(Criteria.where(User_.FIELD_USERNAME).is(username));
		final User user = this.mongoOperations.findOne(query, User.class);
		return Optional.ofNullable(user);
	}

	/**
	 * Retrieve user by email
	 *
	 * @param email
	 *            the user email
	 * @return
	 */
	public Optional<User> findByEmail(final String email) {
		final Query query = new Query(Criteria.where(User_.FIELD_EMAIL).is(email));
		final User user = this.mongoOperations.findOne(query, User.class);
		return Optional.ofNullable(user);
	}

	/**
	 * Retrieve user by username or email
	 *
	 * @param usernameOrEmail
	 *            username or email
	 * @return
	 */
	public Optional<User> findByUsernameOrEmail(final String usernameOrEmail) {
		final Criteria criteria = new Criteria();
		final Criteria usernameCriteria = Criteria.where(User_.FIELD_USERNAME).is(usernameOrEmail);
		final Criteria emailCriteria = Criteria.where(User_.FIELD_EMAIL).in(usernameOrEmail);
		criteria.orOperator(usernameCriteria, emailCriteria);

		final Query userQuery = new Query(criteria);
		final User user = this.mongoOperations.findOne(userQuery, User.class);

		return Optional.ofNullable(user);
	}

	/**
	 * Retrieve user by email and password
	 *
	 * @param email
	 *            the user email
	 * @param password
	 *            the hashed password
	 * @return
	 */
	public User findByEmailAndPassword(final String email, final String password) {
		final Query query = new Query(Criteria.where(User_.FIELD_EMAIL).is(email)
				.andOperator(Criteria.where(User_.FIELD_PASSWORD).is(password)));
		final User user = this.mongoOperations.findOne(query, User.class);
		if (user == null) {
			throw new EntityNotFoundException(String.format("User with email: %s and pass: %s does not exist", email,
					"***"));
		}
		return user;
	}

	public boolean existsByUUIDAndRole(String uuid, String role) {
		return this.mongoOperations.exists(new Query(Criteria.where("UUID").is(uuid).
				andOperator(Criteria.where("role").is(role))), User.class);
	}

}