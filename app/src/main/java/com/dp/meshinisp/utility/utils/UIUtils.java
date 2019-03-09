package com.dp.meshinisp.utility.utils;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class UIUtils {

    public static void approveEnteredData(EditText editText, final ImageView imageView , final int validationType){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ValidationUtils.validateTexts(charSequence.toString(),validationType)) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /*public static void showSweetAlertDialog(Context context, int type, String message){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, type);
        pDialog.setConfirmText(context.getResources().getString(R.string.action_close));
        pDialog.setContentText(message);
        pDialog.show();
    }*/

    /*public static void  startMoveAnimation(Context context , ArrayList<View> animatedViews) {
        Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.move);
        for(int i=0; i< animatedViews.size(); i++) {
            animatedViews.get(i).startAnimation(animation);
        }
    }

    public static void showSweetAlertDialog(Context context, int type, String message){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, type);
        pDialog.setConfirmText(context.getResources().getString(R.string.action_close));
        pDialog.setContentText(message);
        pDialog.show();
    }

    public static void revealButton(final Context context , final AnimatedButtonView loginAnimatedButton , final View revealView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loginAnimatedButton.setElevation(0f);
            revealView.setVisibility(VISIBLE);
            int cx = revealView.getWidth();
            int cy = revealView.getHeight();
            int fabWidth = (int) context.getResources().getDimension(R.dimen.dp56w);
            int x = (int) (fabWidth / 2 + loginAnimatedButton.getX());
            int y = (int) (fabWidth / 2 + loginAnimatedButton.getY());
            float finalRadius = Math.max(cx, cy) * 1.2f;
            Animator revealA = ViewAnimationUtils.createCircularReveal(revealView, x, y, fabWidth, finalRadius);
            revealA.setDuration(350);
            revealA.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    reset(animation);
                }

                private void reset(Animator animation) {
                    super.onAnimationEnd(animation);
                    revealView.setVisibility(INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        loginAnimatedButton.setElevation(4f);
                    }
                    ViewGroup.LayoutParams layoutParams = loginAnimatedButton.getLayoutParams();
                    layoutParams.width = (int) (context.getResources().getDisplayMetrics().density * 300);
                    loginAnimatedButton.requestLayout();
                }
            });

            revealA.start();
        }

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static int getColumnWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (displayMetrics.widthPixels/2);

    }*/



}