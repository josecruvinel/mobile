package br.unibh.sdm.appcriptomoeda.api;

import java.util.List;

import br.unibh.sdm.appcriptomoeda.entidades.Criptomoeda;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CriptomoedaService {

    @Headers({
            "Accept: application/json",
            "User-Agent: AppCriptomoeda"
    })
    @GET("criptomoeda")
    Call<List<Criptomoeda>> getCriptomoedas();

    @GET("criptomoeda/{id}")
    Call<Criptomoeda> getCriptomoeda(@Path("id") String codigo);

    @POST("criptomoeda")
    Call<Criptomoeda> criaCriptomoeda(@Body Criptomoeda criptomoeda);

    @PUT("criptomoeda/{id}")
    Call<Criptomoeda> atualizaCriptomoeda(@Path("id") String codigo, @Body Criptomoeda criptomoeda);

    @DELETE("criptomoeda/{id}")
    Call<Boolean> excluiCriptomoeda(@Path("id") String codigo);

}