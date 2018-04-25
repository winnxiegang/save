package com.tencent.livesdk;

/**
 * Created by willguo on 16/11/1.
 */
public class ILVCustomCmd extends ILVText<ILVCustomCmd>{
    private int cmd=-1;

    public ILVCustomCmd(){}

    public ILVCustomCmd(ILVTextType type, String destId, String text, int cmd) {
        super(type, destId, text);
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }

    public ILVCustomCmd setCmd(int cmd) {
        this.cmd = cmd;
        return this;
    }

    public String getParam(){
        return super.getText();
    }

    public ILVCustomCmd setParam(String param){
        super.setText(param);
        return this;
    }
}
