package ar.edu.itba.it.paw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.paw.dao.interfaces.FoodTypesDAO;
import ar.edu.itba.it.paw.model.FoodType;

@Service
public class FoodTypeServiceImpl implements FoodTypeService {
	
	FoodTypesDAO DAO;
	
	@Autowired
	private FoodTypeServiceImpl(FoodTypesDAO ftDAO) {
		this.DAO = ftDAO;
	}
	
	public List<FoodType> getAll() {
		return DAO.getAll();
	}
	
	public FoodType getSingleFoodType(int id) {
		return DAO.getSingleFoodType(id);
	}
	
}
