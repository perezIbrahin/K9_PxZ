package Model;

import android.graphics.drawable.Drawable;

public class ModelBtnFreq {
    private String btnName;
    private String btnStatus;
    private Drawable menuDrawable;
    private Integer btnTextSize;

    public Drawable getMenuDrawable() {
        return menuDrawable;
    }

    public void setMenuDrawable(Drawable menuDrawable) {
        this.menuDrawable = menuDrawable;
    }

    public Integer getBtnTextSize() {
        return btnTextSize;
    }

    public void setBtnTextSize(Integer btnTextSize) {
        this.btnTextSize = btnTextSize;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getBtnStatus() {
        return btnStatus;
    }

    public void setBtnStatus(String btnStatus) {
        this.btnStatus = btnStatus;
    }
}
