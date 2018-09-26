package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.apap.tutorial3.service.PilotService;
import com.apap.tutorial3.model.PilotModel;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;

	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
					@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
					@RequestParam(value = "name", required = true) String name,
					@RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel (id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}

	@RequestMapping("/pilot/viewall")
	public String viewall (Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value= {"pilot/view/license-number/{licenseNumber}"})
	public String viewPilotPath (@PathVariable String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		if(archive == null) {
			model.addAttribute("licenseNumber", licenseNumber);
			return "error";
		}
		else {
			model.addAttribute("pilot", archive);
			return "view-pilot";
		}	
	}
	
	@RequestMapping(value= {"pilot/update/license-number/{licenseNumber}/fly-hour/{newHour}"})
	public String updateFlyHours (@PathVariable String licenseNumber, @PathVariable Integer newHour, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		if(archive == null) {
			model.addAttribute("licenseNumber", licenseNumber);
			return "error";
		}
		else {
			archive.setFlyHour(newHour);
			return "sukses";
		}	
	}
	
	@RequestMapping(value = {"pilot/delete/id/{id}"})
	public String deletePilot (@PathVariable String id, Model model) {
		List <PilotModel> pilots = pilotService.getPilotList();
		PilotModel pil = null;
		for (int i= 0; i<pilots.size();i++) {
			if (pilots.get(i).getId().equals(id)) {
				pil = pilots.get(i);
				pilots.remove(i);
			}
		}
		if (pil == null) {
			return "error";
		}
		else {
			return "sukses";
		}
	}
}