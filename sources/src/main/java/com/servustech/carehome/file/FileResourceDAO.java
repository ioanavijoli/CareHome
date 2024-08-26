package com.servustech.carehome.file;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.servustech.carehome.persistence.model.FileResource;
import com.servustech.mongo.utils.dao.impl.EntityDAOImpl;
import com.servustech.mongo.utils.model.BaseEntity_;
import com.servustech.mongo.utils.model.FileResource_;

/**
 * FileResource DAO
 *
 * @author Andrei Groza
 *
 */
@Repository
public class FileResourceDAO extends EntityDAOImpl<ObjectId, FileResource> {

	public FileResourceDAO() {
		super(FileResource.class);
	}

	/**
	 * Delete multiple files at once
	 * 
	 * @param uuids
	 */
	public void deleteByUUIDs(final List<String> uuids) {
		this.mongoOperations.remove(new Query().addCriteria(Criteria.where(BaseEntity_.FIELD_UUID)
				.in(uuids)), FileResource.class);
	}

	public Collection<FileResource> retrieveByUUIDs(final Collection<String> UUIDs) {

		return this.mongoOperations.find(new Query().addCriteria(Criteria.where(BaseEntity_.FIELD_UUID).in(UUIDs)), FileResource.class);
	}

	/**
	 *
	 * @param userUUID
	 * @return
	 */
	public Collection<FileResource> find(final String userUUID, final Optional<String> name,
			final Optional<String> keyword, final Optional<LocalDateTime> startDate,
			final Optional<LocalDateTime> endDate) {

		final Criteria criteria = Criteria.where(FileResource_.FIELD_USER_UUID).is(userUUID);
		Criteria nameCriteria = new Criteria();
		final Criteria keywordCriteria = new Criteria();
		Criteria dateCriteria = new Criteria();

		if (name.isPresent()) {
			nameCriteria = Criteria.where(FileResource_.FIELD_NAME).regex(name.get());
		}
		if (keyword.isPresent()) {
			keywordCriteria.orOperator(
					Criteria.where(FileResource_.FIELD_DESCRIPTION).regex(keyword.get()), Criteria
							.where(FileResource_.FIELD_NAME).regex(keyword.get()));

		}
		if (startDate.isPresent()) {
			dateCriteria = Criteria.where(FileResource_.FIELD_UPLOAD_DATE).gte(startDate.get());
		}
		if (endDate.isPresent()) {
			dateCriteria = Criteria.where(FileResource_.FIELD_UPLOAD_DATE).lte(
					endDate.get().plusDays(1).minusMinutes(1));
		}
		if ((startDate.isPresent()) && (endDate.isPresent())) {
			dateCriteria = Criteria.where(FileResource_.FIELD_UPLOAD_DATE).gte(startDate.get())
					.lte(endDate.get().plusDays(1).minusMinutes(1));
		}

		criteria.andOperator(nameCriteria, keywordCriteria, dateCriteria);

		final List<FileResource> fileResources = this.mongoOperations.find(new Query().addCriteria(criteria), FileResource.class);

		return fileResources;
	}

	/**
	 * Update file resource meta data
	 *
	 * @param fileResource
	 * @return the updated file resource
	 */
	@Override
	public FileResource update(final FileResource fileResource) {
		final FileResource oldFileResource = findByUUID(fileResource.getUUID());
		oldFileResource.setName(fileResource.getName());
		oldFileResource.setDescription(fileResource.getDescription());
		oldFileResource.setContentType(fileResource.getContentType());
		oldFileResource.setUserUUID(fileResource.getUserUUID());

		this.delete(fileResource.getUUID());
		return save(oldFileResource);

	}

}
