package brostore.maquillage.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import brostore.maquillage.R;
import brostore.maquillage.manager.DataManager;

public class FontTextView extends TextView {

    private float letterSpacing;
    private int maxLines;
    private CharSequence originalText = "";
    private String fontName;
    private String fontNameSelected;

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        initTextView(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTextView(context, attrs);
    }

    public FontTextView(Context context, String fontName) {
        super(context);
        if (isInEditMode()) {
            return;
        }
        this.fontName = fontName;
        init(fontName);
    }

    private void initTextView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView, 0, 0);
        fontName = a.getString(R.styleable.FontTextView_ttfNameTextView);
        fontNameSelected = a.getString(R.styleable.FontTextView_ttfNameTextViewSelected);
        letterSpacing = a.getFloat(R.styleable.FontTextView_letterSpacing, LetterSpacing.NORMAL);
        final float shadowRadius = a.getDimension(R.styleable.FontTextView_shadowRadius, 0f);
        final float shadowDx = a.getDimension(R.styleable.FontTextView_shadowDx, 0f);
        final float shadowDy = a.getDimension(R.styleable.FontTextView_shadowDy, 0f);
        final int shadowColor = a.getColor(R.styleable.FontTextView_shadowColor, 0);
        if (shadowColor != 0) {
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        } else {
            getPaint().clearShadowLayer();
        }
        a.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.maxLines});
        maxLines = array.getInt(0, -1);
        array.recycle();

        init(fontName);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && letterSpacing != LetterSpacing.NORMAL) {
            setText(getText());
        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (fontNameSelected != null) {
            if (selected) {
                init(fontNameSelected);
            } else {
                init(fontName);
            }
        }
        super.setSelected(selected);
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    private void init(String fontName) {
        setTypeface(DataManager.getInstance(getContext()).getFont(fontName));
    }

    public void refreshFont(String fontName) {
        setTypeface(DataManager.getInstance(getContext()).getFont(fontName));
    }

    private void applyLetterSpacing() {
        if (letterSpacing == LetterSpacing.NORMAL) {
            super.setText(originalText, BufferType.NORMAL);
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < originalText.length(); i++) {
                builder.append(originalText.charAt(i));
                if (i + 1 < originalText.length()) {
                    builder.append("\u00A0");
                }
            }
            SpannableString finalText = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (int i = 1; i < builder.toString().length(); i += 2) {
                    finalText.setSpan(new ScaleXSpan((letterSpacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            super.setText(finalText, BufferType.SPANNABLE);
        }
    }

    public void setLetterSpacing(float letterSpacing) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            originalText = (originalText == null ? "" : originalText);
            this.letterSpacing = letterSpacing;
            applyLetterSpacing();
        } else {
            super.setLetterSpacing(letterSpacing);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            originalText = (text == null ? "" : text);
            applyLetterSpacing();
        } else {
            super.setText(text, type);
        }
    }

    public class LetterSpacing {
        public final static float NORMAL = 0f;
    }
}