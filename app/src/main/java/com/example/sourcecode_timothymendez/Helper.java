package com.example.sourcecode_timothymendez;

public class Helper  {

    public Helper() {

    }


    public String renameGesture(String selectedAction) {
        switch (selectedAction) {
            case "0":
                return "Num0";
            case "1":
                return "Num1";
            case "2":
                return "Num2";
            case "3":
                return "Num3";
            case "4":
                return "Num4";
            case "5":
                return "Num5";
            case "6":
                return "Num6";
            case "7":
                return "Num7";
            case "8":
                return "Num8";
            case "9":
                return "Num9";
            case "Turn On Light":
                return "LightOn";
            case "Turn Off Light":
                return "LightOff";
            case "Turn On Fan":
                return "FanOn";
            case "Turn Off Fan":
                return "FanOff";
            case "Increase Fan Speed":
                return "FanUp";
            case "Decrease Fan Speed":
                return "FanDown";
            case "Set Thermostat to Specified Temperature":
                return "SetThermo";
            default:
                return "";
        }
    }

}
