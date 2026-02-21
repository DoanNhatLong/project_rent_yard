package com.example.project_rent_yard.controller.admin;

import com.example.project_rent_yard.dto.UserCreateDto;
import com.example.project_rent_yard.entity.Role;
import com.example.project_rent_yard.entity.User;
import com.example.project_rent_yard.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admins/user")
@Controller
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("newUser") UserCreateDto userCreateDto,
            BindingResult bindingResult,
            Model model){
        if (bindingResult.hasErrors()) {
            List<User> users=userService.findAllByRole_Name("user");
            model.addAttribute("users", users);
            model.addAttribute("newUser", userCreateDto);
            model.addAttribute("openModal", true); // để mở lại modal
            return "admin/user"; // forward
        }
        User user=new User();
        BeanUtils.copyProperties(userCreateDto,user);
        Role role=new Role();
        role.setId(2);
        user.setRole(role);
        userService.save(user);
        return "redirect:/admins/user";
    }

    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable Integer id){
        User user=userService.findById(id);
        if(user.getStatus().name().equals("ACTIVE")){
            user.setStatus(User.Status.BLOCKED);
        }else{
            user.setStatus(User.Status.ACTIVE);
        }
        userService.save(user);
        return "redirect:/admins/user";
    }


}
