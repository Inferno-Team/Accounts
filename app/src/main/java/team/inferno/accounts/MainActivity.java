package team.inferno.accounts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import connection.API;
import connection.AddResponse;
import connection.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput, emailInput;
    private ListView listView;
    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        emailInput = findViewById(R.id.email_input);
        listView = findViewById(R.id.listView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
        Call<List<User>>listCall=api.getAllUsers();
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User>users=response.body();
                UserAdapter adapter=new UserAdapter(new ArrayList<>(users),
                        MainActivity.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addUser(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String email = emailInput.getText().toString();

        Call<AddResponse> responseCall = api.addUser(username, email, password);
        responseCall.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                AddResponse addResponse = response.body();
                Toast.makeText(MainActivity.this, addResponse.getResult(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class UserAdapter extends BaseAdapter {
        private ArrayList<User> users;
        private Context context;

        public UserAdapter(ArrayList<User> users, Context context) {
            this.users = users;
            this.context = context;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view = LayoutInflater.from(context).inflate(R.layout.user_item_view,
                    null, false);
            TextView username = view.findViewById(R.id.item_username);
            TextView password = view.findViewById(R.id.item_password);
            TextView email = view.findViewById(R.id.item_email);
            username.setText(users.get(position).getUsername());
            password.setText(users.get(position).getPassword());
            email.setText(users.get(position).getEmail());
            return view;
        }
    }
}
