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
        ModelAndView mv = new ModelAndView("form", result.getModel());
        mv.addObject("formAction", "/form");
        return mv;
    }

    @RequestMapping("/reset")
    public String reset() {
        return "redirect:/page/form";
    }
}
