package info.getbus.servebus.web.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StoreDetailsDTO {
    private Long id;
    private String domain = "";
    private String dummyp;
    private String paymentDetailsPhone = "";
    private String paymentDetailsAccount = "";
    private String paymentDetailsMfo = "";
    private String paymentDetailsOkpo = "";
}
