package com.etiya.rentACarSpring.business.concretes;

import com.etiya.rentACarSpring.business.abstracts.LanguageService;
import com.etiya.rentACarSpring.business.dtos.LanguageSearchListDto;
import com.etiya.rentACarSpring.business.requests.create.CreateLanguageRequest;
import com.etiya.rentACarSpring.business.requests.delete.DeleteLanguageRequest;
import com.etiya.rentACarSpring.business.requests.update.UpdateLanguageRequest;
import com.etiya.rentACarSpring.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACarSpring.core.utilities.results.DataResult;
import com.etiya.rentACarSpring.core.utilities.results.Result;
import com.etiya.rentACarSpring.core.utilities.results.SuccessDataResult;
import com.etiya.rentACarSpring.core.utilities.results.SuccessResult;
import com.etiya.rentACarSpring.dataAccess.abstracts.LanguageDao;
import com.etiya.rentACarSpring.entities.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageManager implements LanguageService {

    private LanguageDao languageDao;
    private ModelMapperService modelMapperService;

    public LanguageManager(LanguageDao languageDao,ModelMapperService modelMapperService){
        this.languageDao=languageDao;
        this.modelMapperService=modelMapperService;
    }

    @Override
    public Result add(CreateLanguageRequest createLanguageRequest){
        Language language=this.modelMapperService.forRequest().map(createLanguageRequest,Language.class);
        this.languageDao.save(language);
        return new SuccessResult();


    }

    @Override
    public Result update(UpdateLanguageRequest updateLanguageRequest){

        Language language=this.modelMapperService.forRequest().map(updateLanguageRequest,Language.class);
        this.languageDao.save(language);
        return new SuccessResult();
    }

    @Override
    public Result delete(DeleteLanguageRequest deleteLanguageRequest){

        this.languageDao.deleteById(deleteLanguageRequest.getLanguageId());
        return new SuccessResult();
    }

    @Override
    public DataResult<List<LanguageSearchListDto>> getAll(){
        List<Language> result=this.languageDao.findAll();
        List<LanguageSearchListDto> response=result.stream()
                .map(language->this.modelMapperService.forDto().map(language,LanguageSearchListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<LanguageSearchListDto>>(response);

    }
}
