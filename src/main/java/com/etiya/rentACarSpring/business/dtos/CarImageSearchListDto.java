package com.etiya.rentACarSpring.business.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarImageSearchListDto {

	private int id;
	
	private int carId;

	private byte[] image;
	
	private LocalDateTime date;

}
