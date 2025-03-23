package PaymentGateway.PaymentGateway.service;

import PaymentGateway.PaymentGateway.DTO.OrgRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class APIService {

    @Autowired
     RestTemplate restTemplate;




    public  void  SendData(OrgRequestDTO orgRequestDTO){

        String url = "http://localhost:8093/api/organisations/data";
        RequestEntity dataset =  RequestEntity.post(url).body(orgRequestDTO);
        restTemplate.exchange(url, HttpMethod.POST ,dataset , String.class);
        }

}
