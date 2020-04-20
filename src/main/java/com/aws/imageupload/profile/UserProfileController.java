/**
 * 
 */
package com.aws.imageupload.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author evaristosrodrigues
 *
 */

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {
	
	
    private final UserProfileService userProfileService;		
	
    @Autowired
	public UserProfileController(UserProfileService userProfileService) {
		super();
		this.userProfileService = userProfileService;
	}

	@GetMapping
	public List<UserProfile> getUserProfiles(){
		return userProfileService.getUserProfile();
	}
	
	@PostMapping(
			     path = "{userProfile}/image/upload",
			     consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			     produces = MediaType.APPLICATION_JSON_VALUE)
	public void uploadUserProfileImage(@PathVariable("userProfile") UUID userProfile,
	                                   @RequestParam("file") MultipartFile file) {
			userProfileService.uploadUserProfileImage(userProfile, file);		
	}
	
	@GetMapping(
			path = "{userProfile}/image/download"
			)
	public byte[] downloadUserProfileImage(@PathVariable("userProfile") UUID userProfile) {
		return userProfileService.downloadUserProfileImage(userProfile);
	}
	
	

}
