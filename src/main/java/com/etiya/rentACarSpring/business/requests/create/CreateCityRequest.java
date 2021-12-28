package com.etiya.rentACarSpring.business.requests.create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCityRequest {
	@JsonIgnore
	private int id;

	@NotNull
	@Size(min=3,max=20)
	private String name;
	
}
