package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller

public class UserController {
    private final UserService userService;
    private final RoleDao roleDao;

    public UserController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping("/user")
    public String showInfo(Principal principal, Model model){
        User user = userService.showUserByName(principal.getName());
        model.addAttribute("user",user);
        return "user";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("info", userService.showUserByName(principal.getName()));
        return "users/listUsers";
    }

    @GetMapping("/admin/users/{id}")
    public String showUserById(@PathVariable("id") int id, Model model, Principal principal) {
        model.addAttribute("user", userService.showUserById(id));
        model.addAttribute("info", userService.showUserByName(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        return "users/show";
    }

    @GetMapping("/admin/users/new")
    public String newUser(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("roles",roleDao.findAll());
        model.addAttribute("info", userService.showUserByName(principal.getName()));
        return "users/new";
    }

    @PostMapping("/admin/users")
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id, Principal principal) {
        model.addAttribute("user", userService.showUserById(id));
        model.addAttribute("roles",roleDao.findAll());
        model.addAttribute("info", userService.showUserByName(principal.getName()));
        return "users/edit";
    }

    @PatchMapping("/admin/users/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/users/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}