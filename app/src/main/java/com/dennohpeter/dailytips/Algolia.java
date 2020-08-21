package com.dennohpeter.dailytips;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;

public class Algolia {
    private Client client;
    public Algolia() {
       client = new Client("YourApplicationID", "YourAPIKey");

    }

    public Index getIndex(String index) {
        return client.getIndex(index);
    }
}
