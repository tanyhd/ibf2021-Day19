package nus.edu.workshop19.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import nus.edu.workshop19.services.MarvelService;

@Controller
@RequestMapping(path="/marvel", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    @Autowired
    MarvelService marvelService;

    @GetMapping(path="{hero}")
    public String getCharater(@PathVariable String hero, Model model) {
        model.addAttribute("hero", marvelService.getHero(hero));
        return "result";
    }
}
