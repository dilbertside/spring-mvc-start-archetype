package ${package}.web;

import java.security.Principal;

import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@ModelAttribute("module")
	public String module() {
		return "home";
	}

	@GetMapping("/")
	public String index(Principal principal, Model model) {
		model.addAttribute("springVersion", SpringVersion.getVersion());
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
