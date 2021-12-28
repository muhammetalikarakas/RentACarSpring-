package com.etiya.rentACarSpring.business.concretes;

import java.util.List;
import java.util.stream.Collectors;
import com.etiya.rentACarSpring.business.abstracts.*;
import com.etiya.rentACarSpring.business.constants.Messages;
import com.etiya.rentACarSpring.business.requests.payment.PayCreditCardRequest;
import com.etiya.rentACarSpring.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.etiya.rentACarSpring.business.dtos.RentalSearchListDto;
import com.etiya.rentACarSpring.business.requests.create.CreateRentalRequest;
import com.etiya.rentACarSpring.business.requests.delete.DeleteRentalRequest;
import com.etiya.rentACarSpring.business.requests.update.UpdateRentalRequest;
import com.etiya.rentACarSpring.core.utilities.business.BusinessRules;
import com.etiya.rentACarSpring.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACarSpring.core.utilities.results.DataResult;
import com.etiya.rentACarSpring.core.utilities.results.ErrorResult;
import com.etiya.rentACarSpring.core.utilities.results.Result;
import com.etiya.rentACarSpring.core.utilities.results.SuccessDataResult;
import com.etiya.rentACarSpring.core.utilities.results.SuccessResult;
import com.etiya.rentACarSpring.dataAccess.abstracts.RentalDao;

@Service
public class RentalManager implements RentalService {

	private RentalDao rentalDao;
	private ModelMapperService modelMapperService;
	private CustomerFindexScoreService customerFindexScoreService;
	private UserService userService;
	private CarService carService;
	private CityService cityService;
	private PaymentService paymentService;
	//private MessageService messageService;

	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, UserService userService,
			CustomerFindexScoreService customerFindexScoreService, CarService carService,CityService cityService,
						 PaymentService paymentService) {
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.userService = userService;
		this.customerFindexScoreService = customerFindexScoreService;
		this.carService = carService;
		this.cityService=cityService;
		this.paymentService=paymentService;
		//this.messageService=messageService;

	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {
		PayCreditCardRequest payCreditCardRequest=new PayCreditCardRequest();
		Result result = BusinessRules.run(checkReturnDateExists(createRentalRequest.getCarId()),
				checkCustomerFindexScoreIsEnough(createRentalRequest.getUserId(),createRentalRequest.getCarId()),
				checkCarIsNotOnMaintenance(createRentalRequest.getCarId()),checkIfCarIsNotExists(createRentalRequest.getCarId()),checkIfUserNotExists(createRentalRequest.getUserId()),checkIfLimitIsInsufficient(payCreditCardRequest));

		if (result != null) {
			return result;
		}

		ApplicationUser user = this.userService.getByUserId(createRentalRequest.getUserId()).getData();
		Rental rental = modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		rental.setApplicationUser(user);
		rental.setTakenFromCityId(this.carService.getById(createRentalRequest.getCarId()).getData().getCity().getId());
		rental.setInitialKilometer(carService.getById(createRentalRequest.getCarId()).getData().getKilometer());
		this.rentalDao.save(rental);

		return new SuccessResult("this.messageService.getMessage(Messages.RentalIsSuccessful)");
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		Result result = BusinessRules.run(checkIfReturnKilometerLessThanInitialKilometer(updateRentalRequest.getRentalId(),updateRentalRequest.getReturnKilometer()));

		if (result != null) {
			return result;
		}

		ApplicationUser user;
		Car car;
		Rental rental=this.rentalDao.getById(updateRentalRequest.getRentalId());
		System.out.println(rental.getId());
		user=rental.getApplicationUser();
		car=rental.getCar();
		//rental = modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		rental.setApplicationUser(user);
		rental.setCar(car);
		rental.setReturnDate(updateRentalRequest.getReturnDate());
		rental.setReturnToCityId(updateRentalRequest.getReturnToCityId());
		rental.setReturnKilometer(updateRentalRequest.getReturnKilometer());
		City city=this.cityService.getById(updateRentalRequest.getReturnToCityId()).getData();
		car.setCity(city);
		car.setKilometer(updateRentalRequest.getReturnKilometer());
		this.rentalDao.save(rental);

		return new SuccessResult("this.messageService.getMessage(Messages.RentalUpdated)");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		//bussines rule yazılacak
		this.rentalDao.deleteById(deleteRentalRequest.getId());
		return new SuccessResult("this.messageService.getMessage(Messages.RentalDeleted)");
	}

	@Override
	public DataResult<List<RentalSearchListDto>> getAll() {
		List<Rental> result = this.rentalDao.findAll();
		List<RentalSearchListDto> response = result.stream()
				.map(rental -> modelMapperService.forDto().map(rental, RentalSearchListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<RentalSearchListDto>>(response,"this.messageService.getMessage(Messages.RentalsListed)");
	}

	@Override
	public DataResult<Integer> getDayBetweenDatesOfRental(int rentalId) {
		return new SuccessDataResult<>(this.rentalDao.getDayBetweenDatesOfRental(rentalId)) ;
	}

	@Override
	public DataResult<Integer> getDailyPriceOfRentedCar(int brandId) {
		return new SuccessDataResult<>(this.rentalDao.getDailyPriceOfRentedCar(brandId));
	}

	@Override
	public Result checkCarIsReturnedToSameCity(int rentalId) {
		Rental rental=this.rentalDao.getById(rentalId);
		if(rental.getTakenFromCityId()==rental.getReturnToCityId()){
			return new SuccessResult();
		}
		return new ErrorResult();
	}

	@Override
	public DataResult<Rental> getById(int rentalId) {
		return new SuccessDataResult<Rental>(this.rentalDao.getById(rentalId));
	}

	@Override
	public Double getAdditionalItemsTotalPrice(int rentalId){
		List<Double> items=this.rentalDao.getAdditionalItemsOfRelevantRental(rentalId);
		double additionalsTotalPrice=0;

		for(Double item:items){
			additionalsTotalPrice+=item;
		}
		return additionalsTotalPrice;
	}

	private Result checkReturnDateExists(int carId) {
		Rental rental = this.rentalDao.getByCarIdAndReturnDateIsNull(carId);
		if (rental != null) {
			return new ErrorResult("this.messageService.getMessage(Messages.CarIsOnRent)");
		}
		return new SuccessResult();

	}


	private int getCustomerFindexScore(int userId){
		int customerFindexScore=0;
		ApplicationUser user=this.userService.getByUserId(userId).getData();
		if(user.getIndividualCustomer()!=null){
			customerFindexScore=this.customerFindexScoreService.getFindexScoreOfIndividualCustomer();
		}
		if(user.getCorporateCustomer()!=null){
			customerFindexScore=this.customerFindexScoreService.getFindexScoreOfCorporateCustomer();
		}
		return customerFindexScore;
	}

	private Result checkCustomerFindexScoreIsEnough(int userId,int carId){
		int carFindexScore = this.carService.getById(carId).getData().getMinFindexScore();
		int customerFindexScore=getCustomerFindexScore(userId);
		if (customerFindexScore >= carFindexScore) {
			return new SuccessResult();
		}
		return new ErrorResult("this.messageService.getMessage(Messages.FindexScoreIsNotEnough)");
	}


	private Result checkCarIsNotOnMaintenance(int carId) {
		if (!this.carService.checkCarIsNotOnMaintenance(carId).isSuccess()) {
			return new ErrorResult("this.messageService.getMessage(Messages.CarIsOnMaintenance)");
		}
		return new SuccessResult();
	}
	
	private Result checkIfCarIsNotExists(int carId) {
		if(!this.carService.checkCarExists(carId).isSuccess()) {
			return new ErrorResult("this.messageService.getMessage(Messages.CarNotFound)");
		}
		
		return new SuccessResult();
	}

	private Result checkIfUserNotExists(int userId){
		if(!this.userService.checkUserExists(userId).isSuccess()){
			return new  ErrorResult("this.messageService.getMessage(Messages.UserIsNotFound)");
		}
		return  new SuccessResult();
	}

	private Result checkIfLimitIsInsufficient(PayCreditCardRequest payCreditCardRequest){
		if(!this.paymentService.payByCreditCard(payCreditCardRequest).isSuccess()){
			return new ErrorResult("this.messageService.getMessage(Messages.LimitIsInsufficient)");
		}
		return new SuccessResult();
	}
	private Result checkIfReturnKilometerLessThanInitialKilometer(int rentalId,int returnKilometer){
		int carId=rentalDao.getById(rentalId).getCar().getId();
		if(this.carService.getById(carId).getData().getKilometer()>returnKilometer){
			return new ErrorResult("this.messageService.getMessage(Messages.ReturnKilometerCanNotBeLessThanInitialKilometer)");
		}
		return new SuccessResult();
	}

}
