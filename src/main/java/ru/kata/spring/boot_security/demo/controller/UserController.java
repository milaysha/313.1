package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class UserController {


    private final UserService userService;
    private final RoleRepository roleRepository;
    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('admin')")
    public String findAll(ModelAndView mav, Model model, Authentication authentication) {
        List<User> users = userService.findAll();
        User user = new User();
        model.addAttribute("users", users);
        model.addAttribute("createUsers", user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        model.addAttribute("curUsersRoles", authentication.getAuthorities().stream().map(r-> r.getAuthority()).collect(Collectors.toList()));
        return "User-list";
    }

    @PostMapping("/admin/user-create")
    @PreAuthorize("hasRole('admin')")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user-delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteUser(@PathVariable("id") Long id) {
        if(userService.findById(id) == null) {
            return "User-list";
        }
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/user-list")
    @PreAuthorize("hasRole('admin')")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public String getUserProfile(Model model, @AuthenticationPrincipal User user,Authentication authentication) {
        model.addAttribute("user", user);
        model.addAttribute("curUsersRoles", authentication.getAuthorities().stream().map(r-> r.getAuthority()).collect(Collectors.toList()));
        return "UserInfo";
    }
}

