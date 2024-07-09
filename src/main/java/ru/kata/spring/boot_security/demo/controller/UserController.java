package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
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
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "User-list";
    }

    @GetMapping("/user-create")
    @PreAuthorize("hasRole('admin')")
    public ModelAndView createUserForm(Model model) {
        User user = new User();
        ModelAndView mav = new ModelAndView("User-create");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/user-create")
    @PreAuthorize("hasRole('admin')")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user-delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user-update/{id}")
    @PreAuthorize("hasRole('admin')")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "User-update";
    }

    @PostMapping("/admin/user-update")
    @PreAuthorize("hasRole('admin')")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    public String getUserProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "user";
    }
}

