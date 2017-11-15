package com.zhangmeng.studio.view;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.NavigatorContract;
import com.zhangmeng.studio.contract.ZoomContract.*;

import java.util.ArrayList;
import java.util.List;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/13.
 */

public class ZoomPagerAdapter  extends PagerAdapter implements Presenter{
    private ArrayList<String> imageUries;
    private ZoomView zoomView;
    private  int mItemCount;
    private boolean mAllowSwipingWhileZoomed = true;

    public ZoomPagerAdapter(ArrayList<String> imageUries,ZoomView view) {
        this.imageUries=imageUries;
        mItemCount = imageUries.size();
        zoomView=view;
    }

    public void setAllowSwipingWhileZoomed(boolean allowSwipingWhileZoomed) {
        mAllowSwipingWhileZoomed = allowSwipingWhileZoomed;
    }

    public boolean allowsSwipingWhileZoomed() {
        return mAllowSwipingWhileZoomed;
    }

    public void toggleAllowSwipingWhileZoomed() {
        mAllowSwipingWhileZoomed = !mAllowSwipingWhileZoomed;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ZoomableDraweeView zoomableDraweeView;TextView imageIndex;
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.zoomable_image,container,false);
        zoomableDraweeView =
                    (ZoomableDraweeView) view.findViewById(R.id.zoomableView);
        zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(mAllowSwipingWhileZoomed);
        zoomableDraweeView.setIsLongpressEnabled(false);
        zoomableDraweeView.setTapListener(new DoubleTapGestureListener(zoomableDraweeView));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(getImageUri(position))
                .setCallerContext(this)
                .build();
        zoomableDraweeView.setController(controller);
        container.addView(view);
        view.requestLayout();
        setImageIndex(position+"/"+getCount());
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        ZoomableDraweeView zoomableDraweeView = (ZoomableDraweeView) view.findViewById(R.id.zoomableView);
        zoomableDraweeView.setController(null);
        container.removeView(view);
    }


    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
         return POSITION_NONE;
    }

    public Uri getImageUri(int position)
    {
        String url=imageUries.get(position);
        return Uri.parse(url);
    }

    @Override
    public void setImageIndex(String index) {
        zoomView.setImageIndex(index);
    }
}

