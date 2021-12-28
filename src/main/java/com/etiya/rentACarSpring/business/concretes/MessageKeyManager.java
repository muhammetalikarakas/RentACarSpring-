package com.etiya.rentACarSpring.business.concretes;

import com.etiya.rentACarSpring.business.abstracts.MessageKeyService;
import com.etiya.rentACarSpring.business.dtos.LanguageSearchListDto;
import com.etiya.rentACarSpring.business.dtos.MessageKeySearchListDto;
import com.etiya.rentACarSpring.business.requests.create.CreateMessageKeyRequest;
import com.etiya.rentACarSpring.business.requests.delete.DeleteMessageKeyRequest;
import com.etiya.rentACarSpring.business.requests.update.UpdateMessageKeyRequest;
import com.etiya.rentACarSpring.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACarSpring.core.utilities.results.DataResult;
import com.etiya.rentACarSpring.core.utilities.results.Result;
import com.etiya.rentACarSpring.core.utilities.results.SuccessDataResult;
import com.etiya.rentACarSpring.core.utilities.results.SuccessResult;
import com.etiya.rentACarSpring.dataAccess.abstracts.MessageKeyDao;
import com.etiya.rentACarSpring.entities.Language;
import com.etiya.rentACarSpring.entities.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageKeyManager implements MessageKeyService {


    ModelMapperService modelMapperService;
    MessageKeyDao messageKeyDao;

    @Autowired
    public MessageKeyManager(ModelMapperService modelMapperService, MessageKeyDao messageKeyDao) {
        this.modelMapperService = modelMapperService;
        this.messageKeyDao=messageKeyDao;
    }




    @Override
    public Result add(CreateMessageKeyRequest createMessageKeyRequest) {
        MessageKey messageKey= this.modelMapperService.forRequest().map(createMessageKeyRequest,MessageKey.class);
        this.messageKeyDao.save(messageKey);
        return new SuccessResult();
    }

    @Override
    public Result update(UpdateMessageKeyRequest updateMessageKeyRequest) {
        MessageKey messageKey= this.modelMapperService.forRequest().map(updateMessageKeyRequest,MessageKey.class);
        this.messageKeyDao.save(messageKey);
        return new SuccessResult();
    }

    @Override
    public Result delete(DeleteMessageKeyRequest deleteMessageKeyRequest) {
        this.messageKeyDao.deleteById(deleteMessageKeyRequest.getMessageKeyId());
        return new SuccessResult();
    }

    @Override
    public DataResult<List<MessageKeySearchListDto>> getAll() {
        List<MessageKey> result=this.messageKeyDao.findAll();
        List<MessageKeySearchListDto> response=result.stream()
                .map(messageKey->this.modelMapperService.forDto().map(messageKey,MessageKeySearchListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<MessageKeySearchListDto>>(response);
    }
}
