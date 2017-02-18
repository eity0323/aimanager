package com.sien.aimanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.aimanager.R;
import com.sien.aimanager.model.IFeedbackViewModel;

import java.util.ArrayList;

/**
 * Created by user1 on 2016/12/5.
 */
public class FeedbackUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_IMG = 1;//图片
    private static final int TYPE_ADD = 2;//添加图片按钮
    private static final int MAX_SIZE = 3;//最多3张
    private Context context;
    private ArrayList<String> path;
    private IFeedbackViewModel feedbackView;
    private OnItemButtonClickListener listener;

    public FeedbackUploadAdapter(Context context, ArrayList<String> path, IFeedbackViewModel feedbackView) {
        this.context = context;
        this.path = path;
        this.feedbackView = feedbackView;
    }

    public void releaseMemory(){
        if (path != null){
            path.clear();
        }
        path = null;

        feedbackView = null;
    }

    public void removeItemButtonListener(){
        listener = null;
    }
    public void setItemButtonListener(OnItemButtonClickListener listener) {
        this.listener = listener;
    }

    public void refresh(ArrayList<String> data){
        this.path = data;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < path.size()){
            return TYPE_IMG;
        }else{
            return TYPE_ADD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_IMG){
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_feedback_upload_adapter,parent,false);
            return new ImageUploadVH(itemView);
        }else{
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_upload_add_adapter,parent,false);
            return new AddButtonVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemViewType(position) == TYPE_ADD){
                    //去选择图
                    feedbackView.chooseImageFromAlbum();
                }else{
                    //查看大图
                    feedbackView.review(position);
                }
            }
        });
        if(position<path.size()){
            //加载图片
            ImageUploadVH imageUploadVH = (ImageUploadVH)holder;
            String filePath = "file://" + path.get(position);
            ImageLoader.getInstance().displayImage(filePath,imageUploadVH.iv);
            imageUploadVH.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除图片
                    if (listener != null){
                        listener.onItemClick(path.get(position));
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(path.size()<MAX_SIZE){
            return path.size()+1;
        }else{
            return path.size();
        }
    }

    class AddButtonVH extends RecyclerView.ViewHolder{
        public AddButtonVH(View itemView) {
            super(itemView);
        }

    }

    class ImageUploadVH extends RecyclerView.ViewHolder{
        ImageView iv;
        View btn_remove;
        public ImageUploadVH(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_feedback_img);
            btn_remove = itemView.findViewById(R.id.btn_feedback_img_remove);
        }
    }


    public interface OnItemButtonClickListener{
        public void onItemClick(String data);
    }
}
