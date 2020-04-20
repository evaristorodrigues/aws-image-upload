/**
 * 
 */
package com.aws.imageupload.profile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aws.imageupload.BackutName;
import com.aws.imageupload.filestore.FileStore;

/**
 * @author evaristosrodrigues
 *
 */

@Service
public class UserProfileService {
	
	private final UserProfileDataAccessService userProfileDataAccessService;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
		this.userProfileDataAccessService = userProfileDataAccessService;
		this.fileStore = fileStore;
	}
	
	List<UserProfile> getUserProfile(){
		return userProfileDataAccessService.getUserProfiles();
	}

	public void uploadUserProfileImage(UUID userProfile, MultipartFile file)  {
		final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
		//1 check if image is not empty
		//2 check if file is image
		isImageValid(file, contentTypes);
		//3 The user exists in our database
		UserProfile user = getUserOrThrow(userProfile);
		//4 Grab some metadata from file if any
		Map<String, String> optionalMetaData = generateMetadataFromFile(file);
		//5 Store the image in S3 and update database (userProfileImageLink) s3 image link
		String path = String.format("%s/%s", BackutName.PROFILE_NAME.getProfileName(),user.getUserProfileId());

		try {
			fileStore.save(path, file.getOriginalFilename(), Optional.of(optionalMetaData), file.getInputStream());
			user.setUserProfileImageLink(file.getOriginalFilename());
		} catch (IOException e) {
           throw new IllegalStateException(e) ;           
		}
		
	}
	
	public byte[] downloadUserProfileImage(UUID userProfile) {
		UserProfile  user = getUserOrThrow(userProfile);
		String path = String.format("%s/%s", BackutName.PROFILE_NAME.getProfileName(),user.getUserProfileId());

		return user.getUserProfileImageLink().
				map(key -> fileStore.donwload(path, key)).
				orElse(new byte[0]);
			
	}	

	private Map<String, String> generateMetadataFromFile(MultipartFile file) {
		Map<String, String> optionalMetaData = new HashMap<>();
		optionalMetaData.put("Content-Type", file.getContentType());
		optionalMetaData.put("Content-Length", String.valueOf(file.getSize()));
		return optionalMetaData;
	}

	private UserProfile getUserOrThrow(UUID userProfile) {
		UserProfile user = userProfileDataAccessService.findUserProfileByUUID(userProfile);
		if(user == null) {
			throw new IllegalArgumentException("Usuário inválido");
		}
		return user;
	}

	private void isImageValid(MultipartFile file, final List<String> contentTypes) {
		if(file.isEmpty() || !contentTypes.contains(file.getContentType())) {
			throw new IllegalArgumentException("Arquivo inválido");
		}
	}
}
