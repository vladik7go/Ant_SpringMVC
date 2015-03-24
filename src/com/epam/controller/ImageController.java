package com.epam.controller;

import java.io.File;
import java.io.IOException;

import javax.management.RuntimeErrorException;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
		validateImage(imageFile);
		saveImage(imageModel.getDescription() + ".jpg", imageFile);
		// ---

		return "createForm";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String start(Model model) {
		ImageModel imageModel = new ImageModel();
		model.addAttribute("imageModel", imageModel);
		return "createForm";

	}

	private void saveImage(String filename, MultipartFile imageFile) {
		File file = new File("resources/" + filename);

		System.out.println(file.getAbsolutePath().toString());
		try {
			FileUtils.writeByteArrayToFile(file, imageFile.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new TechnicalException("Only JPG images accepted");
		}

	}
}
