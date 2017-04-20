package itg8.com.kitmonitoringchild.CommonMethod;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Android itg 8 on 2/9/2017.
 */

public interface RetroController {
@GET("/")
  Call<me.everything.providers.android.calllog.Call> sendCallToServer();

}
