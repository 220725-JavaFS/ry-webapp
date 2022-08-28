package com.revature.service;

import java.util.List;

import com.revature.daos.ORMImpl;


public class CarService {


	private ORMImpl carORM = new ORMImpl();


	public List<Object> getInventory(Object cobj){
		return carORM.getAll(cobj);
	}

	public Object newCar(Object cobj) {
		return carORM.insertItem(cobj);
	}

	public Object updateCar(Object cobj) {
		return carORM.updateItem(cobj);
	}

	public Object removeCar(Object cobj) {
		return carORM.removeItem(cobj);
	}

	public Object carByID(Object cobj) {

		return carORM.findById(cobj);
	}







}
