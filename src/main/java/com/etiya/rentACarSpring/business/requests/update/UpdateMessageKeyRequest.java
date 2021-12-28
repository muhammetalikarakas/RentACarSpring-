package com.etiya.rentACarSpring.business.requests.update;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageKeyRequest {

    private int messageKeyId;


    private String messageKey;
}

