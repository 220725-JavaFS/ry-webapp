package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.models.Cars;
import com.revature.service.CarService;

public class CarServlet extends HttpServlet {

	
	private static final long serialVersionUID = 105L;
	private Cars car = new Cars();
	private CarService cserv = new CarService();
	private ObjectMapper mapper = new ObjectMapper();


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		StringBuilder sbuild = new StringBuilder(request.getRequestURI()); // /project1/cars/{id}
		sbuild.replace(0, request.getContextPath().length() + 1, "");
		//add log for doGet if you can get there
		if (sbuild.indexOf("/") != -1) {
			sbuild.replace(0, sbuild.indexOf("/") + 1, "");
			String bpn = sbuild.toString();
			try {
				int id = Integer.parseInt(bpn);
				Cars car = new Cars();
				car.setId(id);
				Object retCar = cserv.carByID(car);
				PrintWriter pwrite = response.getWriter();
				ObjectWriter owrite = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = owrite.writeValueAsString(retCar);
				pwrite.write(json);
				response.setStatus(200);

			} catch (Exception e) { //add log
				PrintWriter writer = response.getWriter();
				writer.write("Invalid Id do get");
				response.setStatus(404);
			}
		} else {
			//get all inventory
			List<Object> gcar = cserv.getInventory(car);
			PrintWriter pwrite = response.getWriter();
			// writejson
			ObjectWriter owrite = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = owrite.writeValueAsString(gcar);
			pwrite.write(json);
		}
	}

	@Override //insert car
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Cars car  = mapper.readValue(request.getInputStream(), Cars.class);
		cserv.newCar(car);
		PrintWriter pwrite = response.getWriter();
		ObjectWriter owrite = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = owrite.writeValueAsString(car);
		pwrite.write(json);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		StringBuilder uriString = new StringBuilder(request.getRequestURI());
		uriString.replace(0, request.getContextPath().length() + 1, "");
		if (uriString.indexOf("/") != -1){
			uriString.replace(0, uriString.indexOf("/") + 1, ""); //from beginning
			String bpn = uriString.toString();
			try {
				mapper.setSerializationInclusion(Include.NON_NULL);
				Cars car = mapper.readValue(request.getInputStream(), Cars.class);
				int id = Integer.parseInt(bpn);
				car.setId(id);
				Object retCar = cserv.removeCar(car); //ref service
				PrintWriter pwrite = response.getWriter();
				ObjectWriter owrite = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = owrite.writeValueAsString(retCar);
				pwrite.write(json);

			} catch (Exception e) {
				PrintWriter pwrite = response.getWriter();
				pwrite.write("Invalid Id do delete");
			}

		}
	}

}
