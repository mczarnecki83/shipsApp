package com.hobbyjoin.ships;

import com.hobbyjoin.ships.exception.NoPermissionException;
import com.hobbyjoin.ships.model.ship.*;
import com.hobbyjoin.ships.model.user.UserService;
import com.hobbyjoin.ships.model.weather.WeatherMini;
import com.hobbyjoin.ships.port.WeatherOpenWeatherMap.WeatherOpenWeatherMapApiImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@EnableCaching
@RequiredArgsConstructor
@Controller
@RequestMapping("/destination")
public class DestinationController {
    private final UserService userService;
    private final RouteService routeService;
    private final DestinationService destinationService;
    private final WeatherOpenWeatherMapApiImpl weatherOpenWeatherMapApi;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("")
    public String destinationList(Model model) {
        final List<Destination> destinationsWhiteList = destinationService.getDestinationsIfBlackListIs(false);
        final HashMap<String, String> loggedUserData = userService.getLoggedUserData();
        model.addAttribute("user_id", loggedUserData.get("id"));
        model.addAttribute("user_role", loggedUserData.get("role"));
        model.addAttribute("destinations", destinationsWhiteList);
        model.addAttribute("active_href","ports");
        return "destination/list";
    }


    @GetMapping("/{id}")
    public String destinationById(Model model, @PathVariable("id") int id) {
        final Optional<Destination> destinationFromId = destinationService.getDestinationFromId(id);
        final HashMap<String, String> loggedUserData = userService.getLoggedUserData();

        String dateToSearch = routeService.getDate("");
        final List<Route> routestoPort =  routeService.getShipRoutesToPort(dateToSearch,id);
        model.addAttribute("routes",routestoPort);

        model.addAttribute("user_id", loggedUserData.get("id"));
        model.addAttribute("user_role", loggedUserData.get("role"));
        model.addAttribute("destinationData", destinationFromId.orElse(null));
        model.addAttribute("active_href","ports");
        return "destination/singlewithroutes";
    }




    @GetMapping("/add")
    public String addDestinationForm(Model model) {
        model.addAttribute("destination", new Destination());
        model.addAttribute("active_href","ports");
        return "destination/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveDestination(@ModelAttribute("destination") Destination destination, RedirectAttributes redirectAttributes, @Valid final Destination destinationValid, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/destination/add";
        } else {
            destinationService.saveDestination(destination);
            redirectAttributes.addFlashAttribute("message", "flash.added");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/destination/";
        }
    }


    @GetMapping("/edit/{id}")
    public ModelAndView editDestinationForm(@PathVariable("id") int id) {
        final Optional<Destination> destinationFromId = destinationService.getDestinationFromId(id);
        final HashMap<String, String> loggedUserData = userService.getLoggedUserData();
        if (null == destinationFromId.get().getUserId() || destinationFromId.get().getUserId() != Integer.parseInt(loggedUserData.get("id"))) {
           throw new NoPermissionException();
        }
        ModelAndView editView = new ModelAndView("destination/edit");
        editView.addObject("destination",destinationFromId.orElse(null));
        editView.addObject("oldName",destinationFromId.get().getName());
        return editView;
    }

    @PostMapping(value = "/edit")
    public String saveEditDestination(@ModelAttribute("oldName") String oldName, @ModelAttribute("destination") Destination destination, RedirectAttributes redirectAttributes, @Valid final Destination destinationValid, BindingResult bindingResult) {
        final HashMap<String, String> loggedUserData = userService.getLoggedUserData();
        if (null == destination.getUserId() || destination.getUserId() != Integer.parseInt(loggedUserData.get("id"))) {
            throw new NoPermissionException();
        }

        final String myError = destinationService.getValidationNameExistsError(oldName,destination.getName());
        if(!myError.isEmpty()){
            bindingResult.rejectValue("name", "error.name", myError);
        }

        if (bindingResult.hasErrors()) {
            return "/destination/edit";
        }
            destinationService.saveDestination(destination);
            redirectAttributes.addFlashAttribute("message", "flash.edited");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/destination/";
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(Model model, @PathVariable("id") int id) {
        final Optional<Destination> destinationFromId = destinationService.getDestinationFromId(id);
        model.addAttribute("destinationData", destinationFromId.orElse(null));
        model.addAttribute("active_href","ports");
        return "destination/delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String deleteElement(@RequestParam int id, RedirectAttributes redirectAttributes) {
          destinationService.deleteDestination(id);
          redirectAttributes.addFlashAttribute("message", "flash.deleted");
          redirectAttributes.addFlashAttribute("alertClass", "alert-success");
          return "redirect:/destination/";
    }



    @PostMapping("/ajax")
    @ResponseBody
    @Cacheable(cacheNames = "apiCache")
    public String ajaxWeather(@ModelAttribute("latitude") Double latitude, @ModelAttribute("longitude") Double longitude) {
        if(latitude==null || longitude==null){
            return "";
        }
        final WeatherMini weatherApi = weatherOpenWeatherMapApi.getWeatherApi(latitude, longitude);
        return destinationService.prepareReturnAjaxWeatherMessage(weatherApi);
    }


}