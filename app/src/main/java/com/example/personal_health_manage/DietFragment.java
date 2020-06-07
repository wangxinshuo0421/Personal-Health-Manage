package com.example.personal_health_manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DietFragment extends Fragment {
    private List<Food> foodList = new ArrayList<>();
    private TextView bodyIndexText;

    String heightStr,weightStr,highBloodPressureStr,lowBloodPressureStr,bloodSugarStr,textToShow;
    int height,weight,highBloodPressure,lowBloodPressure,bodyIndex;
    float bloodSugar,bmiOfBody;

    /*食物清单*/
    Food banana = new Food("香蕉",R.drawable.diet_banana_icon,"热量","93","千卡(每100克)");
    Food apple = new Food("苹果",R.drawable.diet_apple_icon,"热量","53","千卡(每100克)");
    Food tomato = new Food("西红柿",R.drawable.diet_tomato_icon,"热量","15","千卡(每100克)");
    Food pineapple = new Food("菠萝",R.drawable.diet_pineapple_icon,"热量","44","千卡(每100克)");
    Food grape = new Food("葡萄",R.drawable.diet_grape_icon,"热量","45","千卡(每100克)");
    Food strawberry = new Food("草莓",R.drawable.diet_strawberry_icon,"热量","32","千卡(每100克)");
    Food cherry = new Food("樱桃",R.drawable.diet_cherry_icon,"热量","46","千卡(每100克)");
    Food pear = new Food("梨",R.drawable.diet_pear_icon,"热量","51","千卡(每100克)");
    Food watermalon = new Food("西瓜",R.drawable.diet_watermelon_icon,"热量","31","千卡(每100克)");
    Food mango = new Food("芒果",R.drawable.diet_mango_icon,"热量","35","千卡(每100克)");
    Food hazelnut = new Food("榛子",R.drawable.diet_hazelnut_icon,"热量","611","千卡(每100克)");
    Food potato = new Food("土豆（煮）",R.drawable.diet_potato_icon,"热量","65","千卡(每100克)");
    Food chineseYam = new Food("山药",R.drawable.diet_chinese_yam_icon,"热量","57","千卡(每100克)");
    Food carrot = new Food("胡萝卜",R.drawable.diet_carrot_icon,"热量","39","千卡(每100克)");
    Food corn = new Food("玉米",R.drawable.diet_corn_icon,"热量","112","千卡(每100克)");
    Food cucumber = new Food("黄瓜",R.drawable.diet_cucumber_icon,"热量","16","千卡(每100克)");
    Food eggplant = new Food("茄子",R.drawable.diet_eggplant_icon,"热量","23","千卡(每100克)");
    Food pepper = new Food("辣椒",R.drawable.diet_pepper_icon,"热量","38","千卡(每100克)");
    Food mushroom = new Food("蘑菇",R.drawable.diet_mushroom_icon,"热量","24","千卡(每100克)");
    Food pumpkin = new Food("南瓜",R.drawable.diet_pumpkin_icon,"热量","23","千卡(每100克)");
    Food cauliflower = new Food("花菜",R.drawable.diet_cauliflower_icon,"热量","20","千卡(每100克)");
    Food chineseCabbage = new Food("白菜",R.drawable.diet_chinese_cabbage_icon,"热量","20","千卡(每100克)");
    Food rice = new Food("米饭",R.drawable.diet_rice_icon,"热量","116","千卡(每100克)");
    Food steamedBuns= new Food("馒头",R.drawable.diet_steamed_buns_icon,"热量","223","千卡(每100克)");
    Food bread = new Food("面包",R.drawable.diet_bread_icon,"热量","313","千卡(每100克)");
    Food noodle = new Food("面条",R.drawable.diet_noodle_icon,"热量","301","千卡(每100克)");
    Food youtiao = new Food("油条",R.drawable.diet_youtiao_icon,"热量","388 ","千卡(每100克)");
    Food cake = new Food("蛋糕",R.drawable.diet_cake_icon,"热量","379","千卡(每100克)");
    Food eggTart = new Food("蛋挞",R.drawable.diet_egg_tart_icon,"热量","93","千卡(每100克)");
    Food shrimp = new Food("虾",R.drawable.diet_shrimp_icon,"热量","92","千卡(每100克)");
    Food carp = new Food("鲤鱼",R.drawable.diet_carp_icon,"热量","109","千卡(每100克)");
    Food pomfret = new Food("鲳鱼",R.drawable.diet_pomfret_icon,"热量","140","千卡(每100克)");
    Food pork = new Food("猪肉",R.drawable.diet_pork_icon,"热量","395","千卡(每100克)");
    Food chicken = new Food("鸡肉",R.drawable.diet_chicken_icon,"热量","131","千卡(每100克)");
    Food beef = new Food("牛肉",R.drawable.diet_beef_icon,"热量","106","千卡(每100克)");
    Food mutton = new Food("羊肉",R.drawable.diet_mutton_icon,"热量","203","千卡(每100克)");
    Food egg = new Food("鸡蛋",R.drawable.diet_egg_icon,"热量","147","千卡(每100克)");
    Food redWine = new Food("红酒",R.drawable.diet_red_wine_icon,"热量","96","千卡(每100克)");
    Food mike = new Food("牛奶",R.drawable.diet_mike_icon,"热量","54","千卡(每100克)");
    Food greenTea = new Food("绿茶",R.drawable.diet_green_tea_icon,"热量","16","千卡(每100克)");
    Food porridge = new Food("白粥",R.drawable.diet_porridge_icon,"热量","59","千卡(每100克)");
    Food oatmeal = new Food("燕麦粥",R.drawable.diet_oatmeal_icon,"热量","68","千卡(每100克)");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dietLayout = inflater.inflate(R.layout.diet_layout, container, false);
        return dietLayout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        bodyIndexText = (TextView)getActivity().findViewById(R.id.body_index);

        bodyIndex = judgeBodyIndex();
        textToShow = getBmiOfBody(height,weight);
        /**/

        switch (bodyIndex){
            case 0://0----》用户数据不全
                textToShow = textToShow+"输入血压血糖数据有误，请重新输入！！";
                bodyIndexText.setText(textToShow);
                break;
            case 1://1----》正常血压   正常血糖
                textToShow = textToShow + "您的血压和血糖为正常水平，以下为推荐饮食~";
                initFoodNormalNormal();
                break;
            case 2://2----》正常血压   低血糖
                textToShow = textToShow + "您的血压为正常水平，血糖偏低，以下为推荐饮食~";
                initFoodNormalLow();
                break;
            case 3://3----》正常血压   高血糖
                textToShow = textToShow + "您的血压为正常水平，血糖偏高，以下为推荐饮食~";
                initFoodNormalHigh();
                break;
            case 4://4----》高血压    正常血糖
                textToShow = textToShow + "您的血压偏高，血糖为正常水平，以下为推荐饮食~";
                initFoodHighNormal();
                break;
            case 5://5----》高血压    低血糖
                textToShow = textToShow + "您的血压偏高，血糖偏低，以下为推荐饮食~";
                initFoodHighLow();
                break;
            case 6://6----》高血压    高血糖
                textToShow = textToShow + "您的血压偏高，血糖偏高，以下为推荐饮食~";
                initFoodHighHigh();
                break;
            case 7://7----》低血压    正常血糖
                textToShow = textToShow + "您的血压偏低，血糖为正常水平，以下为推荐饮食~";
                initFoodLowNormal();
                break;
            case 8://8----》低血压    低血糖
                textToShow = textToShow + "您的血压偏低，血糖偏低，以下为推荐饮食~";
                initFoodLowLow();
                break;
            case 9://9----》低血压    高血糖
                textToShow = textToShow + "您的血压偏低，血糖偏高，以下为推荐饮食~";
                initFoodLowHigh();
                break;
            case 10://10---》用户数据为坏
                textToShow = textToShow + "根据血压血糖数据无法为您推荐推荐饮食，请更新数据!";
                break;
            default:break;
        }
        bodyIndexText.setText(textToShow);
        FoodAdapter adapter = new FoodAdapter(getActivity(),R.layout.food_item,foodList);
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }
    private void initFoodNormalNormal(){  //正常血压 正常血糖
        foodList.clear();
        foodList.add(apple);        foodList.add(banana);       foodList.add(tomato);           foodList.add(pineapple);
        foodList.add(shrimp);       foodList.add(chineseYam);   foodList.add(chineseCabbage);   foodList.add(chicken);
        foodList.add(grape);        foodList.add(carrot);       foodList.add(rice);             foodList.add(beef);
        foodList.add(strawberry);   foodList.add(corn);         foodList.add(steamedBuns);      foodList.add(mutton);
        foodList.add(cherry);       foodList.add(cucumber);     foodList.add(bread);            foodList.add(redWine);
        foodList.add(pear);         foodList.add(eggplant);     foodList.add(noodle);           foodList.add(mike);
        foodList.add(watermalon);   foodList.add(pepper);       foodList.add(youtiao);          foodList.add(greenTea);
        foodList.add(mango);        foodList.add(mushroom);     foodList.add(cake);             foodList.add(porridge);
        foodList.add(hazelnut);     foodList.add(pumpkin);      foodList.add(pomfret);          foodList.add(oatmeal);
        foodList.add(potato);       foodList.add(cauliflower);  foodList.add(pork);             foodList.add(carp);
        foodList.add(egg);
    }
    private void initFoodNormalLow(){  //正常血压 低血糖
        foodList.clear();
        /*低血糖食物*/
        foodList.add(oatmeal);  foodList.add(eggTart);  foodList.add(cake);
        foodList.add(pepper);   foodList.add(redWine);  foodList.add(egg);
        foodList.add(mike);     foodList.add(shrimp);   foodList.add(carp);
        foodList.add(apple);    foodList.add(pear);     foodList.add(beef);
        foodList.add(cauliflower);                      foodList.add(carrot);
        foodList.add(cucumber); foodList.add(corn);     foodList.add(watermalon);
        foodList.add(cherry);   foodList.add(pomfret);
    }
    private void initFoodNormalHigh(){  //正常血压 高血糖
        foodList.clear();
        foodList.add(oatmeal);  foodList.add(carrot);   foodList.add(eggplant);
        foodList.add(mushroom); foodList.add(cauliflower);
        foodList.add(chineseCabbage);                   foodList.add(beef);
        foodList.add(potato);   foodList.add(chicken);  foodList.add(pumpkin);
        foodList.add(chineseYam);

    }
    private void initFoodHighNormal(){  //高血压 正常血糖
        foodList.clear();
        foodList.add(greenTea); foodList.add(apple);   foodList.add(carrot);
        foodList.add(mike);     foodList.add(corn);     foodList.add(pomfret);
        foodList.add(potato);   foodList.add(shrimp);   foodList.add(eggplant);
        foodList.add(tomato);   foodList.add(pear);     foodList.add(pineapple);
        foodList.add(banana);
    }
    private void initFoodHighLow(){  //高血压 低血糖
        foodList.clear();
        foodList.add(oatmeal);  foodList.add(cauliflower);  foodList.add(mike);
        foodList.add(carrot);   foodList.add(chineseCabbage);
    }
    private void initFoodHighHigh(){  //高血压 高血糖
        foodList.clear();
        foodList.add(greenTea); foodList.add(apple);    foodList.add(carrot);
        foodList.add(mike);     foodList.add(corn);     foodList.add(pomfret);
        foodList.add(potato);   foodList.add(shrimp);   foodList.add(eggplant);
        foodList.add(tomato);   foodList.add(pear);     foodList.add(pineapple);
        foodList.add(banana);   foodList.add(oatmeal);  foodList.add(mushroom);
        foodList.add(cauliflower);                      foodList.add(chineseCabbage);
        foodList.add(beef);     foodList.add(chicken);  foodList.add(pumpkin);
        foodList.add(chineseYam);
    }
    private void initFoodLowNormal(){  //低血压 正常血糖
        foodList.clear();
        foodList.add(cherry);   foodList.add(apple);    foodList.add(mutton);
        foodList.add(chicken);  foodList.add(mushroom); foodList.add(carp);
        foodList.add(mike);     foodList.add(shrimp);   foodList.add(cake);
        foodList.add(pepper);   foodList.add(pomfret);  foodList.add(egg);
    }
    private void initFoodLowLow(){  //低血压 低血糖
        foodList.clear();
        foodList.add(pepper);   foodList.add(pomfret);  foodList.add(watermalon);
        foodList.add(cherry);   foodList.add(mushroom); foodList.add(carp);
        foodList.add(chicken);  foodList.add(apple);    foodList.add(mutton);
        foodList.add(mike);     foodList.add(shrimp);   foodList.add(cake);
        foodList.add(pumpkin);  foodList.add(egg);
    }
    private void initFoodLowHigh(){  //低血压 高血糖
        foodList.clear();
        foodList.add(oatmeal);  foodList.add(shrimp);   foodList.add(egg);
        foodList.add(pomfret);  foodList.add(carp);
    }

    /*
    *首先读取数据，根据数据判断用户的身体状态
    * 0----》用户数据不全
    * 1----》正常血压   正常血糖
    * 2----》正常血压   低血糖
    * 3----》正常血压   高血糖
    * 4----》高血压    正常血糖
    * 5----》高血压    低血糖
    * 6----》高血压    高血糖
    * 7----》低血压    正常血糖
    * 8----》低血压    低血糖
    * 9----》低血压    高血糖
    * 10---》用户数据为坏
    *  */
    private int judgeBodyIndex(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        heightStr = sharedPref.getString("heightStr","");
        weightStr = sharedPref.getString("weightStr","");
        highBloodPressureStr = sharedPref.getString("highBloodPressureStr","");
        lowBloodPressureStr  = sharedPref.getString("lowBloodPressureStr","");
        bloodSugarStr = sharedPref.getString("bloodSugarStr","");
        height = strToInt(heightStr);
        weight = strToInt(weightStr);
        bloodSugar = strToFloat(bloodSugarStr);
        highBloodPressure = strToInt(highBloodPressureStr);
        lowBloodPressure = strToInt(lowBloodPressureStr);
        if(height==0||weight==0||bloodSugar==0||highBloodPressure==0||lowBloodPressure==0){
            return 0;//用户数据不全
        }
        else if((highBloodPressure>90&&highBloodPressure<140)&&(lowBloodPressure>60&&lowBloodPressure<90)
                &&(bloodSugar>3.9&&bloodSugar<6.1)){
            return 1;//正常血压 正常血糖
        }
        else if((highBloodPressure>90&&highBloodPressure<140)&&(lowBloodPressure>60&&lowBloodPressure<90)
                &&(bloodSugar>1&&bloodSugar<=3.9)){
            return 2;//正常血压   低血糖
        }
        else if((highBloodPressure>90&&highBloodPressure<140)&&(lowBloodPressure>60&&lowBloodPressure<90)
                &&(bloodSugar>=6.1&&bloodSugar<11)){
            return 3;//正常血压   高血糖
        }
        else if((highBloodPressure>=140)&&(lowBloodPressure>=90&&lowBloodPressure<140)
                &&(bloodSugar>3.9&&bloodSugar<6.1)){
            return 4;//高血压    正常血糖
        }
        else if((highBloodPressure>=140)&&(lowBloodPressure>=90&&lowBloodPressure<140)
                &&(bloodSugar>1&&bloodSugar<=3.9)){
            return 5;//高血压    低血糖
        }
        else if((highBloodPressure>=140)&&(lowBloodPressure>=90&&lowBloodPressure<140)
                &&(bloodSugar>=6.1&&bloodSugar<11)){
            return 6;//高血压    高血糖
        }
        else if((highBloodPressure>60&&highBloodPressure<=90)&&(lowBloodPressure>30&&lowBloodPressure<=60)
                &&(bloodSugar>3.9&&bloodSugar<6.1)){
            return 7;//低血压    正常血糖
        }
        else if((highBloodPressure>60&&highBloodPressure<=90)&&(lowBloodPressure>30&&lowBloodPressure<=60)
                &&(bloodSugar>1&&bloodSugar<=3.9)){
            return 8;//低血压    低血糖
        }
        else if((highBloodPressure>60&&highBloodPressure<=90)&&(lowBloodPressure>30&&lowBloodPressure<=60)
                &&(bloodSugar>=6.1&&bloodSugar<11)){
            return 9;//低血压    高血糖
        }
        else {
            return 10;//坏数据
        }
    }

    private String getBmiOfBody(int h,int w){
        float ans;
        String str=null;
        if(h<=0||w<=0){
            return "输入的身高体重数据有误，无法计算您的身体质量指数!\n";
        }
        else {
            ans = (float)h/(float)100.0;
            ans = ans*ans;
            ans = (float)w/ans;
            if(ans>1&&ans<18.5){
                str = String.format("您的BMI身体质量指数为: %.2f 偏瘦 可适量增重%n",ans);
            }
            else if(ans>=18.5&&ans<25){
                str = String.format("您的BMI身体质量指数为: %.2f 正常体重 请保持%n",ans);
            }
            else if(ans>=25&&ans<30){
                str = String.format("您的BMI身体质量指数为: %.2f 偏胖 可适量减重%n",ans);
            }
            else if(ans>=30&&ans<35){
                str = String.format("您的BMI身体质量指数为: %.2f 肥胖 请减重%n",ans);
            }
            else if(ans>=35&&ans<40){
                str = String.format("您的BMI身体质量指数为: %.2f 重度肥胖 请减肥！\n",ans);
            }
            else if(ans>=40){
                str = String.format("您的BMI身体质量指数为: %.2f 极重度肥胖 请减肥！！\n",ans);
            }
            else {
                str = "输入的身高体重数据有误，无法计算您的身体质量指数!\n";
            }
            return str;
        }
    }

    private int strToInt(String str){
        if (str == null|| str.trim().equals("")){
            return 0;
        }
        char[] chars = str.trim().toCharArray();
        int result =0;
        for (int i= 0;i<chars.length;i++){

            if (chars[i]>'9'||chars[i]<'0'){
                return 0;
            }
            result = result *10 +(int)(chars[i]-'0');
        }
        return result;
    }

    private float strToFloat(String str){
        if (str == null|| str.trim().equals("")){
            return 0;
        }
        char[] chars = str.trim().toCharArray();
        int i,resultInt = 0,cnt = 1;
        float resultFloat = 0,result = 0;
        for(i = 0;i<chars.length;i++){
            if(chars[i]<='9'&&chars[i]>='0'){
                resultInt = resultInt*10+(int)(chars[i]-'0');
            }
            else if(chars[i]=='.'){
                break;
            }
            else {
                return 0;
            }
        }
        i++;
        for(;i<chars.length;i++){
            resultFloat = resultFloat+(float) Math.pow(0.1,cnt)*(int)(chars[i]-'0');
            cnt++;
        }
        result = resultFloat + resultInt;
        return result;
    }

}