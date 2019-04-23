package info.getbus.servebus.store;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Getter @Setter
@Entity
@Table(name = "carrier_store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "carrier_id")
    private Long carrierId;
    private String domain;
    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL)
    private StorePaymentDetailsBankUA paymentDetails;


    public void setPaymentDetails(StorePaymentDetailsBankUA paymentDetails) {
        paymentDetails.setStore(this); //modelmapper fix
        this.paymentDetails = paymentDetails;

    }
}
