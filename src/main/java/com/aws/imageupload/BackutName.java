package com.aws.imageupload;

public enum BackutName {
	
	PROFILE_NAME("spring-boot-aws-image-upload");
	
	private final String profileName;

	
	private BackutName(String profileName) {
		this.profileName = profileName;
	}


	public String getProfileName() {
		return profileName;
	}
	
	

}
