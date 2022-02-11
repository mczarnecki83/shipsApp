package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.ship.Destination;
import com.hobbyjoin.ships.model.ship.DestinationRepository;
import com.hobbyjoin.ships.model.user.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DestinationApiController.class)
@WithMockUser
public class DestinationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DestinationRepository destinationRepository;

    @MockBean
    private DestinationService destinationService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    Destination mockPort = new Destination("PORT_GDYNIA", 55.4,66.3);

    @Test
    public void retrieveDetailsForDestination() throws Exception {
        Mockito.when(destinationService.getDestinationFromId(Mockito.anyInt())).thenReturn(java.util.Optional.ofNullable(mockPort));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/destination/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":null,\"name\":\"PORT_GDYNIA\",\"latitude\":55.4,\"longitude\":66.3,\"description\":null,\"info\":null,\"blackList\":false,\"userId\":null}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }


    @Test
    public void addDestination() throws Exception {
        String contentJson="{\n" +
                "    \"name\": \"TEST_ADD222\",\n" +
                "    \"latitude\": 63.55,\n" +
                "    \"longitude\": 10.21667,\n" +
                "    \"description\": null,\n" +
                "    \"info\": null,\n" +
                "    \"blackList\": false\n" +
                "}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/destination/add")
                .content(contentJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getStatus());
        final int status = result.getResponse().getStatus();
        JSONAssert.assertEquals("201",String.valueOf(status),false);
    }



    @Test
    public void addDestinationNameNull() throws Exception {
        String contentJson="{\n" +
                "    \"name\": \"\",\n" +
                "    \"latitude\": 63.55,\n" +
                "    \"longitude\": 10.21667,\n" +
                "    \"description\": null,\n" +
                "    \"info\": null,\n" +
                "    \"blackList\": false\n" +
                "}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/destination/add")
                .content(contentJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getStatus());
        final int status = result.getResponse().getStatus();
        JSONAssert.assertEquals("400",String.valueOf(status),false);
    }
}
