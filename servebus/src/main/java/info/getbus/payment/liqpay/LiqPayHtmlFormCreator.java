package info.getbus.payment.liqpay;


import com.liqpay.LiqPay;

import java.util.HashMap;


/**
 * Used to generate the HTML form code.
 */
public class LiqPayHtmlFormCreator {

    private HashMap <String, String> params;

    private String publicKey;

    private String privateKey;

    /**
     *
     * @param params
     *  Map params = new HashMap();
     *  params.put("amount", "1.50");
     *  params.put("currency", "USD");
     *  params.put("description", "description text");
     *  params.put("order_id", "order_id_1");
     *  params.put("sandbox", "1");
     *  ...
     * @param publicKey
     * Merchants Public key
     * @param privateKey
     * Merchants Private key
     */
    public LiqPayHtmlFormCreator(HashMap<String, String> params, String publicKey, String privateKey){

        this.params = params;

        this.publicKey = publicKey;

        this.privateKey = privateKey;
    }

    /**
     * @return
     * HTML form code
     */
    public String getLiqPayHtmlForm() {

        LiqPay liqpay = new LiqPay(publicKey, privateKey);

        return (liqpay.cnb_form(params));

    }

}
