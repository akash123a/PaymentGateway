//package PaymentGateway.PaymentGateway.service;
//
//import PaymentGateway.PaymentGateway.DTO.OrgRequestDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.RequestEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//
//@Service
//public class APIService {
//
//    @Autowired
//     RestTemplate restTemplate;
//
//
//
//
//    public  void  SendData(OrgRequestDTO orgRequestDTO){
//
//        String url = "http://localhost:8094/api/organisations/data";
//        RequestEntity dataset =  RequestEntity.post(url).body(orgRequestDTO);
//        restTemplate.exchange(url, HttpMethod.POST ,dataset , String.class);
//        }
//
//
//
//
//}



package PaymentGateway.PaymentGateway.service;

import PaymentGateway.PaymentGateway.DTO.OrgRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String ORGANISATION_URL = "http://localhost:8094/api/organisations/data";

    public void  sendData(OrgRequestDTO orgRequestDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OrgRequestDTO> request = new HttpEntity<>(orgRequestDTO, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    ORGANISATION_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            //return response; // Return API response
        } catch (Exception e) {
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              //      .body("Failed to send data: " + e.getMessage());
            System.out.println(e);
        }
    }

    public void SendData(OrgRequestDTO orgRequestDTO) {

    }
}

