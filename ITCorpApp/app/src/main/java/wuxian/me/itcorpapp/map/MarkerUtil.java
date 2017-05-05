package wuxian.me.itcorpapp.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wuxian.me.itcorpapp.model.BaseModel;
import wuxian.me.itcorpapp.util.Helper;

/**
 * Created by wuxian on 4/5/2017.
 */

public class MarkerUtil {

    private static boolean inited = false;
    private static RequestManager requestManager;
    private static List<String> failUris = new ArrayList<>();

    public static void init(@NonNull Context context) {
        if (inited) {
            return;
        }

        requestManager = Glide.with(context);
        inited = true;
    }

    public static void addMarker(AMap aMap, BaseModel model) {
        addMarkerFrom(aMap, model, false);
    }

    //Todo: Glide cache custom
    //最后一个参数为true：强制走网络请求,否则当check到失败request list时,直接使用头像生成
    public static void addMarkerFrom(AMap aMap, BaseModel model, boolean forceRemote) {
        if (!inited) {
            return;
        }
        if (true || !forceRemote && failUris.contains(model.getIconUri())) {
            addMarkerFrom(aMap, model, model.getTitle());  //文字生成头像
            return;
        }
        requestManager.load(Uri.parse(model.getIconUri()))
                .downloadOnly(new MarkerLoaderTarget(aMap, model));
    }

    //文字生成marker图片
    //Todo: better showing custom
    private static void addMarkerFrom(AMap aMap, BaseModel model, String text) {
        Bitmap bm = genBitmapFrom(text);
        MarkerOptions options = new MarkerOptions()
                .position(model.getLatLng())
                .title(model.getTitle())
                .snippet(model.getSnippet())
                .icon(BitmapDescriptorFactory.fromBitmap(bm));
        aMap.addMarker(options);

    }

    //远程url生成marker图片
    //icon file大小太大 没法显示...
    private static void addMarkerFrom(AMap aMap, BaseModel model, File file) {
        MarkerOptions options = new MarkerOptions()
                .position(model.getLatLng())
                .title(model.getTitle())
                .snippet(model.getSnippet())
                .icon(BitmapDescriptorFactory.fromFile(file.getAbsolutePath()));
        aMap.addMarker(options);
    }

    /**
     * 如果url下载成功则显示url
     * 否则显示一个名字
     */
    private static class MarkerLoaderTarget extends SimpleTarget<File> {
        private AMap aMap;
        private BaseModel model;

        public MarkerLoaderTarget(@NonNull AMap aMap, @NonNull BaseModel model) {
            this.aMap = aMap;
            this.model = model;
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            if (!failUris.contains(model.getIconUri())) {
                failUris.add(model.getIconUri());
            }
            addMarkerFrom(aMap, model, model.getTitle());
        }

        @Override
        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
            addMarkerFrom(aMap, model, resource);
        }
    }


    private static Bitmap genBitmapFrom(String text) {
        return genBitmapFrom(text, Helper.dp2px(8f));
    }

    //Todo: ui优化
    private static Bitmap genBitmapFrom(String text, float textSize) {

        TextPaint textPaint = new TextPaint();

        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(textSize);

        StaticLayout layout = new StaticLayout(text, textPaint, 450,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,
                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        return bitmap;
    }
}
