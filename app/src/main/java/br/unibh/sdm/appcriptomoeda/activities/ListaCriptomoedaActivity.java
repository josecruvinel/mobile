package br.unibh.sdm.appcriptomoeda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.unibh.sdm.appcriptomoeda.R;
import br.unibh.sdm.appcriptomoeda.api.CriptomoedaService;
import br.unibh.sdm.appcriptomoeda.api.RestServiceGenerator;
import br.unibh.sdm.appcriptomoeda.entidades.Criptomoeda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaCriptomoedaActivity extends AppCompatActivity {

    private CriptomoedaService service = null;
    final private ListaCriptomoedaActivity mainActivity = this;
    private final Context context;

    public ListaCriptomoedaActivity() {
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Lista de Criptomoedas");
        setContentView(R.layout.activity_lista_criptomoeda);
        service = RestServiceGenerator.createService(CriptomoedaService.class);
        buscaCriptomoedas();
        criaAcaoBotaoFlutuante();
        criaAcaoCliqueLongo();
    }

    private void criaAcaoCliqueLongo() {
        ListView listView = findViewById(R.id.listViewListaCriptomoedas);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListaCriptoActivity","Clicou em clique longo na posicao "+position);
                final Criptomoeda objetoSelecionado = (Criptomoeda) parent.getAdapter().getItem(position);
                Log.i("ListaCriptoActivity", "Selecionou a criptomoeda "+objetoSelecionado.getCodigo());
                new AlertDialog.Builder(parent.getContext()).setTitle("Removendo Criptomoeda")
                        .setMessage("Tem certeza que quer remover a criptomoeda "+objetoSelecionado.getCodigo()+"?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeCriptomoeda(objetoSelecionado);
                            }
                        }).setNegativeButton("Não", null).show();
                return true;
            }
        });
    }

    private void removeCriptomoeda(Criptomoeda criptomoeda) {
        Log.i("ListaCriptoActivity","Vai remover criptomoeda "+criptomoeda.getCodigo());
        Call<Boolean> call = this.service.excluiCriptomoeda(criptomoeda.getCodigo());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.i("ListaCriptoActivity", "Removeu a Criptomoeda " + criptomoeda.getCodigo());
                    Toast.makeText(getApplicationContext(), "Removeu a Criptomoeda " + criptomoeda.getCodigo(), Toast.LENGTH_LONG).show();
                    onResume();
                } else {
                    Log.e("ListaCriptoActivity", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ListaCriptoActivity", "Erro: " + t.getMessage());
            }
        });
    }

    private void criaAcaoBotaoFlutuante() {
        FloatingActionButton botaoNovo = findViewById(R.id.floatingActionButtonCriar);
        botaoNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity","Clicou no botão para adicionar Nova Criptomoeda");
                startActivity(new Intent(ListaCriptomoedaActivity.this,
                        FormularioCriptomoedaActivity.class));
            }
        });
    }

    public void buscaCriptomoedas(){
        CriptomoedaService service = RestServiceGenerator.createService(CriptomoedaService.class);
        Call<List<Criptomoeda>> call = service.getCriptomoedas();
        call.enqueue(new Callback<List<Criptomoeda>>() {
            @Override
            public void onResponse(Call<List<Criptomoeda>> call, Response<List<Criptomoeda>> response) {
                if (response.isSuccessful()) {
                    Log.i("ListaCriptoActivity", "Retornou " + response.body().size() + " Criptomoedas!");
                    ListView listView = findViewById(R.id.listViewListaCriptomoedas);
                    listView.setAdapter(new ListaCriptomoedaAdapter(context,response.body()));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("ListaCriptoActivity", "Selecionou o objeto de posicao "+position);
                            Criptomoeda objetoSelecionado = (Criptomoeda) parent.getAdapter().getItem(position);
                            Log.i("ListaCriptoActivity", "Selecionou a criptomoeda "+objetoSelecionado.getCodigo());
                            Intent intent = new Intent(ListaCriptomoedaActivity.this, FormularioCriptomoedaActivity.class);
                            intent.putExtra("objeto", objetoSelecionado);
                            startActivity(intent);
                        }
                    });
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

    @Override
    protected void onResume() {
        super.onResume();
        buscaCriptomoedas();
    }
}