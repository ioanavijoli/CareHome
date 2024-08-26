package com.servustech.carehome.file;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.servustech.carehome.disk.FileSystem;
import com.servustech.carehome.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servustech.carehome.persistence.model.FileResource;

/**
 * @author Andrei Groza
 *
 */
@Service
public class FileResourceService {
	/** Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileResourceService.class);

	private final FileResourceDAO fileResourceDAO;
	private final FileResourceConverter fileResourceConverter;
	private final FileSystem fileSystem;

	@Autowired
	public FileResourceService(final FileResourceDAO fileResourceDAO, final FileResourceConverter fileResourceConverter, final FileSystem fileSystem) {
		this.fileResourceDAO = fileResourceDAO;
		this.fileResourceConverter = fileResourceConverter;
		this.fileSystem = fileSystem;
	}

	/**
	 * Save the file resource
	 *
	 * @param fileResourceDTO
	 *            file resource model
	 * @return the saved file resource
	 */
	public FileResourceDTO saveFileResource(final FileResourceDTO fileResourceDTO) {
		final FileResource fileResource = new FileResource();
		fileResource.prepareForCreate();
		this.fileResourceConverter.toEntity(fileResourceDTO, fileResource);
		fileResource.setUploadTime(LocalDateTime.now());

		String ext = FileUtils.getExtension(fileResourceDTO.getOriginalName());
		// generate path
		String path = fileSystem.generatePath(fileResource.getUUID() + (ext != "" ? "." + ext : ""));
		fileResource.setPath(path);

		fileSystem.saveFile(path, fileResourceDTO.getContent());

		this.fileResourceDAO.save(fileResource);

		FileResourceDTO dto = fileResourceConverter.toDTO(fileResource);
		dto.setContent(fileResourceDTO.getContent());

		LOGGER.trace("Saving file resource: {}", fileResource);
		return dto;

	}

	/**
	 * Retrieve the file resource
	 *
	 * @param fileResourceUUID
	 *            the UUID of the file resource
	 * @return the found file resource
	 */
	public FileResourceDTO getFileResourceByUUID(final String fileResourceUUID) {

		FileResourceDTO fileResource = this.fileResourceConverter.toDTO(this.fileResourceDAO.findByUUID(fileResourceUUID));

		fileResource.setContent(fileSystem.readFile(fileResource.getPath()));

		return fileResource;
	}

	/**
	 * Retrieve the file resources uploaded by a specific user
	 *
	 * @param userUUID
	 *            the UUID of the uploader
	 * @param name
	 *            the name of the file
	 * @param keyword
	 *            keyword to find by
	 * @return the found file resources collection
	 */
	public Collection<FileResourceDTO> getFileResources(final String userUUID, final Optional<String> name,
			final Optional<String> keyword, final Optional<LocalDateTime> startDate,
			final Optional<LocalDateTime> endDate) {

		Collection<FileResource> files = this.fileResourceDAO.find(userUUID, name, keyword, startDate, endDate);
		return toDTOs(files);
	}

	public Collection<FileResourceDTO> getFilesByUUIDs(List<String> uuids) {

		Collection<FileResource> files = this.fileResourceDAO.retrieveByUUIDs(uuids);
		return toDTOs(files);
	}


	// fill the dtos with the content from disk
	private Collection<FileResourceDTO> toDTOs(Collection<FileResource> files) {
		Collection<FileResourceDTO> resourceDTOs = new ArrayList<>();
		for (FileResource file : files) {
			FileResourceDTO dto = fileResourceConverter.toDTO(file);
			dto.setContent(fileSystem.readFile(file.getPath()));
			resourceDTOs.add(dto);
		}

		return resourceDTOs;
	}
	/**
	 * Update an existing file resource
	 *
	 * @param fileResourceDTO
	 *            the new file resource
	 * @return the updated file resource
	 */
	public FileResourceDTO updateFileResource(final FileResourceDTO fileResourceDTO) {
		final FileResource fileResource = this.fileResourceDAO.findByUUID(fileResourceDTO.getUUID());
		this.fileResourceConverter.toEntity(fileResourceDTO, fileResource);

		fileSystem.saveFile(fileResource.getPath(), fileResourceDTO.getContent());

		FileResourceDTO dto = this.fileResourceConverter.toDTO(this.fileResourceDAO.update(fileResource));
		dto.setContent(fileResourceDTO.getContent());

		return dto;
	}

	/**
	 *
	 * @param fileResourceUUID
	 *            the file resource to be deleted
	 */
	public void deleteFileResource(final String fileResourceUUID) {

		FileResource file = fileResourceDAO.findByUUID(fileResourceUUID);

		fileSystem.deleteFile(file.getPath());

		this.fileResourceDAO.delete(fileResourceUUID);
	}

	/**
	 * Delete multiple files at once
	 * 
	 * @param uuids
	 */
	public void deleteByUUIDs(final List<String> uuids) {
		Collection<FileResource> pics = fileResourceDAO.retrieveByUUIDs(uuids);

		this.fileResourceDAO.deleteByUUIDs(uuids);

		for (FileResource pic : pics) {
			fileSystem.deleteFile(pic.getPath());
		}
	}

}
