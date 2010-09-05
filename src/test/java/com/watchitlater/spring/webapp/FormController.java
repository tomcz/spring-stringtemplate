package com.watchitlater.spring.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FormController {

    @RequestMapping("/form")
    public ModelAndView process(@ModelAttribute("command") Command command, BindingResult result) {
        return new ModelAndView("templates/form", result.getModel());
    }
}
