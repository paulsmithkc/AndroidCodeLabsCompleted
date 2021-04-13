package edu.ranken.prsmith.echodemo.model;

import edu.ranken.prsmith.echodemo.network.EchoResponse;
import edu.ranken.prsmith.echodemo.network.EchoService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EchoDataSource {
    private Retrofit retrofit;
    private EchoService echoService;

    public EchoDataSource() {
        retrofit = new Retrofit.Builder()
            .baseUrl("https://awd-example-services.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        echoService = retrofit.create(EchoService.class);
    }

    public Call<EchoResponse> getEcho(String message) {
        return echoService.getEcho("GET", message);
    }

    public Call<EchoResponse> postEcho(String message) {
        return echoService.postEcho("POST", message);
    }
}
