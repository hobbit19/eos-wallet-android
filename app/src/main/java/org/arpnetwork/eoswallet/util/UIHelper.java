package org.arpnetwork.eoswallet.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.arpnetwork.eoswallet.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIHelper {
    private static final Logger LOG = LoggerFactory.getLogger(UIHelper.class);

    private static Toast sToast = null;
    private static Toast sImageToast = null;

    public static void showToast(Context context, int textId) {
        if (context == null) return;

        if (sToast == null) {
            sToast = Toast.makeText(context, textId, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(textId);
        }

        sToast.show();
    }

    public static void showToast(Context context, CharSequence text) {
        if (context == null) return;

        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
        }

        sToast.show();
    }

    public static void showToast(Context context, CharSequence text, int duration) {
        if (context == null) return;

        if (sToast == null) {
            sToast = Toast.makeText(context, text, duration);
        } else {
            sToast.setText(text);
        }

        sToast.show();
    }

    public static void showImageToast(Context context, int imageId, int textId) {
        if (context == null) return;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image_toast, null);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        TextView textView = view.findViewById(R.id.tv_toast);

        if (sImageToast == null) {
            sImageToast = new Toast(context);
            sImageToast.setDuration(Toast.LENGTH_SHORT);
            sImageToast.setView(view);
            sImageToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            sImageToast.setView(view);
        }
        imageView.setImageResource(imageId);
        textView.setText(textId);

        sImageToast.show();
    }

    public static int getResourceIdForName(String res) {
        int resId = -1;

        Pattern pattern = Pattern.compile("^([\\w.]+R)\\.(\\w+)\\.(\\w+)$");
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            String className = String.format(Locale.US, "%s$%s", matcher.group(1), matcher.group(2));
            String fieldName = matcher.group(3);
            try {
                Class<?> klass = Class.forName(className);
                Field field = klass.getField(fieldName);
                resId = field.getInt(klass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return resId;
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    public static int getStatusbarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int id = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            id = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sbar;
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
//        int actionBarHeight = context.getActionBar().getHeight();
//        if (actionBarHeight != 0)
//            return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void safeShowDialog(Dialog var0) {
        try {
            if (var0 != null && !var0.isShowing()) {
                var0.show();
            }
        } catch (Exception var2) {
        }

    }
    // com.umeng.socialize.utils.SocializeUtils.safeCloseDialog
    public static void safeCloseDialog(Dialog var0) {
        try {
            if (var0 != null && var0.isShowing()) {
                var0.dismiss();
                var0 = null;
            }
        } catch (Exception var2) {
        }

    }

    public static void setIndentationText(Context context, TextView textView, String string) {
        SpannableString spannableString =new SpannableString(string);
        LeadingMarginSpan.Standard what =new LeadingMarginSpan.Standard(0, UIHelper.dip2px(context, 6));
        spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(spannableString);
    }
}
