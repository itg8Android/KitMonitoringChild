package itg8.com.kitmonitoringchild.CommonMethod;

import android.view.View;

/**
 * Created by Android itg 8 on 2/9/2017.
 */

public class CommonMethod {

    public static final String baseurl = "";
    public static String SHARED="KidMonitoringChild";
    public static String SHAREDCHILD="SHARED LOGIN CHILD";
    public static String getTag(Class<?> aClass) {
        return aClass.getSimpleName();
    }




    public static void toggleView(View hideView, View showView) {

        hideView.setVisibility(View.GONE);
        showView.setVisibility(View.VISIBLE);

    }

    //    public  static Retrofit getController() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(CommonMethod.baseurl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        return retrofit;
//    }

}
