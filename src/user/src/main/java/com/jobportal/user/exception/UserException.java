package com.jobportal.user.exception;

public class UserException {

    public static class EmailAlreadyExist extends RuntimeException{
        public EmailAlreadyExist(String email){
            super("Email address is already registered: " + email);
        }
    }

    public static class PhoneNumberAlreadyExist extends RuntimeException{
        public PhoneNumberAlreadyExist(String phoneNumber){
            super("Phone number is already registered: " + phoneNumber);
        }
    }

    public static class UserCreationFailed extends RuntimeException{
        public UserCreationFailed(Throwable ex){
            super("User creation failed", ex);
        }
    }

    public static class UserDoesNotExist extends RuntimeException{
        public UserDoesNotExist(){
            super("User does not exist");
        }
    }

    public static class ImagesNotSent extends RuntimeException{
        public ImagesNotSent(){
            super("Images are missing");
        }
    }

    public static class InvalidImageFile extends RuntimeException{
        public InvalidImageFile(){
            super("Invalid image sent");
        }
    }

    public static class InvalidUserPassword extends RuntimeException{
        public InvalidUserPassword(){
            super("Invalid user password");
        }
    }
}
