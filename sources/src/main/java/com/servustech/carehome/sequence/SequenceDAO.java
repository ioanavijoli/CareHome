package com.servustech.carehome.sequence;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.servustech.carehome.persistence.model.Sequence;
import com.servustech.carehome.persistence.model.Sequence_;

@Repository
public class SequenceDAO {
	private static final ObjectId ENTITY_ID = new ObjectId("573da1c4d21f5214ccd6cccf");

	private final MongoOperations mongoOperations;

	@Autowired
	public SequenceDAO(final MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public long getNextSequence(final String key) {
		final Query query = new Query(Criteria.where(Sequence_.FIELD_ID).is(ENTITY_ID));

		if (!this.mongoOperations.exists(query, Sequence.class)) {
			createEntity();
		}

		// increase sequence id by 1
		final Update update = new Update();
		update.inc(key, 1);

		// return new increased id
		final FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		// this is the magic happened.
		final Sequence sequence = this.mongoOperations.findAndModify(query, update, options, Sequence.class);

		// if no id, throws Exception
		if (sequence == null) {
			throw new RuntimeException(String.format("Unable to get sequence for key: %s", key));
		}

		return sequence.getIdSequence();
	}

	private void createEntity() {
		final Sequence sequence = new Sequence();
		sequence.setId(ENTITY_ID);
		this.mongoOperations.save(sequence);
	}

}