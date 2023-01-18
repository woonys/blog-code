package hellojpa.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class Delivery {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Orders orders;

    private String city;

    private String street;

    private String zipcode;

    private DeliveryStatus status;

}
