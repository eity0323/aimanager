package com.sien.lib.datapp.network.base;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author sien
 * @date 2016/10/17
 * @descript 自定义retrofit 字符串解析工具类（解析传入参数）
 */
public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }


    /**
     * 返回参数解析处理
     * @param type
     * @param annotations
     * @param retrofit
     * @return
     */

//    @Override
//    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
//                                                            Retrofit retrofit) {
//        return new StringResponseConverter();
//    }

    /**
     * 请求参数解析处理
     * @param type
     * @param parameterAnnotations
     * @param methodAnnotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestConverter();
    }

//    public static class StringResponseConverter implements Converter<ResponseBody, String> {
//
//        @Override
//        public String convert(ResponseBody value) throws IOException {
////            return value.string();
//
//            BufferedReader r = new BufferedReader(new InputStreamReader(value.byteStream()));
//            StringBuilder total = new StringBuilder();
//            String line;
//            while ((line = r.readLine()) != null) {
//                total.append(line);
//            }
//            return total.toString();
//        }
//    }

    public static class StringRequestConverter implements Converter<String, RequestBody> {
        @Override
        public RequestBody convert(String value) throws IOException {
            return RequestBody.create(MediaType.parse("application/json"), value);//text/plain //application/octet-stream
        }
    }
}