package demo.springboot.security.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String hi(HttpServletResponse response) throws IOException{
		return "Hi!";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        model.addAttribute("greeting", "World");
        return "welcome";
    }
}
