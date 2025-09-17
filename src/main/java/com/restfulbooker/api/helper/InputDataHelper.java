package com.restfulbooker.api.helper;

import java.sql.Date;
import java.util.Properties;

import static org.testng.Assert.fail;

public class InputDataHelper {

    private static final String DATA_PROPS = "data/data.properties";

    private final Properties props;

    public InputDataHelper() {
        props = new PropertiesUtils().loadProps(DATA_PROPS);
    }

    private String get(String key) {
        String v = props.getProperty(key);
        if (v != null)
            return v;
        fail(String.format("Could not find property '%s' in %s", key, DATA_PROPS));
        return null;
    }

    public String bookingFirstname() {
        return get("booking.firstname");
    }
    public String bookingLastname() {
        return get("booking.lastname");
    }
    public int bookingTotalprice() {
        return Integer.parseInt(get("booking.totalprice"));
    }
    public boolean bookingDepositpaid() {
        return Boolean.parseBoolean(get("booking.depositpaid"));
    }
    public String bookingAdditionalneeds() {
        return get("booking.additionalneeds");
    }
    public Date bookingCheckin() {
        return Date.valueOf(get("booking.checkin"));
    }
    public Date bookingCheckout() {
        return Date.valueOf(get("booking.checkout"));
    }

    public String bookingUpdFirstname() {
        return get("booking.update.firstname");
    }
    public String bookingUpdLastname() {
        return get("booking.update.lastname");
    }
    public int    bookingUpdTotalprice() {
        return Integer.parseInt(get("booking.update.totalprice"));
    }
    public boolean bookingUpdDepositpaid() {
        return Boolean.parseBoolean(get("booking.update.depositpaid"));
    }
    public Date   bookingUpdCheckin()
    {
        return Date.valueOf(get("booking.update.checkin"));
    }
    public Date   bookingUpdCheckout() {
        return Date.valueOf(get("booking.update.checkout"));
    }
    public String bookingUpdAdditionalneeds() {
        return get("booking.update.additionalneeds");
    }
    public String bookingFirstnamePatch() {
        return get("booking.firstname.patch");
    }
    public String bookingLastnamePatch()  {

        return get("booking.lastname.patch");
    }
    public String invalidPassword() {
        return get("invalid.password"); // вторият арг. е default, по желание
    }
}