package com.example.sogong.Control;

import com.example.sogong.Model.Ingredients;

import java.util.List;

public class ControlRefrigerator_f {
    List<Ingredients> refrigerator;
    public void lookupRefrigerator(String nickname){}
    public void addRefrigerator(String nickname, Ingredients ingredient){}
    public void deleteRefrigerator(String nickname, String name){}
    public void editRefrigerator(String nickname, Ingredients ingredient){}
    public void expireDateWarning(){}
    public void showRefrigerator(List<Ingredients> refrigerator){}
    public void noResult(String message){}//이거랑 control의 startToast랑 뭐가 다름?
}
