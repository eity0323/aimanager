package com.sien.lib.baseapp.control;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sien.lib.baseapp.activity.CPNoFoundActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author sien
 * @date 2016/9/26
 * @descript 独立apk插件bundle
 *  分离small框架，独立加载apk，需要优化
 */
public class CPAloneBundle {
    //______________________________________________________________________________
    public static final String KEY_QUERY = "sien-bundle-query";
    // Fields
    private static final String BUNDLE_MANIFEST_NAME = "bundle.json";
    private static final String VERSION_KEY = "version";
    private static final String BUNDLES_KEY = "bundles";
    private static final String HOST_PACKAGE = "main";

    private static final class Manifest {
        String version;
        List<CPAloneBundle> bundles;
    }

    private static List<CPAloneBundle> sPreloadBundles = null;//插件列表

    private String mPackageName;//包名
    private String uriString;//对外可见页面引用字符串
    private Uri uri;//对外可见uri
    private Intent mIntent;//对外可用的intent
    private String path;//对外可用的类名
    private String query;//传入参数
    private HashMap<String, String> rules;//配置的可调用页面

    private boolean enabled = true;//插件是否可用

    private static String sBaseUri = "sien://";//插件scheme

    //______________________________________________________________________________
    // Class methods

    /**
     * 加载插件配置文件
     * @param context
     */
    public static void loadBundles(Context context) {
        JSONObject manifestData;
        try {
            // Load from built-in `assets/bundle.json'
            InputStream builtinManifestStream = context.getAssets().open(BUNDLE_MANIFEST_NAME);
            int builtinSize = builtinManifestStream.available();
            byte[] buffer = new byte[builtinSize];
            builtinManifestStream.read(buffer);
            builtinManifestStream.close();
            String manifestJson = new String(buffer, 0, builtinSize);

            // Parse manifest file
            manifestData = new JSONObject(manifestJson);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Manifest manifest = parseManifest(manifestData);
        if (manifest == null) return;

        loadBundles(manifest.bundles);
    }

    private static Manifest parseManifest(JSONObject data) {
        try {
            String version = data.getString(VERSION_KEY);
            return parseManifest(version, data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Manifest parseManifest(String version, JSONObject data) {
        if (version.equals("1.0.0")) {
            try {
                JSONArray bundleDescs = data.getJSONArray(BUNDLES_KEY);
                int N = bundleDescs.length();
                List<CPAloneBundle> bundles = new ArrayList<CPAloneBundle>(N);
                for (int i = 0; i < N; i++) {
                    try {
                        JSONObject object = bundleDescs.getJSONObject(i);

                        //解析插件bundle
                        CPAloneBundle bundle = new CPAloneBundle(object);
                        bundles.add(bundle);
                    } catch (JSONException e) {
                        // Ignored
                    }
                }
                Manifest manifest = new Manifest();
                manifest.version = version;
                manifest.bundles = bundles;
                return manifest;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        throw new UnsupportedOperationException("Unknown version " + version);
    }

    private static void loadBundles(List<CPAloneBundle> bundles) {
        sPreloadBundles = bundles;
    }

    private static CPAloneBundle findBundle(String name, List<CPAloneBundle> bundles) {
        if (name == null) return null;
        if (bundles == null) return null;
        for (CPAloneBundle bundle : bundles) {
            if (bundle.mPackageName == null) continue;
            if (bundle.mPackageName.equals(name)) return bundle;
        }
        return null;
    }

    protected static CPAloneBundle getLaunchableBundle(Uri uri) {
        if (sPreloadBundles != null) {
            for (CPAloneBundle bundle : sPreloadBundles) {
                if (bundle.matchesRule(uri)) {
                    if (!bundle.enabled) return null; // Illegal bundle (invalid signature, etc.)
                    return bundle;
                }
            }
        }
        return null;
    }

    private Boolean matchesRule(Uri uri) {
    /* e.g.
     *  input
     *      - uri: http://base/abc.html
     *      - self.uri: http://base
     *      - self.rules: abc.html -> AbcController
     *  output
     *      - target => AbcController
     */
        String uriString = uri.toString();
        if (this.uriString == null || !uriString.startsWith(this.uriString)) return false;

        String srcPath = uriString.substring(this.uriString.length());
        String srcQuery = uri.getQuery();
        if (srcQuery != null) {
            srcPath = srcPath.substring(0, srcPath.length() - srcQuery.length() - 1);
        }

        String dstPath = null;
        String dstQuery = srcQuery;
        if (srcPath.equals("")) {
            dstPath = srcPath;
        } else {
            for (String key : this.rules.keySet()) {
                // TODO: regex match and replace
                if (key.equals(srcPath)) dstPath = this.rules.get(key);
                if (dstPath != null) break;
            }
            if (dstPath == null) return false;

            int index = dstPath.indexOf("?");
            if (index > 0) {
                if (dstQuery != null) {
                    dstQuery = dstQuery + "&" + dstPath.substring(index + 1);
                } else {
                    dstQuery = dstPath.substring(index + 1);
                }
                dstPath = dstPath.substring(0, index);
            }
        }

        this.path = dstPath;
        this.query = dstQuery;
        return true;
    }

    //______________________________________________________________________________
    // Instance methods
    public CPAloneBundle() {

    }

    public CPAloneBundle(JSONObject map) {
        try {
            this.initWithMap(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*解析插件内部页面*/
    private void initWithMap(JSONObject map) throws JSONException {

        if (map.has("pkg")) {
            String pkg = map.getString("pkg");
            if (pkg != null && !pkg.equals(HOST_PACKAGE)) {
                mPackageName = pkg;
            }
        }

        if (map.has("uri")) {
            String uri = map.getString("uri");
            if (!uri.startsWith("http") && sBaseUri != null) {
                uri = sBaseUri + uri;
            }
            this.uriString = uri;
            this.uri = Uri.parse(uriString);
        }

        this.rules = new HashMap<String, String>();
        // Default rules to visit entrance page of bundle
        this.rules.put("", "");
        this.rules.put(".html", "");
        this.rules.put("/index", "");
        this.rules.put("/index.html", "");
        if (map.has("rules")) {
            // User rules to visit other page of bundle
            JSONObject rulesObj = map.getJSONObject("rules");
            Iterator<String> it = rulesObj.keys();
            while (it.hasNext()) {
                String key = it.next();
                this.rules.put("/" + key, rulesObj.getString(key));
            }
        }
    }

    protected String getActivityName() {
        String activityName = path;
        String pkg = mPackageName != null ? mPackageName : "";
        char c = activityName.charAt(0);
        if (c == '.') {
            activityName = pkg + activityName;
        } else if (c >= 'A' && c <= 'Z') {
            activityName = pkg + '.' + activityName;
        }
        return activityName;
    }

    //打开插件配置页面方法，需要优化
    public static void openUri(String uriString, Context context) {
        openUri(makeUri(uriString), context);
    }

    public static void openUri(Uri uri, Context context) {
        // Small url schemes
        CPAloneBundle bundle = CPAloneBundle.getLaunchableBundle(uri);
        if (bundle != null) {
            Intent tent = new Intent();
            bundle.setIntent(tent);

            String activityName;
            try {
                String uripath = uri.toString();
                activityName = uripath.substring(uripath.lastIndexOf("/"), uripath.indexOf("?") > 0 ? uripath.indexOf("?") : uripath.length());

                tent.setComponent(new ComponentName(context,bundle.rules.get(activityName)));
            }catch (Exception ex){
                activityName = bundle.path;

                tent.setComponent(new ComponentName(context,activityName));
            }

            // Intent extras - params
            String query = bundle.getQuery();
            if (query != null) {
                tent.putExtra(CPAloneBundle.KEY_QUERY, '?'+query);
            }
            context.startActivity(tent);
        }else {
            Intent tent = new Intent(context, CPNoFoundActivity.class);
            context.startActivity(tent);
        }
    }

    public static void openUriForResult(String uriString, Activity context, int requestCode) {
        openUriForResult(makeUri(uriString), context,requestCode);
    }
    public static void openUriForResult(Uri uri, Activity context, int requestCode) {
        // Small url schemes
        CPAloneBundle bundle = CPAloneBundle.getLaunchableBundle(uri);
        if (bundle != null) {
            Intent tent = new Intent();
            bundle.setIntent(tent);

            String activityName;
            try {
                String uripath = uri.toString();
                activityName = uripath.substring(uripath.lastIndexOf("/"), uripath.indexOf("?") > 0 ? uripath.indexOf("?") : uripath.length());

                tent.setComponent(new ComponentName(context,bundle.rules.get(activityName)));

            }catch (Exception ex){
                activityName = bundle.path;

                tent.setComponent(new ComponentName(context,activityName));
            }


            // Intent extras - params
            String query = bundle.getQuery();
            if (query != null) {
                tent.putExtra(CPAloneBundle.KEY_QUERY, '?'+query);
            }
            context.startActivityForResult(tent,requestCode);
        }else {
            Intent tent = new Intent(context, CPNoFoundActivity.class);
            context.startActivity(tent);
        }
    }

    public static void openUriForResult(Intent intent, Activity context, int requestCode){
        // Small url schemes
        if (intent != null) {
            context.startActivityForResult(intent,requestCode);
        }
    }

    //获取插件配置页面方法，需要优化
    public static Intent getIntentOfUri(String uriString, Context context) {
        return getIntentOfUri(makeUri(uriString), context);
    }

    public static Intent getIntentOfUri(Uri uri, Context context) {
        // Small url schemes
        CPAloneBundle bundle = CPAloneBundle.getLaunchableBundle(uri);
        if (bundle != null) {
            Intent tent = new Intent();
            bundle.setIntent(tent);

            String activityName;
            try {
                String uripath = uri.toString();
                activityName = uripath.substring(uripath.lastIndexOf("/"), uripath.indexOf("?") > 0 ? uripath.indexOf("?") : uripath.length());

                tent.setComponent(new ComponentName(context,bundle.rules.get(activityName)));
            }catch (Exception ex){
                activityName = bundle.path;

                tent.setComponent(new ComponentName(context,activityName));
            }

            // Intent extras - params
            String query = bundle.getQuery();
            if (query != null) {
                tent.putExtra(CPAloneBundle.KEY_QUERY, '?'+query);
            }
            return tent;
        }
        return null;
    }

    //获取插件配置页面参数方法，需要优化
    public static Uri getUri(Activity context) {
        android.os.Bundle extras = context.getIntent().getExtras();
        if (extras == null) {
            return null;
        }
        String query = extras.getString(KEY_QUERY);
        if (query == null) {
            return null;
        }
        return Uri.parse(query);
    }

    private static Uri makeUri(String uriString) {
        if (!uriString.startsWith("http://")
                && !uriString.startsWith("https://")
                && !uriString.startsWith("file://")) {
            uriString = sBaseUri + uriString;
        }
        return Uri.parse(uriString);
    }

    protected Intent getIntent() { return mIntent; }
    protected void setIntent(Intent intent) { mIntent = intent; }

    protected String getPackageName() {
        return mPackageName;
    }

    protected Uri getUri() {
        return uri;
    }

    protected String getQuery() {
        return query;
    }

    protected void setQuery(String query) {
        this.query = query;
    }
}

