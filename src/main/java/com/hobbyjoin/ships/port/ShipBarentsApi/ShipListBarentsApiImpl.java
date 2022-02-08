package com.hobbyjoin.ships.port.ShipBarentsApi;
import com.fasterxml.jackson.databind.JsonNode;
import com.hobbyjoin.ships.model.ApiKey;
import com.hobbyjoin.ships.model.ship.Destination;
import com.hobbyjoin.ships.model.ship.DestinationRepository;
import com.hobbyjoin.ships.model.ship.ShipRoute;
import com.hobbyjoin.ships.model.ship.ShipRouteApi;
import com.hobbyjoin.ships.port.ShipBarentsApi.PrivateModel.ShipBarents;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class ShipListBarentsApiImpl implements ShipRouteApi, ApiKey {

    DestinationRepository destinationRepository;

    public ShipListBarentsApiImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }
    private String apiKey = null;
    public ShipListBarentsApiImpl() {
    }

    @Override
    public String getApiKey() {
        final String userLogin = "czarnymic@interia.pl:Czarnymic";
        final String userPassword = "";
        final String apiUrl = "https://id.barentswatch.no/connect/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        body.add("client_id", userLogin);
        body.add("scope", "api");
        body.add("client_secret", userPassword);
        body.add("grant_type", "client_credentials");

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, String.class);

        if(response.getStatusCodeValue()==200){
            String responseBody = response.getBody();
            String startSubstring = "access_token\":\"";
            int startIndex = responseBody.indexOf(startSubstring);
            int endIndex = responseBody.indexOf("\",\"expires_in");
            String stringToBeChecked = responseBody.substring(startIndex + startSubstring.length(), endIndex);
            if(stringToBeChecked.length()>500){
                this.apiKey = stringToBeChecked;
                return stringToBeChecked;
            }
        }
        return null;
    }



    private String filterNameDestination(String destination) {
        if (!(destination==null || destination.equals(""))){
            if(destination.matches(".*\\d.*")){return"";}
            if(destination.matches(".* .*")){return"";}
            if(destination.matches(".*\\(.*")){return"";}
            if(destination.matches(".*>.*")){
                String[] split = destination.split(">");
                return split[1];//FLAKK>RORVIK//From>To
            }
            if(destination.matches(".*-.*")){
                String returnValue="";
                try {
                    String[] split = destination.split("-");
                    returnValue = split[1];
                }
                catch (Exception e){
                }
                return returnValue;//FLAKK-RORVIK//From-To
            }
            return destination;
        }
        return "";
    }

    @Override
    public List<ShipRoute> getShips() {
        if(this.apiKey==null){
            getApiKey();
            //TODO renev key
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "+this.apiKey);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        //ResponseEntity<ShipBarents[]> exchange = restTemplate.exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=10.09094&Xmax=10.67047&Ymin=63.3989&Ymax=63.58645",HttpMethod.GET,httpEntity,ShipBarents[].class);
        //ResponseEntity<ShipBarents[]> exchange = restTemplate.exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=8.09094&Xmax=11.67047&Ymin=59.3989&Ymax=64.58645",HttpMethod.GET,httpEntity,ShipBarents[].class);
        ResponseEntity<ShipBarents[]> exchange = restTemplate.exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=4.190875&Xmax=12.0319559&Ymin=57.4906503&Ymax=61.9624142",HttpMethod.GET,httpEntity,ShipBarents[].class);
        List<ShipRoute> collect = Stream.of(exchange.getBody()).map(
                track -> new ShipRoute.Builder()
                        .name(track.getName())
                        .mmsi(track.getMmsi())
                        .latitude(track.getGeometry().getCoordinates().get(1))
                        .longitude(track.getGeometry().getCoordinates().get(0))
                        .actualCourse(track.getCog())
                        .actualSpeed(track.getSog())
                        .shipType(track.getShipType())
                        .destination(filterNameDestination(track.getDestination()))
                        .arrivalTime((track.getEta()==null) ? null : LocalDateTime.parse(track.getEta()))
                        .build()
        ).collect(Collectors.toList()); //collection without Destination lati/long
        List<ShipRoute> shipRoutesWithCoords = addDestinationLatiLongFacade(collect);
        return shipRoutesWithCoords;
    }



    private List<ShipRoute> addDestinationLatiLongFacade(List<ShipRoute> collect) {
        Map<String, String> destinationsFromActualSnapschot = new HashMap<>();
        for (ShipRoute singleShip:collect){
            if(singleShip.getDestination()!=""){
                destinationsFromActualSnapschot.putIfAbsent(singleShip.getDestination(),singleShip.getDestination());
            }
        }

        Map<String, String> destinationsFromDB = new HashMap<>();
        final List<Destination> allDestinations = destinationRepository.findAll();
        for (Destination singleDestination : allDestinations) {
            destinationsFromActualSnapschot.remove(singleDestination.getName());
        }
        ExecutorService executorService  = Executors.newFixedThreadPool(10);
        for (String soloDestination : destinationsFromActualSnapschot.keySet()) {
            if(soloDestination.length()<3){
                continue;
            }
            executorService.submit(()->processSoloDestination(soloDestination));
         }
        executorService.shutdown();
        return  collect;
    }


    private void processSoloDestination(String soloDestination){
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = "http://api.positionstack.com/v1/forward?access_key=467830a136fd57f650dcf49e5da7b946&query=" + soloDestination;
            JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("data");
            if (data == null) {
                setNewDestination("",true,soloDestination,null,null);

            }
            else {
                //loop because sample query FLAKK -> api return FLAK (first row) and FLAKK (second row)
                final JsonNode jsonNode = data.get(data.size() - 1);
                String nname = jsonNode.get("name").asText().toUpperCase(Locale.ROOT);
                double latitude = jsonNode.get("latitude").asDouble();
                double longitude = jsonNode.get("longitude").asDouble();
                if (nname.equals(soloDestination)) {
                    final Optional<Destination> checkNameExists = destinationRepository.findByName(soloDestination);
                    if (checkNameExists.isEmpty()) {
                        setNewDestination("",false,soloDestination,latitude,longitude);
                    }
                }
            }
        } catch (Exception e) {
            setNewDestination("",true,soloDestination,null,null);
        }
    }


    private void setNewDestination(String description, Boolean blackList,String destinationName,Double latitude,Double longitude){
        //na czas importu - wyłączyć ograniczenie walidacji ilości znaków w nazwie. Bo czarna lista musi być.
        if(destinationName.length()>=3) {
            Destination destination = new Destination();
            destination.setBlackList(blackList);
            destination.setName(destinationName);
            destination.setDescription(description);
            destination.setLatitude(latitude);
            destination.setLongitude(longitude);
            destinationRepository.save(destination);
        }
    }


}