package PaymentGateway.PaymentGateway.DTO;

public class OrgRequestDTO {


    String name ;
    String email;

    public  OrgRequestDTO(){

    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OrgRequestDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
