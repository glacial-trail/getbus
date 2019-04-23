package info.getbus.servebus.store;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Getter @Setter
@Entity
@Table(name = "carrier_store_payment_details_bank_ua")
public class StorePaymentDetailsBankUA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone; //move to Liqpay payment details
    private String account;
    private String mfo;
    private String okpo;

    @OneToOne
    @JoinColumn( name = "carrier_store_id")
    private Store store;
}
