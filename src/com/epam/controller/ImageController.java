package com.epam.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpRequest;
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
	public String createImage(
			@Valid ImageModel imageModel,
			BindingResult bindingResult,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			Model model, HttpServletRequest req) {
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
		String realPath = req.getSession().getServletContext().getRealPath("/")
				+ "resources/images/" + imageModel.getDescription() + ".jpg";
		try {
			// Validate file - should be *.jpg
			validateJPG(imageFile);
			// Validate name of file - should not be duplicated
			validateDuplicateFile(realPath);
		} catch (TechnicalException e) {
			bindingResult.addError(new FieldError("imageModel", "description",
					e.getMessage()));
			// If image not JPG, or image already exist - return to create-form
			return "createForm";
		}
		String realURL = req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + req.getContextPath()
				+ "/resources/images/" + imageModel.getDescription() + ".jpg";
		saveImage(realPath, imageFile);
		model.addAttribute("success", " Image successfuly saved");
		model.addAttribute("linkToImage", realURL);

		// --- Finish of file receiving, validating, writing...
		return "createForm";
	}

	/**
	 * This method execute GET requests for shows the list of images
	 * 
	 * @param model
	 * @param imageModel
	 * @return mapping to list.jsp, and Map of filenames(in the model`s
	 *         attribute "mapNames")
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showListOfImages(Model model, ImageModel imageModel,
			HttpServletRequest req) {
		Map<String, String> listMap;
		System.out.println("GET: " + imageModel.getDescription());
		listMap = getMap(req);
		// put Map to the attribute "mapNames"
		model.addAttribute("mapNames", listMap);
		return "list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/showImageById")
	public String showImageById(Model model,
			@RequestParam(value = "imageId", required = true) String imageId) {
		System.out.println(imageId);
		model.addAttribute("imageURL", imageId);
		return "showImage";
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

	/**
	 * Save image to file system
	 * 
	 * @param filename
	 * @param imageFile
	 */
	private void saveImage(String filename, MultipartFile imageFile) {
		File file = new File(filename);
		System.out.println(file.getAbsolutePath().toString());
		try {
			FileUtils.writeByteArrayToFile(file, imageFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method scan resources/images/ folder and create a Map of existing
	 * files
	 * 
	 * @param req
	 * @return Map (key=fileName, value=URL to file)
	 */
	private Map<String, String> getMap(HttpServletRequest req) {

		// scan resources/images/ folder and create an array of existing files
		String realPathFolder = req.getSession().getServletContext()
				.getRealPath("/")
				+ "resources/images/";
		File file = new File(realPathFolder);
		System.out.println(file.getAbsolutePath().toString());
		System.out.println(req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + req.getContextPath());
		File[] listFiles = file.listFiles();
		// Create a map (key=fileName, value=URL to file)
		HashMap<String, String> listMap = new HashMap<String, String>();
		for (File file2 : listFiles) {
			// listMap.put(file2.getName(), req.getSession().getServletContext()
			// .getRealPath("/")
			// + "resources/images/" + file2.getName());
			listMap.put(
					file2.getName(),
					req.getScheme() + "://" + req.getServerName() + ":"
							+ req.getServerPort() + req.getContextPath()
							+ "/resources/images/" + file2.getName());
			System.out.println(file2.getName());

		}

		return listMap;
	}

	/**
	 * Validate on equivalence to JPG
	 * 
	 * @param image
	 */
	private void validateJPG(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new TechnicalException("Only JPG images are acceptable");
		}

	}

	/**
	 * Validate on uniqueness of image in target folder
	 * 
	 * @param filename
	 */
	private void validateDuplicateFile(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			throw new TechnicalException("Image with this name already exist");
		}
	}
}
