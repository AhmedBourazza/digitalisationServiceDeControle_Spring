package org.system.digitalisationservicedecontrole.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String handleLogin() {
        return "C_login";
    }
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/tst")
    public String tst() {
        return "tst";
    }

}
