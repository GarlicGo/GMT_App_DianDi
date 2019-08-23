package com.example.bottomtesttwo.fragments.fragment3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.ViewPager;

import com.example.bottomtesttwo.R;
import com.example.bottomtesttwo.util.DateUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.bottomtesttwo.activity.MainActivity.setStatusBar;
import static com.example.bottomtesttwo.util.DateUtils.FORMAT_D;
import static com.example.bottomtesttwo.util.DateUtils.FORMAT_M;
import static com.example.bottomtesttwo.util.DateUtils.FORMAT_Y;
import static com.example.bottomtesttwo.util.DateUtils.FORMAT_YMD;

public class Calculator extends AppCompatActivity implements OnClickListener, DatePickerDialog.OnDateSetListener {

    private int selectedPosition = -5; //默认一个参数
    private ViewPager viewpagerItem;
    private List<CalculatorSortItem> calculatorSpendSortItemList = new ArrayList<>();
    private List<CalculatorSortItem> calculatorIncomeSortItemList = new ArrayList<>();
    RecyclerView recyclerView;
    CalculatorAdapter calculatorSpendAdapter;
    CalculatorAdapter calculatorIncomeAdapter;



    private String dp_date;
    private TextView tv_date;
    private TextView tb_note_money;
    private String display = "0.00";
    TextView textViewOutcome;
    TextView textViewIncome;
    EditText editText;

    //辅助参数
    private boolean haveFloat = false;
    private boolean isEdit = false;
    private int itemPosotion = -1;
    private int floatCount = 0;
//    private

    //传递参数（返回给上一个活动）
    private int firstType;//选择支出/收入，1为支出，2为收入
    private int secondType = R.mipmap.income_lijin;//选择小类别
    private int moneyCard;//支付卡包类别
    private double moneyNumber;//记录用户存入钱数
    private String tip = "hhh";
    private String accountingDate;//记录日期（2019年8月22日）
//    accountingDate = String.format("%d年%d月%d日",year,month+1,dayOfMonth);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉顶部标题(状态栏下面带有顶部返回按钮的那个)
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
//        viewpagerItem = (ViewPager)findViewById(R.id.viewpager_item);

        tv_date = (TextView)findViewById(R.id.tb_note_date);
        tb_note_money = (TextView)findViewById(R.id.tb_note_money);
        editText = (EditText)findViewById(R.id.calculator_ediTtext);
//        dp_date = (DatePicker)findViewById(R.id.dp_date);
//        tv_date.setOnClickListener(this);

        //获取系统的日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountingDate = year + "年" + month + "月" + day + "日";

        initSpendSorts();
        initIncomeSorts();
        recyclerView = (RecyclerView)findViewById(R.id.calculator_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        calculatorSpendAdapter = new CalculatorAdapter(calculatorSpendSortItemList);
        calculatorIncomeAdapter = new CalculatorAdapter(calculatorIncomeSortItemList);
        recyclerView.setAdapter(calculatorSpendAdapter);

        textViewOutcome = (TextView)findViewById(R.id.tb_note_outcome);
        textViewIncome = (TextView)findViewById(R.id.tb_note_income);
        textViewOutcome.setOnClickListener(this);
        textViewIncome.setOnClickListener(this);
        textViewOutcome.setSelected(true);
        textViewIncome.setSelected(false);
        firstType = 1;


        calculatorIncomeAdapter.onClickListener(new CalculatorAdapter.onItemClick() {
            @Override
            public void onClick(View v,int i) {
//                Toast.makeText(Calculator.this,"点击了"+secondType,Toast.LENGTH_SHORT).show();
                CalculatorSortItem calculatorSortItem = calculatorIncomeSortItemList.get(i);
                secondType = calculatorSortItem.getImageId();
                editText.setHint(calculatorSortItem.getName());
                if(isEdit == false){
                    tip = calculatorSortItem.getName();
                }
            }
        });

        calculatorSpendAdapter.onClickListener(new CalculatorAdapter.onItemClick() {
            @Override
            public void onClick(View v,int i) {
//                Toast.makeText(Calculator.this,"点击了"+secondType,Toast.LENGTH_SHORT).show();
                CalculatorSortItem calculatorSortItem = calculatorSpendSortItemList.get(i);
                secondType = calculatorSortItem.getImageId();
                editText.setHint(calculatorSortItem.getName());
                if(isEdit == false){
                    tip = calculatorSortItem.getName();
                }
            }
        });


    }




    private void initSpendSorts(){
        calculatorSpendSortItemList.add(new CalculatorSortItem("餐饮",R.mipmap.sort_1_canyin));
        calculatorSpendSortItemList.add(new CalculatorSortItem("购物",R.mipmap.sort_1_gouwu));
        calculatorSpendSortItemList.add(new CalculatorSortItem("交通",R.mipmap.sort_1_jiaotong));
        calculatorSpendSortItemList.add(new CalculatorSortItem("零食",R.mipmap.sort_1_lingshi));
        calculatorSpendSortItemList.add(new CalculatorSortItem("美容",R.mipmap.sort_1_meirong));
        calculatorSpendSortItemList.add(new CalculatorSortItem("数码",R.mipmap.sort_1_shuma));
        calculatorSpendSortItemList.add(new CalculatorSortItem("通讯",R.mipmap.sort_1_tongxun));
        calculatorSpendSortItemList.add(new CalculatorSortItem("学习",R.mipmap.sort_1_xuexi));
        calculatorSpendSortItemList.add(new CalculatorSortItem("烟酒",R.mipmap.sort_1_yanjiu));
        calculatorSpendSortItemList.add(new CalculatorSortItem("娱乐",R.mipmap.sort_1_yule));
        calculatorSpendSortItemList.add(new CalculatorSortItem("办公",R.mipmap.sort_bangong));
        calculatorSpendSortItemList.add(new CalculatorSortItem("宠物",R.mipmap.sort_chongwu));
        calculatorSpendSortItemList.add(new CalculatorSortItem("孩子",R.mipmap.sort_haizi));
        calculatorSpendSortItemList.add(new CalculatorSortItem("还款",R.mipmap.sort_huankuan));
        calculatorSpendSortItemList.add(new CalculatorSortItem("兼职",R.mipmap.sort_jianzhi));
        calculatorSpendSortItemList.add(new CalculatorSortItem("捐赠",R.mipmap.sort_juanzeng));
        calculatorSpendSortItemList.add(new CalculatorSortItem("居家",R.mipmap.sort_jujia));
        calculatorSpendSortItemList.add(new CalculatorSortItem("零钱",R.mipmap.sort_lingqian));
        calculatorSpendSortItemList.add(new CalculatorSortItem("礼物",R.mipmap.sort_liwu));
        calculatorSpendSortItemList.add(new CalculatorSortItem("旅行",R.mipmap.sort_lvxing));
        calculatorSpendSortItemList.add(new CalculatorSortItem("手续费",R.mipmap.sort_shouxufei));
        calculatorSpendSortItemList.add(new CalculatorSortItem("水果",R.mipmap.sort_shuiguo));
        calculatorSpendSortItemList.add(new CalculatorSortItem("维修",R.mipmap.sort_weixiu));
        calculatorSpendSortItemList.add(new CalculatorSortItem("违约金",R.mipmap.sort_weiyuejin));
//        calculatorSpendSortItemList.add(new CalculatorSortItem("yiban",R.mipmap.sort_yiban));
        calculatorSpendSortItemList.add(new CalculatorSortItem("医疗",R.mipmap.sort_yiliao));
        calculatorSpendSortItemList.add(new CalculatorSortItem("佣金",R.mipmap.sort_yongjin));
        calculatorSpendSortItemList.add(new CalculatorSortItem("运动",R.mipmap.sort_yundong));
        calculatorSpendSortItemList.add(new CalculatorSortItem("住房",R.mipmap.sort_zhufang));
    }

    public void initIncomeSorts(){
        calculatorIncomeSortItemList.add(new CalculatorSortItem("返现",R.mipmap.income_fanxian));
        calculatorIncomeSortItemList.add(new CalculatorSortItem("分红",R.mipmap.income_fenhong));
        calculatorIncomeSortItemList.add(new CalculatorSortItem("工资",R.mipmap.income_gongzi));
        calculatorIncomeSortItemList.add(new CalculatorSortItem("奖金",R.mipmap.income_jiangjin));
        calculatorIncomeSortItemList.add(new CalculatorSortItem("礼金",R.mipmap.income_lijin));
        calculatorIncomeSortItemList.add(new CalculatorSortItem("利息",R.mipmap.income_lixi));
    }


//    @Override
//    public void onClick(View v, int i) {
//        Toast.makeText(Calculator.this,"6666",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_note_outcome:
                recyclerView.setAdapter(calculatorSpendAdapter);
                textViewOutcome.setSelected(true);
                textViewIncome.setSelected(false);
                firstType = 1;
                break;
            case R.id.tb_note_income:
                recyclerView.setAdapter(calculatorIncomeAdapter);
                textViewIncome.setSelected(true);
                textViewOutcome.setSelected(false);
                firstType = 2;
                break;
                default:
                    break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dp_date = String.format("%d月%d日",month+1,dayOfMonth);
        accountingDate = String.format("%d年%d月%d日",year,month+1,dayOfMonth);
        tv_date.setText(dp_date);
    }



    public void calculator_onClick(View view) {
//        Toast.makeText(this,"you clicked ",Toast.LENGTH_SHORT).;
        switch (view.getId()) {
            case R.id.back_btn:
                doBack();
                break;
//            case R.id.tb_note_income://收入
////                isOutcome = false;
////                setTitleStatus();
//                break;
//            case R.id.tb_note_outcome://支出
////                isOutcome = true;
////                setTitleStatus();
//                break;
//            case R.id.tb_note_cash://现金
//                showPayinfoSelector();
//                break;
            case R.id.tb_note_date://日期
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
//            case R.id.tb_note_remark://备注
//                showContentDialog();
//                break;
            case R.id.tb_calc_num_done://确定
                doCommit();
                break;
            case R.id.tb_calc_num_1:
                addDisplay("1");
                break;
            case R.id.tb_calc_num_2:
                addDisplay("2");
                break;
            case R.id.tb_calc_num_3:
                addDisplay("3");
                break;
            case R.id.tb_calc_num_4:
                addDisplay("4");
                break;
            case R.id.tb_calc_num_5:
                addDisplay("5");
                break;
            case R.id.tb_calc_num_6:
                addDisplay("6");
                break;
            case R.id.tb_calc_num_7:
                addDisplay("7");
                break;
            case R.id.tb_calc_num_8:
                addDisplay("8");
                break;
            case R.id.tb_calc_num_9:
                addDisplay("9");
                break;
            case R.id.tb_calc_num_0:
                addDisplay("0");
                break;
            case R.id.tb_calc_num_dot:
                addDisplay(".");
                break;
            case R.id.tb_note_clear://清空
                deleteEmptyDisplay();
                break;
            case R.id.tb_calc_num_del://删除
                deleteBackDisplay();
                break;
            case R.id.calculator_recycle_view:
                if(firstType == 1){
                    secondType = calculatorSpendAdapter.intentPosition;
                    Toast.makeText(Calculator.this,"fitsttype_1",Toast.LENGTH_SHORT).show();
                    editText.setHint(calculatorSpendAdapter.intentText);
                }else if(firstType == 2){
                    secondType = calculatorIncomeAdapter.intentPosition;
                    Toast.makeText(Calculator.this,"fitsttype_2",Toast.LENGTH_SHORT).show();
                    editText.setHint(calculatorIncomeAdapter.intentText);
                }
                break;
        }
    }

    //小键盘数字键和小数点添加功能
    public void addDisplay(String i){
        //在0.00时操作
        if (display.equals("0.00")){
            //第一次就输入小数点
            if(i.equals(".")){
                display = "0.";
                haveFloat = true;
                tb_note_money.setText(display);
                //第一次输入数字
            }else {
                display = i;
                tb_note_money.setText(display);
            }
            //在不为0.00时操作
        }else {
            if(display.length()<=10) {
                if(i.equals(".")){
                    if(haveFloat == false){
                        display = display + i;
                        haveFloat = true;
                        tb_note_money.setText(display);
                    }
                }else {
                    display = display + i;
                    tb_note_money.setText(display);
                }
            }else {
                Toast.makeText(Calculator.this,"数额不合理",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //退格删除
    public void deleteBackDisplay(){
        if(display.equals("")){
            tb_note_money.setText(display);
        }else {
            if(display.equals("0.00")){
                tb_note_money.setText(display);
            }else{
                display = display.substring(0,display.length()-1);
                tb_note_money.setText(display);
            }
        }
    }

    public void deleteEmptyDisplay(){
        display = "0.00";
        tb_note_money.setText(display);
    }

    public void doCommit() {
        if(tip.equals("hhh")){
            if (firstType == 1){
                tip = "支出";
            }else{
                tip = "收入";
            }
        }

//        Toast.makeText(Calculator.this,accountingDate,Toast.LENGTH_SHORT).show();
        String tempString = editText.getText().toString();
        if(tempString.equals(null)){
        }
        else {
            if(tempString.equals("")){}
            else {
                tip = tempString;
            }
        }


        moneyNumber = Double.parseDouble(display);

        if(firstType==1){
            moneyNumber = -moneyNumber;
        }

        Intent intent = new Intent();
        intent.putExtra("firstType",firstType);
        intent.putExtra("secondType",secondType);
        intent.putExtra("moneyCard",moneyCard);//未完成
        intent.putExtra("moneyNumber",moneyNumber);
        intent.putExtra("accountingDate",accountingDate);
        intent.putExtra("tip",tip);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void doBack(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }



//    @Override
//    public void onClick(int position) {
//        Toast.makeText(Calculator.this,position+"11111",Toast.LENGTH_SHORT).show();
//    }


//    private void selectItem(int posotion){
//        Log.d("CalculatorAdapter","1111111");
//        Toast.makeText(Calculator.this,"000",Toast.LENGTH_SHORT).show();
//        if(firstType == 1){
//            CalculatorSortItem calculatorOutputItem = calculatorSpendSortItemList.get(posotion);
//            editText.setHint(calculatorOutputItem.getName());
//        }else if(firstType == 2){
//            CalculatorSortItem calculatorInputItem = calculatorIncomeSortItemList.get(posotion);
//            editText.setHint(calculatorInputItem.getName());
//        }
//    }
//
//    @Override
//    public void select(View view, int position) {
//        Log.d("CalculatorAdapter","22222222222");
//        calculatorIncomeAdapter.setSelectItem(new CalculatorAdapter.SelectItem() {
//            @Override
//            public void select(View view, int position) {
//                selectItem(position); //对数据进行操作的函数，自己定义的
//            }
//        });
//    }

//    private void selectPosotion(){
//        if(firstType == 1){
//            for (int i = 0; i<calculatorSpendSortItemList.size() ; i++){
//
//                if(calculatorSpendAdapter.getItemViewType(i).){
//
//                }
//            }
//        }else if(firstType == 2){
//            for(int i = 0;i<calculatorIncomeSortItemList.size();i++){
//
//            }
//        }
//    }


}

