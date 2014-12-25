package com.jjh.view;

import com.jjh.view.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class StartTextEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	private String startText;
	private int startTextColor;
	private float textSize = 12;
	private int pLeft;
	private int paddingLeft;
	private int rectWidth;
	private Drawable clearDrawable;
	private boolean hasFoucs;
	private ITextChangeListener textChangeListener;

	public StartTextEditText(Context context) {   
        this(context, null);   
    }   
   
    public StartTextEditText(Context context, AttributeSet attrs) {   
        this(context, attrs, android.R.attr.editTextStyle);   
    }   
      
    public StartTextEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StartTextEditText, defStyle, 0);
        startText = a.getString(R.styleable.StartTextEditText_start_text);
        startTextColor = a.getColor(R.styleable.StartTextEditText_start_textColor, Color.BLACK);
        clearDrawable = a.getDrawable(R.styleable.StartTextEditText_clearDrawable);
        a.recycle();
        init();
        if(clearDrawable!=null){
        	clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());   
            //默认设置隐藏图标  
            setClearIconVisible(false);   
            //设置焦点改变的监听  
            setOnFocusChangeListener(this);   
            //设置输入框里面内容发生改变的监听  
            addTextChangedListener(this);  
        }
    }

	private void init() {
		textSize  = getTextSize();
		pLeft = getPaddingLeft();
		
		Paint pFont = new Paint(); 
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		//返回包围整个字符串的最小的一个Rect区域
		pFont.getTextBounds(startText, 0, startText.length(), rect); 
		int strwid = rect.width();
		
		paddingLeft = strwid + (3*pLeft);
		rectWidth = strwid + (2*pLeft);
	}  
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		setPadding(paddingLeft, 0, pLeft, 0);
		super.onLayout(changed, left, top, right, bottom);  
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int viewHeight = getHeight();
		
		Rect targetRect = new Rect(0, 0, rectWidth, viewHeight);  
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
	    paint.setStrokeWidth(3);  
	    paint.setTextSize(textSize);  
	    paint.setColor(Color.TRANSPARENT);  
	    canvas.drawRect(targetRect, paint);  
	    paint.setColor(startTextColor);  
	    FontMetricsInt fontMetrics = paint.getFontMetricsInt();  
	    int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
	    // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()  
	    paint.setTextAlign(Paint.Align.CENTER);  
	    canvas.drawText(startText, targetRect.centerX(), baseline, paint);

		super.onDraw(canvas);
	}
	
	/** 
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和 
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑 
     */  
    @Override   
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            if (getCompoundDrawables()[2] != null) {  
  
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())  
                        && (event.getX() < ((getWidth() - getPaddingRight())));  
                  
                if (touchable) {  
                    this.setText("");  
                }  
            }  
        }  
  
        return super.onTouchEvent(event);  
    }  
   
    /** 
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 
     */  
    @Override   
    public void onFocusChange(View v, boolean hasFocus) {   
        this.hasFoucs = hasFocus;  
        if (hasFocus) {   
            setClearIconVisible(getText().length() > 0);   
        } else {   
            setClearIconVisible(false);   
        }   
    }   
   
   
    /** 
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去 
     * @param visible 
     */  
    protected void setClearIconVisible(boolean visible) {   
        Drawable right = visible ? clearDrawable : null;   
        setCompoundDrawables(getCompoundDrawables()[0],   
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);   
    }   
       
      
    /** 
     * 当输入框里面内容发生变化的时候回调的方法 
     */  
    @Override   
    public void onTextChanged(CharSequence s, int start, int count,   
            int after) {   
	    if(hasFoucs){  
	        setClearIconVisible(s.length() > 0);  
	    }
	    if(textChangeListener != null) {
	    	textChangeListener.onTextChanged(s, start, count, after);
	    }
    }   
   
    @Override   
    public void beforeTextChanged(CharSequence s, int start, int count,   
            int after) {   
    	if(textChangeListener != null) {
	    	textChangeListener.beforeTextChanged(s, start, count, after);
	    }
    }   
   
    @Override   
    public void afterTextChanged(Editable s) {   
    	if(textChangeListener != null) {
	    	textChangeListener.afterTextChanged(s);
	    }
    }
    
    public interface ITextChangeListener {
    	void onTextChanged(CharSequence s, int start, int count,int after);
    	void beforeTextChanged(CharSequence s, int start, int count,int after);
    	void afterTextChanged(Editable s);
    }
    
    public void setTextChangeListener(ITextChangeListener l) {
    	textChangeListener = l;
    }
	
}
