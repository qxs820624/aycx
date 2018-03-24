package com.android.liyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.bean.CommendBean;
import com.android.liyun.inter.OnItemClickListener;
import com.android.liyun.ui.goods.ComDetailsAct;
import com.android.liyun.ui.login.RegistAct;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> channelList;
    private List<Integer> girlList;
    List<String> picList;
    private List<String> normalList;
    private final int BANNER_VIEW_TYPE = 0;//轮播图
    private final int CHANNEL_VIEW_TYPE = 1;//频道
    private final int GIRL_VIEW_TYPE = 2;//美女
    private final int INSURANCE_VOUCHERS = 3;//保险代金券
    private final int NORMAL_VIEW_TYPE = 4;//正常布局

    List<CommendBean.GoodsBean.OneBean> oneBeans;
    List<CommendBean.GoodsBean.TwoBean> twoBeans;
    List<CommendBean.GoodsBean.ThreeBean> threeBeans;

    public RecyclerAdapter(Context context, List<String> picList,
                           List<CommendBean.GoodsBean.OneBean> oneBeans,
                           List<CommendBean.GoodsBean.TwoBean> twoBeans, List<CommendBean.GoodsBean.ThreeBean> threeBeans) {
        this.context = context;
        this.oneBeans = oneBeans;
        this.girlList = girlList;
        this.picList = picList;
        this.twoBeans = twoBeans;
        this.threeBeans = threeBeans;
    }

    /**
     * 获取item的类型
     *
     * @param position item的位置
     * @return item的类型
     * 有几种type就回在onCreateViewHolder方法中引入几种布局,也就是创建几个ViewHolder
     */
    @Override
    public int getItemViewType(int position) {
        /*
        区分item类型,返回不同的int类型的值
        在onCreateViewHolder方法中用viewType来创建不同的ViewHolder
         */
        if (position == 0) {//第0个位置是轮播图
            return BANNER_VIEW_TYPE;
        } else if (position == 1) {//第一个是频道布局
            return CHANNEL_VIEW_TYPE;
        } else if (position == 2) {//第2个位置是美女布局
            return GIRL_VIEW_TYPE;
        } else if (position == 3) {//其他位置返回正常的布局
            return INSURANCE_VOUCHERS;
        } else {
            return NORMAL_VIEW_TYPE;
        }
    }

    /**
     * 创建ViewHolder,根据getItemViewType方法里面返回的几种类型来创建
     *
     * @param viewType 就是getItemViewType返回的type
     * @return 返回自己创建的ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == BANNER_VIEW_TYPE) {//如果viewType是轮播图就去创建轮播图的viewHolder
            view = getView(R.layout.item_banner);
            BannerHolder bannerHolder = new BannerHolder(view);
            return bannerHolder;
        } else if (viewType == CHANNEL_VIEW_TYPE) {//频道的type
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_head, parent, false);//解决宽度不能铺满
            return new ChannelHolder(view);
        } else if (viewType == GIRL_VIEW_TYPE) {//美女
            // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_girl, parent, false);//解决宽度不能铺满
            view = getView(R.layout.item_girl);
            return new OneGoodsHolder(view);
        } else if (viewType == INSURANCE_VOUCHERS) {
            view = getView(R.layout.item_insurance_vouchers);
            return new InsVouHolder(view);
        } else if (viewType == NORMAL_VIEW_TYPE) {
            view = getView(R.layout.item_car_goods);
//            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(lp);
            return new CarServiceHolder(view);
        } else {//正常
            view = getView(R.layout.item_car_goods);
            return new CarServiceHolder(view);
        }
    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int view) {
        View view1 = View.inflate(context, view, null);
        return view1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BannerHolder) {//轮播图
            BannerHolder bannerHolder = (BannerHolder) holder;
            //设置轮播图相关
            setBanner(bannerHolder);
        } else if (holder instanceof ChannelHolder) {//频道
            ChannelHolder channelHolder = (ChannelHolder) holder;
        } else if (holder instanceof OneGoodsHolder) {//车载商品
            OneGoodsHolder OneGoodsHolder = (OneGoodsHolder) holder;
            CarGoodsAdapter adapter = new CarGoodsAdapter(context, oneBeans);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(UIUtils.getContext(), oneBeans.get(position).getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("goods_id", oneBeans.get(position).getGoods_id());
                    intent.setClass(UIUtils.getContext(), ComDetailsAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    UIUtils.getContext().startActivity(intent);
                }
            });
            OneGoodsHolder.recyclerView.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 2));
            OneGoodsHolder.recyclerView.setAdapter(adapter);
        } else if (holder instanceof InsVouHolder) {//保险代金券
            InsVouHolder insVouHolder = (InsVouHolder) holder;
            InsVouAdapter adapter = new InsVouAdapter(context, twoBeans);
            insVouHolder.recyclerView.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 3));
            insVouHolder.recyclerView.setAdapter(adapter);
        } else {//正常布局
            CarServiceHolder carServiceHolder = (CarServiceHolder) holder;
            CarServiceAdapter adapter = new CarServiceAdapter(context, threeBeans);
            carServiceHolder.recyclerView.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 3));
            carServiceHolder.recyclerView.setAdapter(adapter);
        }

    }

    /**
     * 设置轮播图
     *
     * @param bannerHolder
     */
    private void setBanner(BannerHolder bannerHolder) {
        //设置banner样式
        bannerHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bannerHolder.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        bannerHolder.banner.setImages(picList);
        //设置banner动画效果
        bannerHolder.banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
//            bannerHolder.banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        bannerHolder.banner.isAutoPlay(true);
        //设置轮播时间
//            bannerHolder.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        bannerHolder.banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        bannerHolder.banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    /*****************************************下面是为不同的布局创建不同的ViewHolder*******************************************************/
    /**
     * 轮播图的ViewHolder
     */
    public static class BannerHolder extends RecyclerView.ViewHolder {
        Banner banner;

        public BannerHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner);

        }
    }

    /**
     * 频道列表的ViewHolder
     */
    public static class ChannelHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        public ChannelHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llyt_goods_head);
        }
    }

    /**
     * 车载设备
     */
    public static class OneGoodsHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public OneGoodsHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleView);
        }
    }

    /**
     * 保险代金券
     */
    public static class InsVouHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public InsVouHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleView);
        }
    }

    /**
     * 汽车服务
     */
    public static class CarServiceHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public CarServiceHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleView);
        }
    }

    /**
     * 正常布局的ViewHolder
     */
    public static class NormalHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public NormalHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
