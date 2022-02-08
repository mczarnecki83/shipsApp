package com.hobbyjoin.ships;
import com.hobbyjoin.ships.model.ship.Ship;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ShipController {

    private final ShipService shipService;

    @GetMapping("/ships")
    public String getAllShips(Model model,
                              @RequestParam(defaultValue = "1") Integer pageNo,
                              @RequestParam(defaultValue = "6") Integer pageSize,
                              @RequestParam(defaultValue = "name") String sortBy)
    {
        if (pageNo<=0) pageNo=1;
        Page<Ship> pagedResult = shipService.getAllShips(pageNo-1, 6, "name");
        if(pagedResult.getContent().size()>0) {
                  model.addAttribute("maxPage",pagedResult.getTotalPages());
                  model.addAttribute("ships",pagedResult.getContent());
                  model.addAttribute("pageNo",pageNo);
         }
        else{
            model.addAttribute("ships",null);
        }
        model.addAttribute("active_href","ship_data");
        return "ships";
    }



    @GetMapping("/ship-courses-native-query")
    public String getAllShipsWithRoutes(Model model,
                                        @RequestParam(defaultValue = "1") Integer pageNo)
    {
        Integer pageSize = 6;
        final List shipList = shipService.getCountRoutesNativeQuery(pageNo, pageSize);
        final Object shipsCountNativeQuery = shipService.getCountRoutesObjNativeQuery();
        final Double shipMeter = Double.parseDouble(shipsCountNativeQuery.toString());
        model.addAttribute("ships",shipList);
        model.addAttribute("maxPage",(int) Math.ceil(shipMeter/pageSize));
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("active_href","ship_data");
        return "routes";
    }




}