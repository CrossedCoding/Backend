package com.revature.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.revature.models.Patient;
import com.revature.models.User;

import com.revature.security.models.TokenHolder;

import com.revature.security.models.RoleConstants;
import com.revature.security.services.RoleService;

import com.revature.services.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class userController {

	@Autowired
	private UserService userService;

	@Autowired
	public userController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<User>> findAllUsers(){
		List<User> all = userService.findAllUsers();
		
		if(all.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(all);
	}
	
	@GetMapping("/patient/doctor/{inputString}")
	public ResponseEntity<List<Patient>> findPatientsByString(@PathVariable("inputString") String inputString) {
		
		System.out.println("String Received : " + inputString);
		
		List<Patient> patients = userService.findAllPatients();
				
		return ResponseEntity.status(201).body(patients);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") String id ){
		Optional<User> optional = userService.findByUserId(id);
		
		if(optional.isPresent())
		{
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.noContent().build();
	}

	
	@GetMapping("/doctorPatientMap/{firstName}/{lastName}")
	public ResponseEntity<List<Patient>> findDoctorPatientMapping(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
	
		
		List<Patient> returner = userService.getDoctorPatientData(firstName, lastName).get();
		
		if (returner.size() > 0) { return ResponseEntity.ok(returner); }
		
		else { return ResponseEntity.noContent().build(); }
		
	}
	
	@PostMapping("/registration")
	public ResponseEntity<User> insert(@RequestBody User user){
	
		String id = user.getId();
		
		System.out.println("ID : " + id);
		System.out.println("User : " + user);
		
		if(id == null) {
			
			System.out.println("Bad Request");
			
			return ResponseEntity.badRequest().build();
		}
		
			System.out.println("Good Request");
		
		userService.addOrUpdateUser(user);
		
		return ResponseEntity.status(201).body(user);
	
	}
	
	
	
}
