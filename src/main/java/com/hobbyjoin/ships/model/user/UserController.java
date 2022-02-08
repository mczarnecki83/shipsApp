package com.hobbyjoin.ships.model.user;
import com.hobbyjoin.ships.DestinationService;
import com.hobbyjoin.ships.model.ship.Destination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


@Controller
public class UserController {

    private UserService userService;
    private DestinationService destinationService;

    public UserController(UserService userService, DestinationService destinationService) {
        this.userService = userService;
        this.destinationService = destinationService;
    }

    @GetMapping("/my-account")
    public String myAccount(Model model) {
        final HashMap<String, String> loggedUserData = userService.getLoggedUserData();
        List<Destination> myDestinations = null;
        if (loggedUserData.size()>0) {
           myDestinations = destinationService.getMyDestinations(Long.parseLong(loggedUserData.get("id")));
        }
            model.addAttribute("destinations", myDestinations);
            model.addAttribute("user_id", loggedUserData.get("id"));
            model.addAttribute("user_role", loggedUserData.get("role"));

        model.addAttribute("active_href", "account");
        return "account";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("AppUser", new AppUser());
        model.addAttribute("active_href", "login");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        model.addAttribute("active_href", "register");
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("confirmPassword") String confirmPassword,AppUser appUser, Model model, RedirectAttributes redirectAttributes, @Valid final AppUser userValid, BindingResult bindingResult) {
    //TODO unique key on email
        final String myError = userService.getValidationPasswordsError(appUser, confirmPassword);
        if(!myError.isEmpty()){
            bindingResult.rejectValue("password", "error.password", myError);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("active_href", "register");
            return "/register";
        } else {
            userService.addUser(appUser);
            redirectAttributes.addFlashAttribute("message", "flash.registered_check_mail");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            redirectAttributes.addAttribute("active_href","login");
            return "redirect:/login";
        }
    }

    @GetMapping("/token")
    public String singup(@RequestParam String value, RedirectAttributes redirectAttributes) {
        final AppUser userFromVerivyToken = userService.getUserFromVerifyToken(value);
        userService.setUserEnabled(userFromVerivyToken);
        redirectAttributes.addFlashAttribute("message", "flash.you_can_login_now");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        redirectAttributes.addAttribute("active_href","login");
        return "redirect:/login";
    }
}
