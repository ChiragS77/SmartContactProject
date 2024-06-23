package com.SmartContactManager.controller;

import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.SmartContactManager.dao.ContactRepository;
import com.SmartContactManager.dao.UserRepository;
import com.SmartContactManager.entities.Contact;
import com.SmartContactManager.entities.User;
import com.SmartContactManager.helper.Message;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {

		String email = principal.getName();
		System.out.println(email);

		User user = userRepository.findByEmail(email);

		m.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashbord(Model model, Principal principal) {
		String email = principal.getName();
		System.out.println(email);

		User user = userRepository.findByEmail(email);

		model.addAttribute("user", user);

		return "normal/user_dashboard"; //
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");

		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(HttpSession session, @RequestParam("profileImage") MultipartFile file,
			@ModelAttribute("Contact") Contact contact, Principal principal) {
		try {
			String email = principal.getName();
			User user = this.userRepository.findByEmail(email);

			// process file and uploading file

			if (file.isEmpty()) {
				// file is empty
				System.out.println("File is empty..");
				contact.setImage("default.jpg");

			} else {
				// file the file to folder and update name of contacts
				contact.setImage(file.getOriginalFilename());

				java.io.File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + java.io.File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image Uploaded..");
			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("Data " + contact);

			// message success
			session.setAttribute("message", new Message("Your contact is added !! Add more..", "success"));

		} catch (Exception e) {
			System.out.println("Error..");
			session.setAttribute("message", new Message("Something went wrong !! Try again..", "danger"));

			e.printStackTrace();

		}
		return "normal/add_contact_form";
	}

	// show contacts
	// Pageble store 2 info
	// contacts per page = 5[n]
	// current page
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "show user contacts");

		// contact ki list ko bhejni he
		String email = principal.getName();

		Pageable pageable = PageRequest.of(page, 5);

		User user = userRepository.findByEmail(email);
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);

		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// show contact detail

	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {

		System.out.println(cId);
		Optional<Contact> optional = this.contactRepository.findById(cId);
		Contact contact = optional.get();

		// bug solve
		String email = principal.getName();
		User user = this.userRepository.findByEmail(email);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_details";
	}

// ---------------- delete form --------------
	@RequestMapping("/delete/{cId}")
	public String delete(@PathVariable Integer cId, Model model, Principal pricipal) {

		Contact contact = this.contactRepository.findById(cId).get();
		
		//trick delete mapping with user
		contact.setUser(null);

		model.addAttribute("message", new Message("Contact deleted successfully...", "success"));

		this.contactRepository.delete(contact);

		return "redirect:/user/show-contacts/0";
	}

	//-------------  update form handler  ---------
	@PostMapping("/update-contact/{cId}")
	public String updateForm(Model m,@PathVariable Integer cId) {
		
		m.addAttribute("title", "Update Contact");
		Contact contact = this.contactRepository.findById(cId).get();
		
		m.addAttribute("contact",contact);
			
		return "normal/update_form";
	}
	 
	
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,
			@RequestParam("profileImage")MultipartFile file,Model m,
			HttpSession session,Principal principal) 
	{
		
		System.out.println("COntact Name "+contact.getName());
		System.out.println("contact id is not saved..... "+contact.getcId());
		
		try {
			
		Contact contactOldDetails =	this.contactRepository.findById(contact.getcId()).get();
			
			
			//image
			if(!file.isEmpty()) {
				
			java.io.File saveFile =	new ClassPathResource("static/img").getFile();
			
		    Path path = Paths.get(saveFile.getAbsolutePath() +java.io.File.separator+ file.getOriginalFilename());
		    
			  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			  
			  contact.setImage(file.getOriginalFilename());
			}else {
				contact.setImage(contactOldDetails.getImage());
			}
		User user= this.userRepository.findByEmail(principal.getName());
		    contact.setUser(user);
			this.contactRepository.save(contact);
			
			System.out.println("contact has been saved....");
			
			
		}catch(Exception e) {
		
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		
		model.addAttribute("title","profile page");
		
		return "normal/profile";
	}
	
	
}
