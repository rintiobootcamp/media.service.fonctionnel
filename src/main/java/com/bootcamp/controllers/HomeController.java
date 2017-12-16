/**
 * Created by darextossa on 5/4/17.
 */

package com.bootcamp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Bello
 */
@CrossOrigin(origins = "*")
@Controller
//@RequestMapping("/")
public class HomeController {

    /**
     * The web service home page
     * @return the home page
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}