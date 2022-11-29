package com.example.sogong.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitStringClient {
    public static String BASE_URL = "https://recippe-sg.herokuapp.com/";
    private static Retrofit retrofit;

    // // API 주소 연결용 클라이언트 (String 응답 전용)
    public static Retrofit getClient(){
        class NullOnEmptyConverterFactory extends Converter.Factory {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
                return new Converter<ResponseBody, Object>() {
                    @Override
                    public Object convert(ResponseBody body) throws IOException {
                        if (body.contentLength() == 0) return null;
                        return delegate.convert(body);
                    }
                };
            }
        }

        Gson gson = new GsonBuilder().setLenient().create();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }
}
