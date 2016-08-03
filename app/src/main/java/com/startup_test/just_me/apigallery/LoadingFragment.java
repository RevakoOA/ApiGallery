package com.startup_test.just_me.apigallery;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by just_me on 02.08.16.
 */
public class LoadingFragment extends Fragment {
    private static final String TAG = "LoadingFragment";

    private String userID;
    private RequestQueue requestQueue;
    private Map<String, String> mHeaders = new android.support.v4.util.ArrayMap<>();
    private JSONArray imagesInfo;


    @BindView(R.id.infoLog)
    TextView infoLog;
    @BindView(R.id.progressBar)
    CircleProgressBar progressBar;
    @BindView(R.id.goToGallery)
    public Button goToGalleryButton;

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getMessage());
            addLogInfo(error.getMessage());
            error.printStackTrace();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.loading_fragment, container, false);
        ButterKnife.bind(this, fragment);
        progressBar.onAnimationStart();
        progressBar.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        infoLog.setText("");
        login();
        return fragment;
    }

    private void login() {
        String login = getString(R.string.server_url) + getString(R.string.login_url);
        Request<NetworkResponse> request = new Request<NetworkResponse>(Request.Method.POST, login, errorListener) {
            @Override
            protected void deliverResponse(NetworkResponse response) {
                Log.i(TAG, "response status is " + response.statusCode);
                if (response.statusCode == 200) {
                    userID = new String(response.data);
                    Log.i(TAG, "user_ID: " + userID);
                    addLogInfo("user ID is " + userID);
                    getPhotoList();
                } else {
                    Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
                return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(request);
    }

    private void getPhotoList() {
        final String listUrl = getString(R.string.server_url) + getString(R.string.list_url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, listUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    imagesInfo = response.getJSONArray("images");
                    Log.i(TAG, "Pictures info successfully fetched");
                    addLogInfo("Pictures successfully fetched in count " + imagesInfo.length());
                    for (int i = 0; i < imagesInfo.length(); i++) {
                        addLogInfo((String) ((JSONObject) imagesInfo.get(i)).get("title"));
                    }
                    progressBar.setEnabled(false);
                    goToGalleryButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new android.support.v4.util.ArrayMap<>(1);
                headers.put("user_id", userID);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void addLogInfo(String info) {
        if (infoLog != null) {
            infoLog.setText(infoLog.getText().toString() + '\n' + info);
        }
    }

    @OnClick(R.id.goToGallery)
    public void galleryClick(View v) {
        Bundle bundle = new Bundle();
        String[] imagesUrls = new String[imagesInfo.length()];
        for (int i = 1; i <= imagesUrls.length; i++) {
            imagesUrls[i-1] = getString(R.string.server_url) + getString(R.string.item_base_url) + i;
        }
        bundle.putStringArray(getString(R.string.images_urls), imagesUrls);
        bundle.putString(getString(R.string.userID), userID);
        ((MainActivity)getActivity()).galleryStart(bundle);
    }

}
