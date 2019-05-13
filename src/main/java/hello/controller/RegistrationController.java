package hello.controller;

import hello.domain.User;
import hello.domain.dto.CaptchaResponseDto;
import hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model){
        model.put("message", "Add new user");
        return "registration";
    }

    // Данный метод добавляет пользователя в бащу данных
    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @RequestParam("password2") String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult, Model model){
    //Формируент запрос на гугл капчу
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess())
            model.addAttribute("captchaError", "Please, fill the captcha");

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm); 
        if (isConfirmEmpty)
            model.addAttribute("password2Error", "password confirmation can't be empty");

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm))
            model.addAttribute("passwordError", "Password are different");

        if (bindingResult.hasErrors() || isConfirmEmpty || !response.isSuccess())
        {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user))
        {
            model.addAttribute("usernameError", "User exist!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){

        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        }else {
            model.addAttribute("message", "Activation code is not found!");
            model.addAttribute("messageType", "danger");
        }

        return "login";
    }
}
