package es.upm.dit.apsv.transportationorderserver;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import es.upm.dit.apsv.transportationorderserver.repository.TransportationOrderRepository;
import es.upm.dit.apsv.transportationorderserver.controller.TransportationOrderController;
import es.upm.dit.apsv.transportationorderserver.model.TransportationOrder;

@WebMvcTest(TransportationOrderController.class)
public class TransportationOrderControllerTest {

    @InjectMocks
    private TransportationOrderController controller;

    @MockBean
    private TransportationOrderRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOrders() throws Exception {
        when(repository.findAll()).thenReturn(getAllTestOrders());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/transportationorders")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)));
    }

@Test
public void testGetOrder() throws Exception {
    when(repository.findById("8962ZKR")).thenReturn(Optional.of(
            new TransportationOrder("8962ZKR", "28", 1591682400000L,
                    40.4562191, -3.8707211, 1591692196000L, 42.0206372, -4.5330132,
                    0, 0.0, 0.0, 0)));

    // Enviar petición GET con el ID del camión "8962ZKR"
    RequestBuilder request = MockMvcRequestBuilders
            .get("/transportationorders/8962ZKR")
            .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.truck", is("8962ZKR")));

    when(repository.findById("9999XXX")).thenReturn(Optional.empty());

    RequestBuilder invalidRequest = MockMvcRequestBuilders
        .get("/transportationorders/9999XXX")
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(invalidRequest)
        .andExpect(status().isNotFound());

}


    private List<TransportationOrder> getAllTestOrders() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TransportationOrder> orders = new ArrayList<>();
        TransportationOrder order = null;

        try (BufferedReader br = new BufferedReader(new FileReader(
                new ClassPathResource("orders.json").getFile()))) {
            for (String line; (line = br.readLine()) != null;) {
                order = objectMapper.readValue(line, TransportationOrder.class);
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orders;
    }
}