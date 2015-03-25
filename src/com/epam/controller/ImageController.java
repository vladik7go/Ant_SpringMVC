package com.epam.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.epam.exception.TechnicalException;
import com.epam.model.ImageModel;

/**
 * This controller handles REST requests to resource:
 * http://localhost:8080/AntSpringMVC1/images/
 * 
 * @author Ivan_Filimonau
 *
 */
@Controller
@RequestMapping("/images")
public class ImageController {

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String receiveData(
			@Valid ImageModel imageModel,
			BindingResult bindingResult,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			Model model) {

		if (bindingResult.hasErrors()) {
			System.out.println("errors!!!");
			return "createForm";
		}
		model.addAttribute("description", imageModel.getDescription());
		System.out.println("Description: " + imageModel.getDescription());
		// ---- File receiving
		try {
			validateImage(imageFile);
		} catch (TechnicalException e) {
			bindingResult.addError(new FieldError("imageModel", "description",
					" Attached file should be JPG"));
			return "createForm";
		}
		saveImage(imageModel.getDescription() + ".jpg", imageFile);
		// ---
		return "createForm";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showList(Model model, ImageModel imageModel) {
		ArrayList<String> listNames = new ArrayList<String>();
		model.addAttribute("imageModel", imageModel);
		System.out.println("GET: " + imageModel.getDescription());
		File[] listFiles = getList();
		for (File file2 : listFiles) {
			listNames.add(file2.getName());
			System.out.println(file2.getName());
			
		}
		model.addAttribute("listNames", listNames);
		return "list";

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/showCreateForm")
	public String showCreateForm(Model model, ImageModel imageModel) {
		ArrayList<String> listNames = new ArrayList<String>();
		model.addAttribute("imageModel", imageModel);
		System.out.println("GET: " + imageModel.getDescription());
		File[] listFiles = getList();
		for (File file2 : listFiles) {
			listNames.add(file2.getName());
			System.out.println(file2.getName());
			
		}
		model.addAttribute("listNames", listNames);
		return "createForm";

	}

	private void saveImage(String filename, MultipartFile imageFile) {
		File file = new File("resources/" + filename);
		System.out.println(file.getAbsolutePath().toString());
		try {
			FileUtils.writeByteArrayToFile(file, imageFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public File[] getList() {
		File file = new File("resources/");
		System.out.println(file.getAbsolutePath().toString());
		File[] listFiles = file.listFiles();
		
//		System.out.println(Arrays.toString(listFiles));
		return listFiles;

	}

	public void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new TechnicalException("Only JPG images accepted");
		}

	}
}
