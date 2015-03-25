package com.epam.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

	/**
	 * This method execute POST requests for CREATING new image
	 * 
	 * @param imageModel
	 * @param bindingResult
	 * @param imageFile
	 * @param model
	 * @return mapping to the createForm.jsp
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String receiveData(
			@Valid ImageModel imageModel,
			BindingResult bindingResult,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			Model model) {
		// If validation failed - return to create-form
		if (bindingResult.hasErrors()) {
			System.out.println("errors!!!");
			return "createForm";
		}
		// Else (if validation is correct) - creating new file and write to the
		// file system
		model.addAttribute("description", imageModel.getDescription());
		System.out.println("Description: " + imageModel.getDescription());
		// ---- Start of file receiving, validating, writing...
		try {
			// Validate file - should be *.jpg
			validateImage(imageFile);
			// Validate name of file - should not be duplicated
			validateDuplicateFile(imageModel.getDescription() + ".jpg",
					imageFile);
		} catch (TechnicalException e) {
			bindingResult.addError(new FieldError("imageModel", "description",
					e.getMessage()));
			// If image not JPG, or image already exist - return to create-form
			return "createForm";
		}
		saveImage(imageModel.getDescription() + ".jpg", imageFile);
		model.addAttribute("success", " Image successfuly saved");

		// --- Finish of file receiving, validating, writing...
		return "createForm";
	}

	/**
	 * This method execute GET requests for shows the list of images
	 * 
	 * @param model
	 * @param imageModel
	 * @return mapping to list.jsp, and list of filenames(in the model`s
	 *         attribute "listNames")
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showList(Model model, ImageModel imageModel) {
		ArrayList<String> listNames = new ArrayList<String>();
		model.addAttribute("imageModel", imageModel);
		System.out.println("GET: " + imageModel.getDescription());
		// Create an array of files, extract filenames (without path) and put to
		// the attribute "listNames"
		File[] listFiles = getList();
		for (File file2 : listFiles) {
			listNames.add(file2.getName());
			System.out.println(file2.getName());

		}
		model.addAttribute("listNames", listNames);
		return "list";

	}

	/**
	 * This method execute GET requests for show create-form
	 * 
	 * @param model
	 * @param imageModel
	 * @return mapping to the createForm.jsp
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/showCreateForm")
	public String showCreateForm(ImageModel imageModel) {
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

	/**
	 * This method scan resources folder and create an array of existing files
	 * (exclude directories)
	 * 
	 * @return array of existing files (exclude directories)
	 */
	public File[] getList() {
		File file = new File("resources/");
		System.out.println(file.getAbsolutePath().toString());
		File[] listFiles = file.listFiles();

		// System.out.println(Arrays.toString(listFiles));
		return listFiles;

	}

	public void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new TechnicalException("Only JPG images are acceptable");
		}

	}

	private void validateDuplicateFile(String filename, MultipartFile imageFile) {
		File file = new File("resources/" + filename);
		if (file.exists()) {
			throw new TechnicalException("Image with this name already exist");
		}
	}
}
