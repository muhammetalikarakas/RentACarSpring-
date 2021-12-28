package com.etiya.rentACarSpring.business.requests.delete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMessageRequest {

    @NotNull
    @Range(min = 1,message = "lütfen geçerli bir id girin")
    private int id;

}