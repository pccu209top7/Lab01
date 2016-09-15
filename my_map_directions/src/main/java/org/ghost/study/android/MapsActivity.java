package org.ghost.study.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnClickListener {

    /*
        請求碼
     */
    //權限
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 0x0001;
    //Activity
    public static final int REQUEST_CODE_CHOICE_GOAL = 0x0001;
    public static final String RESULT_KEY_CHOICE_GOAL = MapsActivity.class.getName() + "_RESULT_KEY_CHOICE_GOAL";

    /*
        地圖
     */
    private GoogleMap map;

    /*
        設定用的常數
     */
    //目標分析個數
    private final int targetAnalysisCount = 1;

    /*
        非同步等待停止線
     */
    private boolean isMapReady = false;
    private boolean isOnResume = false;
    private boolean isPermissionAvailable = false;

    /*
        View
     */
    private Button choiceGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViews();
    }

    private void findViews() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        choiceGoal = (Button) findViewById(R.id.choiceGoal);
        choiceGoal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.choiceGoal:
                Intent intent = new Intent(this, ChoiceGoalActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOICE_GOAL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHOICE_GOAL:
                String goal = data.getStringExtra(RESULT_KEY_CHOICE_GOAL);
                startOptimizePath(goal);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //危險權限
        int permission = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_DENIED == permission) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_LOCATION);
        } else {
            isPermissionAvailable = true;
            googleMap.setMyLocationEnabled(true);
        }

        //檢查停止線
        isMapReady = true;
        if (isOnResume && isPermissionAvailable) {
            choiceGoal.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_CODE_PERMISSION_LOCATION:
                if (permissions[0].equals(ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //noinspection MissingPermission
                    map.setMyLocationEnabled(true);

                    //檢查停止線
                    isPermissionAvailable = true;
                    if (isMapReady && isOnResume) {
                        choiceGoal.setEnabled(true);
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("權限狀態提示")
                            .setMessage("取得精確定位權限未完成!!")
                            .setNeutralButton("確定", null)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //檢查停止線
        isOnResume = true;
        if (isMapReady && isPermissionAvailable) {
            choiceGoal.setEnabled(true);
        }
    }

    /*
        這兩種精確度設定都回傳 "gps" 故棄用
        直接設定為 LocationManager.GPS_PROVIDER 及 LocationManager.NETWORK_PROVIDER
     */
//        Criteria criteriaFine = new Criteria();
//        criteriaFine.setAccuracy(Criteria.ACCURACY_FINE);
//        String providerFineName = locationManager.getBestProvider(criteriaFine, true);
//        Log.d("providerFineName - ", providerFineName);

//        Criteria criteriaCoarse = new Criteria();
//        criteriaCoarse.setAccuracy(Criteria.ACCURACY_COARSE);
//        String providerCoarseName = locationManager.getBestProvider(criteriaCoarse, true);
//        Log.d("providerCoarseName - ",providerCoarseName);

    /**
     * 開始進行路徑分析
     */
    private void startOptimizePath(String goal) {

        /*
            定位目標地點
         */
        double goalLat = 0;
        double goalLng = 0;

        Geocoder geocoder = new Geocoder(this, Locale.TAIWAN);
        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocationName(goal, targetAnalysisCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() == targetAnalysisCount) {

            Address address = addressList.remove(addressList.size() - 1);
            goalLat = address.getLatitude();
            goalLng = address.getLongitude();

        } else {
            Toast.makeText(this, "取得目標地失敗", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
            定位自己
         */
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        double myLat = 0;
        double myLng = 0;

        //noinspection MissingPermission
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS == null) {
            Toast.makeText(this, "使用GPS未取得位置", Toast.LENGTH_SHORT).show();

            //noinspection MissingPermission
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationNetwork == null) {
                Toast.makeText(this, "使用wifi未取得位置", Toast.LENGTH_SHORT).show();

                Toast.makeText(this, "兩種方式皆未取得位置,定位結束!!", Toast.LENGTH_SHORT).show();
                return;

            } else {
                myLat = locationNetwork.getLatitude();
                myLng = locationNetwork.getLongitude();
                Toast.makeText(this, "使用Wifi有取得位置 myLat - " + myLat + " myLng - " + myLng, Toast.LENGTH_SHORT).show();
            }

        } else {
            myLat = locationGPS.getLatitude();
            myLng = locationGPS.getLongitude();
            Toast.makeText(this, "使用GPS有取得位置 myLat - " + myLat + " myLng - " + myLng, Toast.LENGTH_SHORT).show();
        }

        /*
            清空地圖 準備繪圖
         */
        map.clear();

        LatLng myPosi = new LatLng(myLat, myLng);
        LatLng goalPosi = new LatLng(goalLat, goalLng);

        /*
            在起點與終點放上marker
         */
        //目標地點 marker
        BitmapDescriptor goalBitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        MarkerOptions goalMarkerOptions = new MarkerOptions();
        goalMarkerOptions.position(goalPosi).title("您的目標").snippet(goal).icon(goalBitmapDescriptor);
        map.addMarker(goalMarkerOptions);

        //起點 我的位置
        BitmapDescriptor myBitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);

        MarkerOptions myMarkerOptions = new MarkerOptions();
        myMarkerOptions.position(myPosi).title("現在位置").snippet("從這裡出發").icon(myBitmapDescriptor);
        map.addMarker(myMarkerOptions);

        //視角移動到起點
        CameraUpdate myPosiCameraUpdate = CameraUpdateFactory.newLatLngZoom(myPosi, 18);
        map.animateCamera(myPosiCameraUpdate);

        /*
            組合url 下載路徑資料 分析資料 繪製路線圖
         */
        String dirUrl = createDirUrl(myPosi, goalPosi);
        DownloadPathTask dlTask = new DownloadPathTask();
        dlTask.execute(dirUrl);
    }

    private static String createDirUrl(LatLng myPosi, LatLng goalPosi) {

        final String originPara = "origin=";
        final String destPara = "destination=";
        final String sensorPara = "sensor=false";
        final String outputFormat = "json";
        final String hostAndServiceName = "https://maps.googleapis.com/maps/api/directions/";

        String url = hostAndServiceName + outputFormat + "?"
                + originPara + myPosi.latitude + "," + myPosi.longitude
                + "&"
                + destPara + goalPosi.latitude + "," + goalPosi.longitude
                + "&"
                + sensorPara;
        return url;
    }

    /**
     * 下載路徑資料並解析
     */
    class DownloadPathTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urlArray) {

            StringBuffer jsonString = new StringBuffer();

            try {
                URL url = new URL(urlArray[0]);
                InputStream is = url.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String read;
                while ((read = br.readLine()) != null) {
                    jsonString.append(read);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonString.toString();
        }

        private static final String JSON_ARRAY_NAME_ROUTES = "routes";
        private static final String JSON_ARRAY_NAME_LEGS = "legs";
        private static final String JSON_ARRAY_NAME_STEPS = "steps";
        private static final String JSON_OBJECT_NAME_POLYLINE = "polyline";
        private static final String JSON_OBJECT_NAME_POINTS = "points";

        @Override
        protected void onPostExecute(String jsonString) {

            try {
                //root
                JSONObject rootJsonObj = new JSONObject(jsonString);

                JSONArray routesJsonArr = rootJsonObj.getJSONArray(JSON_ARRAY_NAME_ROUTES);
                for (int routesIdx = 0; routesIdx < routesJsonArr.length(); routesIdx++) {
                    Object routesObj = routesJsonArr.get(routesIdx);
                    JSONObject routesJsonObj = (JSONObject) routesObj;

                    JSONArray legsJsonArr = routesJsonObj.getJSONArray(JSON_ARRAY_NAME_LEGS);
                    for (int legsIdx = 0; legsIdx < legsJsonArr.length(); legsIdx++) {
                        Object legsObj = legsJsonArr.get(legsIdx);
                        JSONObject legsJsonObj = (JSONObject) legsObj;

                        JSONArray stepsJsonArr = legsJsonObj.getJSONArray(JSON_ARRAY_NAME_STEPS);
                        for (int stepsIdx = 0; stepsIdx < stepsJsonArr.length(); stepsIdx++) {
                            Object stepsObj = stepsJsonArr.get(stepsIdx);
                            JSONObject stepsJsonObj = (JSONObject) stepsObj;

                            Object polylineObj = stepsJsonObj.get(JSON_OBJECT_NAME_POLYLINE);
                            JSONObject polylineJsonObj = (JSONObject) polylineObj;
                            String points = (String) polylineJsonObj.get(JSON_OBJECT_NAME_POINTS);

                            //test-
                            Log.d("points - - - ", points);

                            List<LatLng> latLngList = decodePoly(points);

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.addAll(latLngList);
                            polylineOptions.width(8F);
                            polylineOptions.color(Color.BLUE);

                            map.addPolyline(polylineOptions);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*
         * 官方網站說明
         */
        // https://developers.google.com/maps/documentation/utilities/polylinealgorithm
        // 正向操作
        //
        // 1.起點
        // 2.乘以10萬(1E5)
        // 3.轉二進位 若是負數 要取2的補數[2的補數 - 就是反轉位元再加1]
        // 4.左移一位(全部乘以2)
        // 5.如果起點是負數 反轉位元(1變0 0變1)
        // 6.五個位元一組(從右手邊算起)
        // 7.以五個位元為單位 反轉順序
        // 8.除了最右邊的一組(五個位元)其餘全部加32(每組最左邊加一個1 變成六碼)
        // 9.轉十進位數字
        // 10.每個數字加63
        // 11.轉ascii字元
        //
        // 反向操作
        //
        // 11.ascii本來就可以當整數計算 無動作
        // 10.每個字元減63
        // 9.不用轉二進位也可以計算 無動作
        // 8.這裡判斷 比32小的 就是結尾 但不論是不是結尾 都要用遮罩保留每組右邊五位
        // 7.字元被迴圈從左繞行到右 所以最左邊的一組放在最低位元 左邊第二組放在次低位元 以此類推
        // 6.分組無計算動作 無動作
        //
        // 5-4.這裡要第五步到第四步要一起看
        // 因為第四步有向左偏移 所以最小位必為0
        // 但第五步 若是負數 要反轉位元 則最小位必為1
        // 所以 到了第六步 若是最小位為1 原本必為負數
        // 所要採取的是 判斷是否為負數
        // 若是 反轉位元
        // 若不是則無動作
        // 但不論如何 要向右移一個位元 還原第四步
        //
        // 3.不轉回十進位 也可以計算 不逆轉2的補數 可以將負數資訊包含在二進位碼中 故無動作
        // 2.除以10萬(1E5)
        // 1.回到起點

        /**
         * 解析為緯經度清單
         *
         * @param points
         * @return
         */
        private List<LatLng> decodePoly(String points) {

            List<LatLng> list = new ArrayList<LatLng>();

            int lat = 0;
            int lng = 0;

            int index = 0;
            while (index < points.length()) {

                /*
                 * 緯度
                 */
                // 清空result shift
                int charLatVal = 0;
                int resultLat = 0;
                int shiftLat = 0;
                do {
                    charLatVal = points.charAt(index++) - 63; // 至少六碼(二進位) 因為被加32
                    resultLat |= (charLatVal & 0x1F) << shiftLat;// (charLatVal & 0x1F)後剩五碼
                    shiftLat += 5;// 所以這裡步進5
                } while (charLatVal >= 32);
                // 與上一個緯度的差距
                int dLat = (resultLat & 1) != 0 ? ~(resultLat >> 1) : (resultLat >> 1);
                lat += dLat;

                /*
                 * 經度
                 */
                // 清空result shift
                int charLngVal = 0;
                int resultLng = 0;
                int shiftLng = 0;
                do {
                    charLngVal = points.charAt(index++) - 63;
                    resultLng |= (charLngVal & 0x1F) << shiftLng;
                    shiftLng += 5;
                } while (charLngVal >= 32);
                // 與上一個經度的差距
                int dLng = (resultLng & 1) != 0 ? ~(resultLng >> 1) : (resultLng >> 1);
                lng += dLng;

                //test-
                Log.d("緯經度解析 -> ", "lat = " + (double) lat / 1E5 + " lng = " + (double) lng / 1E5);

                /*
                 * 加總
                 */
                list.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
            }
            return list;
        }
    }
}

