package connection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    String BASE_URL = "http://192.168.1.6/db_test/";

    @GET("get_all_users.php")
    Call<List<User>> getAllUsers();

    @POST("add_user.php")
    @FormUrlEncoded
    Call<AddResponse> addUser(@Field(encoded = true, value = "username")
                                      String username, @Field(encoded = true,
                                value = "email") String email,
                              @Field(encoded = true, value = "password") String password);
}
