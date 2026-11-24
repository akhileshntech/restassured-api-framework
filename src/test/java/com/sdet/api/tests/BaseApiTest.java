package com.sdet.api.tests;

import com.sdet.api.client.AuthClient;
import com.sdet.api.client.BookingClient;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    protected AuthClient authClient;
    protected BookingClient bookingClient;
    protected String token;

    @BeforeClass(alwaysRun = true)
    public void initClients() {
        authClient = new AuthClient();
        bookingClient = new BookingClient();
        token = authClient.getToken();
    }
}
