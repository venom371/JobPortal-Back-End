package com.jobportal.common;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse(
    boolean success, 
    @JsonInclude(JsonInclude.Include.NON_NULL) String message, 
    @JsonInclude(JsonInclude.Include.NON_NULL) Object data
    ) {
        public ApiResponse(boolean success, String message) {
            this(success, message, null);
        }

        public ApiResponse(boolean success) {
            this(success, null, null);
        }
} 
 