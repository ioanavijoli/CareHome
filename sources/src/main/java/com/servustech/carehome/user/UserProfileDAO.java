package com.servustech.carehome.user;

import com.servustech.carehome.persistence.model.UserProfile;
import com.servustech.carehome.persistence.model.UserProfile_;
import com.servustech.mongo.utils.dao.impl.EntityDAOImpl;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserProfileDAO extends EntityDAOImpl<ObjectId, UserProfile> {

	public UserProfileDAO() {
		super(UserProfile.class);
	}

	/**
	 * Retrieve user profile by user UUID
	 * 
	 * @param userUUID
	 * @return
	 */
	public Optional<UserProfile> findByUserUUID(String userUUID) {
		Query q = new Query(Criteria.where(UserProfile_.FIELD_USER_UUID).is(userUUID));
		UserProfile profile = this.mongoOperations.findOne(q, UserProfile.class);
		return Optional.ofNullable(profile);
	}

	/**
	 * Remove from DB the user profile
	 * 
	 * @param userUUID
	 */
	public void deleteByUserUUID(String userUUID) {
		Query q = new Query(Criteria.where(UserProfile_.FIELD_USER_UUID).is(userUUID));
		this.mongoOperations.remove(q, UserProfile.class);

	}

	public Optional<UserProfile> getAvatar(String userUUID) {
		Query q = new Query(Criteria.where(UserProfile_.FIELD_USER_UUID).is(userUUID));
		q.fields().include(UserProfile_.FIELD_SMALL_AVATAR);
//		q.fields().include("avatar");
		q.fields().include(UserProfile_.FIELD_AVATAR_CONTENT_TYPE);
		UserProfile profile = this.mongoOperations.findOne(q, UserProfile.class);

		return Optional.ofNullable(profile);
	}

	/**
	 * Get profiles for several users
	 * @param userUUIDs
	 * @return
     */
	public Collection<UserProfile> getSummaryByUserUUIDs(Collection<String> userUUIDs) {
		final Query query = new Query(Criteria.where(UserProfile_.FIELD_USER_UUID).in(userUUIDs));
		query.fields().include(UserProfile_.FIELD_SMALL_AVATAR);
		query.fields().include(UserProfile_.FIELD_FIRSTNAME);
		query.fields().include(UserProfile_.FIELD_SURNAME);
		query.fields().include(UserProfile_.FIELD_USER_UUID);

		return this.mongoOperations.find(query, UserProfile.class);
	}

}