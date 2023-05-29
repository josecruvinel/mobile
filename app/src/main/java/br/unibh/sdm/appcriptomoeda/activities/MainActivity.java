package br.unibh.sdm.appcriptomoeda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unibh.sdm.appcriptomoeda.R;
import br.unibh.sdm.appcriptomoeda.api.CriptomoedaService;
import br.unibh.sdm.appcriptomoeda.api.RestServiceGenerator;
import br.unibh.sdm.appcriptomoeda.entidades.Criptomoeda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CriptomoedaService service = null;
    final private MainActivity mainActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = RestServiceGenerator.createService(CriptomoedaService.class);
        buscaCriptomoedas();
    }

    public void buscaCriptomoedas(){
        CriptomoedaService service = RestServiceGenerator.createService(CriptomoedaService.class);
        Call<List<Criptomoeda>> call = service.getCriptomoedas();
        call.enqueue(new Callback<List<Criptomoeda>>() {
            @Override
            public void onResponse(Call<List<Criptomoeda>> call, Response<List<Criptomoeda>> response) {
                if (response.isSuccessful()) {
                    Log.i("CriptomoedaDAO", "Retornou " + response.body().size() + " Criptomoedas!");
                    List<String> lista2 = new ArrayList<String>();
                    for (Criptomoeda item : response.body()) {
                        lista2.add(item.getNome());
                    }
                    Log.i("MainActivity", lista2.toArray().toString());
                    ListView listView = findViewById(R.id.listViewListaCriptomoeda);
                    listView.setAdapter(new ArrayAdapter<String>(mainActivity,
                            android.R.layout.simple_list_item_1,
                            lista2));
                } else {
                    Log.e("CriptomoedaDAO", "" + response.message());
                    Toast.makeText(getApplicationContext(), "Erro: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Criptomoeda>> call, Throwable t) {
                Log.e("Error", "" + t.getMessage());
            }
        });
    }

}