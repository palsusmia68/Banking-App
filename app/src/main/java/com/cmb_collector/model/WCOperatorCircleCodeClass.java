package com.cmb_collector.model;

import java.util.ArrayList;
import java.util.List;

public class WCOperatorCircleCodeClass {

    List<String> list_operator = new ArrayList<>();
    List<String> list_circle = new ArrayList<>();

    public WCOperatorCircleCodeClass() {
        list_operator.add("V");
        list_operator.add("A");
        list_operator.add("RC");
        list_operator.add("I");
        list_operator.add("BS");
        list_operator.add("D");
        list_operator.add("M");

        list_circle.add("41");
        list_circle.add("13");
        list_circle.add("41");
        list_circle.add("24");
        list_circle.add("17");
        list_circle.add("43");
        list_circle.add("27");
        list_circle.add("44");
        list_circle.add("45");
        list_circle.add("46");
        list_circle.add("12");
        list_circle.add("20");
        list_circle.add("21");
        list_circle.add("25");
        list_circle.add("22");
        list_circle.add("9");
        list_circle.add("14");
        list_circle.add("47");
        list_circle.add("16");
        list_circle.add("4");
        list_circle.add("48");
        list_circle.add("49");
        list_circle.add("50");
        list_circle.add("51");
        list_circle.add("52");
        list_circle.add("23");
        list_circle.add("53");
        list_circle.add("1");
        list_circle.add("18");
        list_circle.add("54");
        list_circle.add("8");
        list_circle.add("55");
        list_circle.add("10");
        list_circle.add("56");
        list_circle.add("2");
        list_circle.add("11");
        list_circle.add("3");
        list_circle.add("5");
        list_circle.add("7");
        list_circle.add("26");
        list_circle.add("6");
    }

    public String getOperatorCodeList(int position) {
        return list_operator.get(position);
    }

    public String getCircleCodeList(int position) {
        return list_circle.get(position);
    }
}
