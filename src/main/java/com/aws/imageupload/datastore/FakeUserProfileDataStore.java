/**
 * 
 */
package com.aws.imageupload.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aws.imageupload.profile.UserProfile;

/**
 * @author evaristosrodrigues
 *
 */

@Repository
public class FakeUserProfileDataStore {

	private static final List<UserProfile> USER_PROFILE = new ArrayList<>();
	
	static {
		USER_PROFILE.add(new UserProfile(UUID.fromString("85e37cff-c21a-415e-810c-cb6d0fa3a8af"),"janetijones",null));
		USER_PROFILE.add(new UserProfile(UUID.fromString("d4a1e133-8349-4621-b832-7c7c1c451981"),"antoniojunior",null));
	}
	
	public List<UserProfile> getUserProfiles(){
		return 	USER_PROFILE;
	}

	public UserProfile findUserProfileByUUID(UUID userProfile) {
		return USER_PROFILE.stream().filter(user -> userProfile.equals(user.getUserProfileId())).findFirst().orElse(null);
		
	}
}
