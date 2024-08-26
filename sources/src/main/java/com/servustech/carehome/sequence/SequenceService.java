/**
 *
 */
package com.servustech.carehome.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servustech.carehome.persistence.model.Sequence_;

/**
 * @author Andrei Groza
 *
 */
@Service
public class SequenceService {
	private static final String SEQUENCE_PREFIX = "seq_prefix_";

	/**
	 * Reference to {@link SequenceDAO}
	 */
	private final SequenceDAO sequenceDAO;

	@Autowired
	public SequenceService(final SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	public String nextSequenceString() {
		return SEQUENCE_PREFIX + this.sequenceDAO.getNextSequence(Sequence_.FIELD_ID_SEQUENCE);
	}
}
