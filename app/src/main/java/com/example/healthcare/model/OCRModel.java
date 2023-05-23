package com.example.healthcare.model;

import java.io.Serializable;

public class OCRModel implements Serializable {
    //변수 선언
    private String Abdominal_fat_percentage;
    private String BMI;
    private String muscle;
    private String basal_metabolic;
    private String protein;
    private String kidney;
    private String weight;
    private String body_water;
    private String body_fat;




    public OCRModel(){}
    public String getAbdominal_fat_percentage() {
        return Abdominal_fat_percentage;
    }

    public void setAbdominal_fat_percentage(String Abdominal_fat_percentage) {
        this.Abdominal_fat_percentage = Abdominal_fat_percentage;
    }

    //여기서부터 get,set 함수를 사용하는데 이부분을 통해 값을 가져옴
    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getmuscle() {
        return muscle;
    }

    public void setmuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getbasal_metabolic() {
        return basal_metabolic;
    }

    public void setbasal_metabolic(String basal_metabolic) {
        this.basal_metabolic = basal_metabolic;
    }

    public String getprotein() {
        return protein;
    }

    public void setprotein(String protein) {
        this.protein = protein;
    }

    public String getkidney() {
        return kidney;
    }

    public void setkidney(String kidney) {
        this.kidney = kidney;
    }

    public String getweight() {
        return weight;
    }

    public void setweight(String weight) {
        this.weight = weight;
    }

    public String getbody_water() {
        return body_water;
    }

    public void setbody_water(String body_water) {
        this.body_water = body_water;
    }

    public String getbody_fat() {
        return body_fat;
    }

    public void setbody_fat(String body_fat) {
        this.body_fat = body_fat;
    }


    //이거는 그룹을 생성할때 사용하는 부분
    public OCRModel(String BMI, String muscle, String basal_metabolic, String protein, String kidney, String weight, String body_water, String body_fat) {
        this.BMI = BMI;
        this.setBMI(BMI);
        this.muscle = muscle;
        this.setmuscle(muscle);
        this.basal_metabolic = basal_metabolic;
        this.protein = protein;
        this.kidney = kidney;
        this.setkidney(kidney);
        this.weight = weight;
        this.body_water = body_water;
        this.body_fat = body_fat;
    }
}