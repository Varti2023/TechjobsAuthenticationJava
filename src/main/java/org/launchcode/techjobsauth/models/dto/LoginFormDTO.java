package org.launchcode.techjobsauth.models.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;

public class LoginFormDTO {
    @NotNull
    @NotBlank
    @Size(min=3,max=20,message="Invalid User. Name must be between 3 and 20 characters.")
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 5, max = 30, message = "Invalid password. Must be between 5 and 30 characters.")
    private String password;

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
