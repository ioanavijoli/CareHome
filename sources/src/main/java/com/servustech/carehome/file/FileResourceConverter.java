/**
 *
 */
package com.servustech.carehome.file;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.servustech.carehome.persistence.model.FileResource;
import com.servustech.carehome.util.DateConverter;

/**
 * FileResource converter. Converts {@link FileResource} entity to {@link FileResourceDTO} model
 *
 * @author Andrei Groza
 *
 */
@Component
public class FileResourceConverter {

	public FileResourceDTO toDTO(
			final FileResource fileResource) {
		final FileResourceDTO fileResourceDTO = new FileResourceDTO();
		fileResourceDTO.setUUID(fileResource.getUUID());
		fileResourceDTO.setUserUUID(fileResource.getUserUUID());
		fileResourceDTO.setName(fileResource.getName());
		fileResourceDTO.setDescription(fileResource.getDescription());
		fileResourceDTO.setContentType(fileResource.getContentType());
		fileResourceDTO.setUploadTime(DateConverter.toString(fileResource.getUploadTime()));
		fileResourceDTO.setPath(fileResource.getPath());

		return fileResourceDTO;
	}

	public void toEntity(
			final FileResourceDTO fileResourceDTO,
			final FileResource fileResource) {
		fileResource.setUserUUID(fileResourceDTO.getUserUUID());
		fileResource.setName(fileResourceDTO.getName());
		fileResource.setDescription(fileResourceDTO.getDescription());
		fileResource.setContentType(fileResourceDTO.getContentType());
	}

	public List<FileResourceDTO> toDTOList(
			final Collection<FileResource> fileResources) {
		return fileResources.stream().map(this::toDTO).collect(Collectors.toList());
	}
}
