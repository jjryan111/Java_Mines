/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mines;

import javafx.scene.control.Button;

/**
 *
 * @author admin13
 */
public class MyBtn extends Button{
    private final int BUTTONID;
    private String txt;
    public MyBtn(int buttonID){
        BUTTONID = buttonID;
    }
    
    public int getButtonID(){
        return BUTTONID;
    }
    public void setTxt(String txt){
        this.txt = txt;
    }
    @Override
    public String toString(){
        return txt;
    }
}
