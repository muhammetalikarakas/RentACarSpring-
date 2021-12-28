package com.etiya.rentACarSpring.business.requests.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageRequest {


    @NotNull
    @Range(min = 1,message = "lütfen geçerli bir id girin")
    private int id;

    //@NotNull
    private String messageContent ;

    //@NotNull
    //@Range(min = 1,message = "lütfen geçerli bir id girin")
    private int languageId;

    //@NotNull
    //@Range(min = 1,message = "lütfen geçerli bir id girin")
    private int messageKeyId;
}
