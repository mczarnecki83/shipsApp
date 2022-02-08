package com.hobbyjoin.ships;
import com.hobbyjoin.ships.model.ship.*;
import com.hobbyjoin.ships.port.ShipBarentsApi.ShipListBarentsApiImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Controller
public class RouteController{
    private final RouteService routeService;
    private final InputShipApiService inputShipApiService;
    private final DestinationRepository destinationRepository;

    //ShipsListManualApiImpl shipListBarentsApiImpl = new ShipsListManualApiImpl();
    //ShipListBarentsApiImpl shipListBarentsApiImpl = new ShipListBarentsApiImpl(destinationRepository);


    @GetMapping("/map-mmsi/{mmsi}")
    public String mapMSSI(Model model,@PathVariable("mmsi") int itemid) {
        final List<Route> routes = routeService.getRoutesFromMmsi(itemid);
        model.addAttribute("active_href","todays_routes");
        model.addAttribute("routes",routes);
        return "mapmmsi";
    }

    @GetMapping("/clear-standing-ships")
    public String clearStandingShips(RedirectAttributes redirectAttributes) {
        routeService.clearStandingShips();
        redirectAttributes.addFlashAttribute("message", "flash.the_data_has_been_deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/";
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/api-links")
    public String apiLinks(Model model) {
        model.addAttribute("active_href","api_endpoints");
        return "apiLinks";
    }

    @GetMapping("/shipsMap")
    public String shipsMap(Model model) {
        final List<Route> allShipsRoutes = routeService.getAllShipsRoutes();
        model.addAttribute("ships",allShipsRoutes);
        model.addAttribute("active_href","local_hobby");
        model.addAttribute("maps","yes");
        return "shipsMap";
    }

    @GetMapping("/save")
    private String analysisDataFromShipsDto(RedirectAttributes redirectAttributes) {
        ExecutorService executorService  = Executors.newFixedThreadPool(10);
        ShipListBarentsApiImpl shipListBarentsApiImpl = new ShipListBarentsApiImpl(destinationRepository);
        final List<ShipRouteDto> shipsFromArea = inputShipApiService.getShipsFromAreaFromApiAsShipDto(shipListBarentsApiImpl);
        for (ShipRouteDto singleShip : Optional.ofNullable(shipsFromArea).orElse(Collections.emptyList())) {
            executorService.submit(()->routeService.proceedingsSingleShipsDtoEntry(singleShip));
            //routeService.proceedingsSingleShipsDtoEntry(singleShip);
        }
        executorService.shutdown();
        redirectAttributes.addFlashAttribute("message", "flash.the_data_has_been_saved");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/";
    }

    @GetMapping("/map-date")
    public String mapDate(Model model,@RequestParam(required = false) String date) {
        String dateToSearch = routeService.getDate(date);
        final List routesByDate = routeService.getAllRoutesByDate(dateToSearch);
        model.addAttribute("actualDate",dateToSearch);
        model.addAttribute("routes",routesByDate);
        model.addAttribute("active_href","todays_routes");
        return "mapdate";//native query
    }

    @GetMapping("/map-popular-native")
    public String mapPopularNative(Model model,@RequestParam(required = false) String date) {
        String dateToSearch = routeService.getDate(date);
        final List<Route> popularRoutes =  routeService.getPopularRoutesByDateNative(dateToSearch);
        model.addAttribute("actualDate",dateToSearch);
        model.addAttribute("routes",popularRoutes);
        model.addAttribute("active_href","long_routes_nq");
        return "mappopular_native";//object
    }

    @GetMapping("/map-popular")
    public String mapPopular(Model model,@RequestParam(required = false) String date) {
        String dateToSearch = routeService.getDate(date);
        final List<Route> popularRoutes =  routeService.getPopularRoutesByDate(dateToSearch);
        model.addAttribute("actualDate",dateToSearch);
        model.addAttribute("routes",popularRoutes);
        model.addAttribute("active_href","long_routes");
        return "mappopular";//object
    }
}