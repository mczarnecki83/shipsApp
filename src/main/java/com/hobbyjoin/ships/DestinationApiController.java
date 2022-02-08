package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.Destination;
import com.hobbyjoin.ships.model.ship.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/destination")
public class DestinationApiController {

    private final DestinationRepository destinationRepository;

    private ResponseEntity<String> responseElements(HttpStatus status, String message){
        Translator translator = Translator.getInstance();
        translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
        String manualJSON = "{\"code\":\""+status+"\", \"message\":\""+ translator.translate(message)+"\"}";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(manualJSON);

    }

    @PostMapping("/add")
    public Object addDestinationApi(@RequestBody Destination destination) {
        if(destination.getName().isEmpty()){
            return responseElements(HttpStatus.BAD_REQUEST,"name_cannot_by_empty");
        }else {
            final Optional<Destination> byName = destinationRepository.findByName(destination.getName());
            if(byName.isEmpty()){
                return new ResponseEntity<Destination>(destinationRepository.save(destination), HttpStatus.CREATED);
            }
            else{
                return responseElements(HttpStatus.CONFLICT,"error.new_name_already_exists_change_it");
            }
        }
    }

    @GetMapping("/")
    public List<Destination> getDestinationListApi() {
          return (List<Destination>) destinationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object destinationById(Model model, @PathVariable("id") int id) {
        final Optional<Destination> destById = destinationRepository.findById(id);
        if(destById.isEmpty()){
            return responseElements(HttpStatus.NOT_FOUND,"not_found");
        }
         return destById;
    }

    @PutMapping("/{id}")
    public Object putShip(@RequestBody Destination destination, @PathVariable int id) {
        if(destination.getName().isEmpty()){
            return responseElements(HttpStatus.BAD_REQUEST,"name_cannot_by_empty");
        }
        final Optional<Destination> checkDestinationExists = destinationRepository.findById(id);
        if(checkDestinationExists.isEmpty()){
            return responseElements(HttpStatus.NOT_FOUND,"not_found");
        }
        final Optional<Destination> checkNewNameExists = destinationRepository.findByName(destination.getName());
        if(!checkNewNameExists.isEmpty()){
            return responseElements(HttpStatus.CONFLICT,"error.new_name_already_exists_change_it");
        }
        destination.setId(id);
        final Destination save = destinationRepository.save(destination);
        return save;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteElement(@PathVariable int id) {
        final Optional<Destination> destById = destinationRepository.findById(id);
        if(destById.isEmpty()){
            return responseElements(HttpStatus.NOT_FOUND,"not_found");
        }
        destinationRepository.deleteById(id);
        return null;
    }
}