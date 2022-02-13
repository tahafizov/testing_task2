package com.khafizov.restfull.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatDto {
    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotNull
    @Min(value = 1, message = "Cats tail length must be greater than 0!")
    @Max(value = 80, message = "Cats tail length must be less than 80!")
    private Integer tailLength;

    @NotNull
    @Min(value = 1, message = "Cats whiskers length must be greater than 0!")
    @Max(value = 20, message = "Cats whiskers length must be less than 20!")
    private Integer whiskersLength;
}
