package br.unibh.sdm.appcriptomoeda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import br.unibh.sdm.appcriptomoeda.R;
import br.unibh.sdm.appcriptomoeda.api.CriptomoedaService;
import br.unibh.sdm.appcriptomoeda.api.RestServiceGenerator;
import br.unibh.sdm.appcriptomoeda.entidades.Criptomoeda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioCriptomoedaActivity extends AppCompatActivity {

    private CriptomoedaService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomulario_criptomoeda);
        setTitle("Edição de Criptomoeda");
        service = RestServiceGenerator.createService(CriptomoedaService.class);
        configuraBotaoSalvar();
        inicializaObjeto();
    }

    private void inicializaObjeto() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("objeto") != null) {
            Criptomoeda objeto = (Criptomoeda) intent.getSerializableExtra("objeto");
            EditText codigo = findViewById(R.id.editTextCodigo);
            EditText nome = findViewById(R.id.editTextNome);
            EditText descricao = findViewById(R.id.editTextDescricao);
            codigo.setText(objeto.getCodigo());
            nome.setText(objeto.getNome());
            descricao.setText(objeto.getDescricao());
            codigo.setEnabled(false);
            Button botaoSalvar = findViewById(R.id.buttonSalvar);
            botaoSalvar.setText("Atualizar");
        }
    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(R.id.buttonSalvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FormularioCripto","Clicou em Salvar");
                Criptomoeda criptomoeda = recuperaInformacoesFormulario();
                Intent intent = getIntent();
                if (intent.getSerializableExtra("objeto") != null) {
                    Criptomoeda objeto = (Criptomoeda) intent.getSerializableExtra("objeto");
                    criptomoeda.setCodigo(objeto.getCodigo());
                    criptomoeda.setDataCriacao(objeto.getDataCriacao());
                    if (validaFormulario(criptomoeda)) {
                        atualizaCriptomoeda(criptomoeda);
                    }
                } else {
                    criptomoeda.setDataCriacao(new Date());
                    if (validaFormulario(criptomoeda)) {
                        salvaCriptomoeda(criptomoeda);
                    }
                }
            }
        });
    }

    private boolean validaFormulario(Criptomoeda criptomoeda){
        boolean valido = true;
        EditText codigo = findViewById(R.id.editTextCodigo);
        EditText nome = findViewById(R.id.editTextNome);
        EditText descricao = findViewById(R.id.editTextDescricao);
        if (criptomoeda.getCodigo() == null || criptomoeda.getCodigo().trim().length() == 0){
            codigo.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            codigo.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (criptomoeda.getNome() == null || criptomoeda.getNome().trim().length() == 0){
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            nome.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (criptomoeda.getDescricao() == null || criptomoeda.getDescricao().trim().length() == 0){
            descricao.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            valido = false;
        } else {
            descricao.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_ATOP);
        }
        if (!valido){
            Log.e("FormularioCripto", "Favor verificar os campos destacados");
            Toast.makeText(getApplicationContext(), "Favor verificar os campos destacados", Toast.LENGTH_LONG).show();
        }
        return valido;
    }

    private void salvaCriptomoeda(Criptomoeda criptomoeda) {
        Call<Criptomoeda> call = service.criaCriptomoeda(criptomoeda);
        call.enqueue(new Callback<Criptomoeda>() {
            @Override
            public void onResponse(Call<Criptomoeda> call, Response<Criptomoeda> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioCripto", "Salvou a Criptomoeda "+ criptomoeda.getCodigo());
                    Toast.makeText(getApplicationContext(), "Salvou a Criptomoeda "+ criptomoeda.getCodigo(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioCripto", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Criptomoeda> call, Throwable t) {
                Log.e("FormularioCripto", "Erro: " + t.getMessage());
            }
        });
    }

    private void atualizaCriptomoeda(Criptomoeda criptomoeda) {
        Log.i("FormularioCripto","Vai atualizar criptomoeda "+criptomoeda.getCodigo());
        Call<Criptomoeda> call = service.atualizaCriptomoeda(criptomoeda.getCodigo(), criptomoeda);
        call.enqueue(new Callback<Criptomoeda>() {
            @Override
            public void onResponse(Call<Criptomoeda> call, Response<Criptomoeda> response) {
                if (response.isSuccessful()) {
                    Log.i("FormularioCripto", "Atualizou a Criptomoeda " + criptomoeda.getCodigo());
                    Toast.makeText(getApplicationContext(), "Atualizou a Criptomoeda " + criptomoeda.getCodigo(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("FormularioCripto", "Erro (" + response.code()+"): Verifique novamente os valores");
                    Toast.makeText(getApplicationContext(), "Erro (" + response.code()+"): Verifique novamente os valores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Criptomoeda> call, Throwable t) {
                Log.e("FormularioCripto", "Erro: " + t.getMessage());
            }
        });
    }

    @NotNull
    private Criptomoeda recuperaInformacoesFormulario() {
        EditText codigo = findViewById(R.id.editTextCodigo);
        EditText nome = findViewById(R.id.editTextNome);
        EditText descricao = findViewById(R.id.editTextDescricao);
        Criptomoeda criptomoeda = new Criptomoeda();
        criptomoeda.setCodigo(codigo.getText().toString());
        criptomoeda.setNome(nome.getText().toString());
        criptomoeda.setDescricao(descricao.getText().toString());
        criptomoeda.setDataCriacao(new Date());
        return criptomoeda;
    }

}