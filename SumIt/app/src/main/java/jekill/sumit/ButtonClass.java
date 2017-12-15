package jekill.sumit;

import android.graphics.Bitmap;
import android.graphics.Rect;
public class ButtonClass {
    Bitmap buttonBitmap;
    int text;
    Rect coordinate;
    boolean selected;
    boolean answer;
    public ButtonClass(Bitmap bObject, int mText, Rect rect, boolean select, boolean answer){
    this.buttonBitmap= bObject;
    this.text=mText;
    this.coordinate=rect;
    this.selected=select;
    this.answer=answer;
    }

    public boolean getAnswer() {
        return answer;
    }

    public int getWrittenNumber() {
        return text;
    }

    public Bitmap getButtonBitmap() {return buttonBitmap;}

    public Rect getCoordinate() {
        return coordinate;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
