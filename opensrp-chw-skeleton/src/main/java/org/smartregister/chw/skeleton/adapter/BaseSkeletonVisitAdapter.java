package org.smartregister.chw.skeleton.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.smartregister.chw.skeleton.contract.BaseSkeletonVisitContract;
import org.smartregister.chw.skeleton.model.BaseSkeletonVisitAction;
import org.smartregister.chw.skeleton.R;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseSkeletonVisitAdapter extends RecyclerView.Adapter<BaseSkeletonVisitAdapter.MyViewHolder> {
    private Map<String, BaseSkeletonVisitAction> skeletonVisitActionList;
    private Context context;
    private BaseSkeletonVisitContract.View visitContractView;

    public BaseSkeletonVisitAdapter(Context context, BaseSkeletonVisitContract.View view, LinkedHashMap<String, BaseSkeletonVisitAction> myDataset) {
        skeletonVisitActionList = myDataset;
        this.context = context;
        this.visitContractView = view;
    }

    @NotNull
    @Override
    public BaseSkeletonVisitAdapter.MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skeleton_visit_item, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * get the position of the the valid items in the data set
     *
     * @param position
     * @return
     */
    private BaseSkeletonVisitAction getByPosition(int position) {
        int count = -1;
        for (Map.Entry<String, BaseSkeletonVisitAction> entry : skeletonVisitActionList.entrySet()) {
            if (entry.getValue().isValid())
                count++;

            if (count == position)
                return entry.getValue();
        }

        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {

        BaseSkeletonVisitAction skeletonVisitAction = getByPosition(position);
        if (skeletonVisitAction == null)

            return;

        if (!skeletonVisitAction.isEnabled()) {
            holder.titleText.setTextColor(context.getResources().getColor(R.color.grey));
            holder.descriptionText.setTextColor(context.getResources().getColor(R.color.grey));
        } else {
            holder.titleText.setTextColor(context.getResources().getColor(R.color.black));
        }

        String title = MessageFormat.format("{0}<i>{1}</i>",
                skeletonVisitAction.getTitle(),
                skeletonVisitAction.isOptional() ? " - " + context.getString(R.string.optional) : ""
        );
        holder.titleText.setText(Html.fromHtml(title));
        if (StringUtils.isNotBlank(skeletonVisitAction.getSubTitle())) {

            if (skeletonVisitAction.isEnabled()) {
                holder.descriptionText.setVisibility(View.VISIBLE);
                holder.invalidText.setVisibility(View.GONE);
                holder.descriptionText.setText(skeletonVisitAction.getSubTitle());

                boolean isOverdue = skeletonVisitAction.getScheduleStatus() == BaseSkeletonVisitAction.ScheduleStatus.OVERDUE &&
                        skeletonVisitAction.isEnabled();

                holder.descriptionText.setTextColor(
                        isOverdue ? context.getResources().getColor(R.color.alert_urgent_red) :
                                context.getResources().getColor(android.R.color.darker_gray)
                );

            } else {
                holder.descriptionText.setVisibility(View.GONE);
                holder.invalidText.setVisibility(View.VISIBLE);
                holder.invalidText.setText(Html.fromHtml("<i>" + skeletonVisitAction.getDisabledMessage() + "</i>"));
            }
        } else {
            holder.descriptionText.setVisibility(View.GONE);
        }

        int color_res = getCircleColor(skeletonVisitAction);

        holder.circleImageView.setCircleBackgroundColor(context.getResources().getColor(color_res));
        holder.circleImageView.setImageResource(R.drawable.ic_checked);
        holder.circleImageView.setColorFilter(context.getResources().getColor(R.color.white));

        if (color_res == R.color.transparent_gray) {
            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.light_grey));
        } else {
            holder.circleImageView.setBorderColor(context.getResources().getColor(color_res));
        }

        bindClickListener(holder.getView(), skeletonVisitAction);
    }

    private int getCircleColor(BaseSkeletonVisitAction skeletonVisitAction) {

        int color_res;
        boolean valid = skeletonVisitAction.isValid() && skeletonVisitAction.isEnabled();
        if (!valid)
            return R.color.transparent_gray;

        switch (skeletonVisitAction.getActionStatus()) {
            case PENDING:
                color_res = R.color.transparent_gray;
                break;
            case COMPLETED:
                color_res = R.color.alert_complete_green;
                break;
            case PARTIALLY_COMPLETED:
                color_res = R.color.pnc_circle_yellow;
                break;
            default:
                color_res = R.color.alert_complete_green;
                break;
        }
        return color_res;
    }

    private void bindClickListener(View view, final BaseSkeletonVisitAction skeletonVisitAction) {
        if (!skeletonVisitAction.isEnabled() || !skeletonVisitAction.isValid()) {
            view.setOnClickListener(null);
            return;
        }

        view.setOnClickListener(v -> {
            if (StringUtils.isNotBlank(skeletonVisitAction.getFormName())) {
                visitContractView.startForm(skeletonVisitAction);
            } else {
                visitContractView.startFragment(skeletonVisitAction);
            }
            visitContractView.redrawVisitUI();
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Map.Entry<String, BaseSkeletonVisitAction> entry : skeletonVisitActionList.entrySet()) {
            if (entry.getValue().isValid())
                count++;
        }

        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, invalidText, descriptionText;
        private CircleImageView circleImageView;
        private LinearLayout myView;

        private MyViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.customFontTextViewTitle);
            descriptionText = view.findViewById(R.id.customFontTextViewDetails);
            invalidText = view.findViewById(R.id.customFontTextViewInvalid);
            circleImageView = view.findViewById(R.id.circleImageView);
            myView = view.findViewById(R.id.linearLayoutHomeVisitItem);
        }

        public View getView() {
            return myView;
        }
    }

}
