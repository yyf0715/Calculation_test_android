package com.example.calculation_test_android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;


public class MyViewModel extends AndroidViewModel {
    SavedStateHandle handle;
    private static String KEY_HIGH_SCORE = "key_high_score";
    private static String KEY_LEFT_NUMBER = "key_left_number";
    private static String KEY_RIGHT_NUMBER = "key_right_number";
    private static String KEY_OPERATOR = "key_operator";
    private static String KEY_ANSWER = "key_answer";
    private static String SAVE_SHP_DATA_NAME = "save_shp_data_name";
    private static String KEY_CURRENT_SCORE = "key_current_score";
    public boolean win_flag = false;

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
//        使用SavedStateHandle handle需要添加依赖
//        在build.gradle里添加implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-alpha01'
        super(application);
        if (!handle.contains(KEY_HIGH_SCORE)) {
            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE, shp.getInt(KEY_HIGH_SCORE, 0));
            //读取SharedPreferences存储high_score值到handle中，没有high_score的话默认值为0

            //意味着需要将全部KEY值初始化
            handle.set(KEY_LEFT_NUMBER, 0);
            handle.set(KEY_OPERATOR, "+");
            handle.set(KEY_RIGHT_NUMBER, 0);
            handle.set(KEY_ANSWER, 0);
            handle.set(KEY_CURRENT_SCORE, 0);
        }
        this.handle = handle;
    }

    public MutableLiveData<Integer> getLeftNumber() {
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }

    public MutableLiveData<Integer> getRightNumber() {
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_ANSWER);
    }

    public void generator() {//生成题
        int LEVEL = 20;//难度  题目在多少位以内
        Random random = new Random();
        int x, y;
        x = random.nextInt(LEVEL) + 1;//random.nextInt(LEVEL)生成0到（LEVEL-1）范围的整数，涉及加法不需要0
        y = random.nextInt(LEVEL) + 1;
        if (x % 2 == 0) {//50%概率生成+或-
            getOperator().setValue("+");
            if (x > y) {                      //避免超过20以外的运算
                getAnswer().setValue(x);//大的数为x，则x为答案
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x - y);
            } else {
                getAnswer().setValue(y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y-x);
            }
        } else {
            getOperator().setValue("-");
            if (x>y){
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
                getAnswer().setValue(x-y);
            }else{
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
                getAnswer().setValue(y-x);
            }
        }
    }
    @SuppressWarnings("ConstantConditions")
    public void save(){
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE,getHighScore().getValue());
        editor.apply();
    }
    @SuppressWarnings("ConstantConditions")
    public void answerCorrent(){
        //答对当前分数+1
        getCurrentScore().setValue(getCurrentScore().getValue()+ 0b1);
        if (getCurrentScore().getValue()>getHighScore().getValue()){
            getHighScore().setValue(getCurrentScore().getValue());
            win_flag = true;
        }
        generator();
    }
}
