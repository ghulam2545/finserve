package com.ghulam.customer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddressDto(

        @NotBlank(message = "Address line 1 is required")
        @Size(max = 255, message = "Address line 1 cannot exceed 255 characters")
        String line1,

        @Size(max = 255, message = "Address line 2 cannot exceed 255 characters")
        String line2,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City cannot exceed 100 characters")
        String city,

        @NotBlank(message = "pincode is required")
        @Size(max = 20, message = "pincode cannot exceed 20 characters")
        String pincode,

        @NotBlank(message = "Country code is required")
        @Size(min = 2, max = 2, message = "Country code must be a 2-letter ISO code")
        String countryCode
) {
}