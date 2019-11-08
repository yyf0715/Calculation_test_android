package com.example.calculation_test_android;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calculation_test_android.databinding.FragmentQuestionBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {


    public QuestionFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final MyViewModel myViewModel;
        myViewModel = ViewModelProviders.of(requireActivity(),new SavedStateVMFactory(requireActivity())).get(MyViewModel.class);
        myViewModel.generator();//加载题目
        final FragmentQuestionBinding binding;
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_question,container,false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(requireActivity());
        final StringBuilder stringBuilder = new StringBuilder();//StringBuilder创建一个可变的字符序列，与普通的String不同
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.button0:
                        stringBuilder.append(0);//追加0
                        break;
                    case R.id.button1:
                        stringBuilder.append(1);//追加1
                        break;
                    case R.id.button2:
                        stringBuilder.append(2);//追加2
                        break;
                    case R.id.button3:
                        stringBuilder.append(3);//追加3
                        break;
                    case R.id.button4:
                        stringBuilder.append(4);//追加4
                        break;
                    case R.id.button5:
                        stringBuilder.append(5);//追加5
                        break;
                    case R.id.button6:
                        stringBuilder.append(6);//追加6
                        break;
                    case R.id.button7:
                        stringBuilder.append(7);//追加7
                        break;
                    case R.id.button8:
                        stringBuilder.append(8);//追加8
                        break;
                    case R.id.button9:
                        stringBuilder.append(9);//追加9
                        break;
                    case R.id.buttonClear:
                        stringBuilder.setLength(0);//清除
//                        stringBuilder = new StringBuilder();
                        break;
                }
                if (stringBuilder.length() == 0){
                    binding.textView9.setText(getString(R.string.input_indicator));
                }else{
                    binding.textView9.setText(stringBuilder.toString());
                }
            }
        };
        binding.button0.setOnClickListener(listener);
        binding.button1.setOnClickListener(listener);
        binding.button2.setOnClickListener(listener);
        binding.button3.setOnClickListener(listener);
        binding.button4.setOnClickListener(listener);
        binding.button5.setOnClickListener(listener);
        binding.button6.setOnClickListener(listener);
        binding.button7.setOnClickListener(listener);
        binding.button8.setOnClickListener(listener);
        binding.button9.setOnClickListener(listener);
        binding.buttonClear.setOnClickListener(listener);
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringBuilder.length()==0){
                    //防止没有输入数值直接点击提交键返回一个null值导致程序崩溃
                    stringBuilder.append(-1);
                }
                if (Integer.valueOf(stringBuilder.toString()).intValue() == myViewModel.getAnswer().getValue()){
                    //计算正确
//                    stringBuilder.toString()如果输入直接点击提交键会返回一个null值，会导致程序崩溃
                    myViewModel.answerCorrent();
                    stringBuilder.setLength(0);
                    binding.textView9.setText(R.string.answer_correct_message);
                }else{
                    NavController controller = Navigation.findNavController(view);
                    if (myViewModel.win_flag){
                        controller.navigate(R.id.action_questionFragment_to_winFragment);
                        myViewModel.win_flag = false;
                        myViewModel.save();//有新纪录，保存
                    }else{
                        controller.navigate(R.id.action_questionFragment_to_loseFragment);
                    }
                }
            }
        });

        return binding.getRoot();
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_question, container, false);
    }

}
