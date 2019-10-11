package br.com.bossini.pessoal_consome_ws_rest_json_livros;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private RecyclerView livrosRecyclerView;
    private LivroAdapter adapter;
    private List <Livro> livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        livrosRecyclerView = findViewById(R.id.livrosRecyclerView);
        livros = new ArrayList<>();
        adapter = new LivroAdapter(this, livros);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        livrosRecyclerView.setAdapter(adapter);
        livrosRecyclerView.setLayoutManager(llm);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = montaUrl(
                        getString(R.string.host_address),
                        getString(R.string.host_port),
                        getString(R.string.endpoint_base),
                        getString(R.string.endpoint_listar)
                );
               requestQueue.add(new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                   @Override
                   public void onResponse(JSONArray response) {
                       //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                       for (int i = 0; i < response.length(); i++){
                           try {
                               JSONObject iesimo = response.getJSONObject(i);
                               String titulo = iesimo.getString("titulo");
                               String autor = iesimo.getString("autor");
                               String edicao = iesimo.getString("edicao");
                               livros.add(new Livro (titulo ,autor, edicao));
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }
                       adapter.notifyDataSetChanged();
                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();
                   }
               }));
            }
        });

        requestQueue = Volley.newRequestQueue(this);
    }

    public String montaUrl (String... args){
        StringBuilder sb = new StringBuilder();
        for (String s : args){
            sb.append(s);
        }
        return sb.toString();
    }

}

class LivroViewHolder extends RecyclerView.ViewHolder{
    public TextView tituloTextView;
    public TextView autorTextView;
    public TextView edicaoTextView;

    public LivroViewHolder (View raiz){
        super (raiz);
        this.tituloTextView = raiz.findViewById(R.id.tituloTextView);
        this.autorTextView = raiz.findViewById(R.id.autorTextView);
        this.edicaoTextView = raiz.findViewById(R.id.edicaoTextView);
    }
}

class LivroAdapter extends RecyclerView.Adapter <LivroViewHolder>{

    private Context context;
    private List <Livro> livros;

    public LivroAdapter(Context context, List<Livro> livros) {
        this.context = context;
        this.livros = livros;
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View raiz = inflater.inflate(
                R.layout.list_item,
                parent,
                false
        );
        return new LivroViewHolder(raiz);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        Livro livro = this.livros.get(position);
        holder.autorTextView.setText(livro.getAutor());
        holder.edicaoTextView.setText(livro.getEdicao());
        holder.tituloTextView.setText(livro.getTitulo());
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }
}
