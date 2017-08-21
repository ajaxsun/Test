package com.example.myapplication.pickerview.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.myapplication.R;
import com.example.myapplication.pickerview.lib.LoopView;
import com.example.myapplication.pickerview.lib.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class PopDateHelper2 {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> listDate, ListSecond, ListPeriod, listTime, list24Time;
    private String date, time, stage, second;

    public PopDateHelper2(Context context) {
        this.context = context;
        init();
    }

    @SuppressLint("InflateParams")
    public void init() {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.picker_date, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initPop();
        initData();
        initView();
    }

    @SuppressWarnings("deprecation")
    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtil.backgroundAlpha((Activity) context, 1f);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        listDate = getDateList();

        ListPeriod = getPeriodAllList();
        listTime = getTime12AllList();
        list24Time = getTime24AllList();

        ListSecond = getSecondAllList();

    }

    boolean b = false;

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        final LoopView loopView3 = (LoopView) view.findViewById(R.id.loopView3);

        final LoopView loopView4 = (LoopView) view.findViewById(R.id.loopView4);
        view.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        if (Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).get(Calendar.HOUR_OF_DAY) > 12)
            stage = "下午";
        else
            stage = "上午";

        loopView1.setItems(listDate);
        loopView1.setNotLoop();
        loopView1.setCurrentPosition(0);

        loopView2.setItems(ListPeriod);
        loopView2.setNotLoop();
        loopView2.setCurrentPosition("上午".equals(stage) ? 0 : 1);

        loopView3.setItems(listTime);
        loopView3.setNotLoop();
        int index = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).get(Calendar.HOUR_OF_DAY) - 1;
        loopView3.setCurrentPosition(index > 11 ? index - 12 : index);

        loopView4.setItems(ListSecond);
        loopView4.setNotLoop();
        loopView4.setCurrentPosition(0);

        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String select_item = listDate.get(index);
                date = select_item.substring(0, select_item.indexOf("日")).replace("年", "-").replace("月", "-");
            }
        });

        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int item) {
                stage = ListPeriod.get(item);

                if (b == false) {
                    loopView3.setItems(listTime);
                    loopView3.setNotLoop();
                    loopView3.setCurrentPosition(0);
                    b = true;
                } else {
                    if (stage.equals("上午")) {
                        loopView3.setItems(listTime);
                        loopView3.setNotLoop();
                        loopView3.setCurrentPosition(0);
                    } else {
                        loopView3.setItems(list24Time);
                        loopView3.setNotLoop();
                        loopView3.setCurrentPosition(0);
                    }
                }
            }
        });

        loopView3.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int item) {
                if (stage.equals("上午")) {
                    time = listTime.get(item);
                } else {
                    time = list24Time.get(item);
                }

            }
        });

        loopView4.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int item) {
                second = ListSecond.get(item);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onClickOkListener != null)
                            onClickOkListener.onClickOk(date, stage, time, second);
                    }
                }, 500);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        pop.setOnDismissListener(onDismissListener);
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(String date, String stage, String time, String second);
    }

    /**
     * 获取当前时间向后一周内的日期列表
     *
     * @return
     */
    public static List<String> getDateList() {
        List<String> months = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        long current_time = System.currentTimeMillis();
        long day_ms = 24 * 60 * 60 * 1000;
        for (int i = 0; i < 365; i++) {
            c.setTimeInMillis(current_time + day_ms * i);
            int tempMonth = (c.get(Calendar.MONTH) + 1);
            String tempMonthStr = tempMonth < 10 ? "0" + tempMonth : tempMonth + "";
            int tempDay = c.get(Calendar.DAY_OF_MONTH);
            String tempDayStr = tempDay < 10 ? "0" + tempDay : tempDay + "";
            if (i == 0) {
                months.add(c.get(Calendar.YEAR) + "年" + tempMonthStr + "月" + tempDayStr
                        + "日 今天");
            } else if (i == 1) {

                months.add(c.get(Calendar.YEAR) + "年" + tempMonthStr + "月" + tempDayStr
                        + "日 明天");
            } else {
                months.add(c.get(Calendar.YEAR) + "年" + tempMonthStr + "月" + tempDayStr
                        + "日 " + getWeek(c.get(Calendar.DAY_OF_WEEK) - 1));
            }
        }
        return months;
    }

    /**
     * 获取该天可预约的时间列表（全天）-小时
     *
     * @return
     */
    public static List<String> getPeriodAllList() {
        List<String> stageList = new ArrayList<String>();
        stageList.add("上午");
        stageList.add("下午");
        return stageList;
    }

    /**
     * 获取该天可预约的时间列表（全天）上午-小时
     *
     * @return
     */
    public static List<String> getTime12AllList() {
        List<String> timeList = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {

            if (i <= 12) {
                String str = i < 10 ? "0" + i : i + "";
                timeList.add(str);
            }
        }
        return timeList;
    }

    /**
     * 获取该天可预约的时间列表（全天）下午-小时
     *
     * @return
     */
    public static List<String> getTime24AllList() {

        List<String> timeList = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            if (i > 12) {
                if (i == 24) {
                    timeList.add("00");
                } else {
                    timeList.add(i + "");
                }
            }
        }

        return timeList;
    }

    /**
     * 获取该天可预约的时间列表（全天）-分钟
     *
     * @return
     */
    public static List<String> getSecondAllList() {
        List<String> SecondList = new ArrayList<String>();

        for (int j = 0; j < 6; j++) {
            SecondList.add(j + "0");
            SecondList.add(j + "5");
        }
        return SecondList;
    }

    /**
     * 获取星期几
     */
    public static String getWeek(int week) {
        String[] _weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        switch (week) {
            case 0:
                return _weeks[0];
            case 1:
                return _weeks[1];
            case 2:
                return _weeks[2];
            case 3:
                return _weeks[3];
            case 4:
                return _weeks[4];
            case 5:
                return _weeks[5];
            case 6:
                return _weeks[6];
        }
        return "";
    }
}
