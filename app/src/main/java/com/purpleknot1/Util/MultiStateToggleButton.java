//package com.PurpleKnot.Util;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import com.PurpleKnot.Activity.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MultiStateToggleButton extends ToggleButton {
//
//    private static final String TAG = MultiStateToggleButton.class.getSimpleName();
//
//    private static final String KEY_BUTTON_STATES = "button_states";
//    private static final String KEY_INSTANCE_STATE = "instance_state";
//
//    List<View> buttons;
//    boolean mMultipleChoice = false;
//    private LinearLayout mainLayout;
//
//    public MultiStateToggleButton(Context context) {
//        super(context, null);
//        if (this.isInEditMode()) {
//            return;
//        }
//    }
//
//    public MultiStateToggleButton(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        if (this.isInEditMode()) {
//            return;
//        }
//        int[] set = {
//                android.R.attr.entries
//        };
//        TypedArray a = context.obtainStyledAttributes(attrs, set);
//        CharSequence[] texts = a.getTextArray(0);
//        a.recycle();
//
//        setElements(texts, null, new boolean[texts.length]);
//    }
//
//    /**
//     * If multiple choice is enabled, the user can select multiple
//     * toggle simultaneously.
//     *
//     * @param enable
//     */
//    public void enableMultipleChoice(boolean enable) {
//        this.mMultipleChoice = enable;
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
//        bundle.putBooleanArray(KEY_BUTTON_STATES, getStates());
//        return bundle;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        if (state instanceof Bundle) {
//            Bundle bundle = (Bundle) state;
//            setStates(bundle.getBooleanArray(KEY_BUTTON_STATES));
//            state = bundle.getParcelable(KEY_INSTANCE_STATE);
//        }
//        super.onRestoreInstanceState(state);
//    }
//
//    /**
//     * Set multiple buttons with the specified texts and default
//     * initial toggle. Initial states are allowed, but both
//     * arrays must be of the same size.
//     *
//     * @param texts            An array of CharSequences for the buttons
//     * @param imageResourceIds an optional icon to show, either text, icon or both needs to be set.
//     * @param selected         The default value for the buttons
//     */
//    public void setElements(CharSequence[] texts, int[] imageResourceIds, boolean[] selected) {
//        final int textCount = texts != null ? texts.length : 0;
//        final int iconCount = imageResourceIds != null ? imageResourceIds.length : 0;
//        final int elementCount = Math.max(textCount, iconCount);
//        if (elementCount == 0) {
//            throw new IllegalArgumentException("neither texts nor images are setup");
//        }
//
//        boolean enableDefaultSelection = true;
//        if (selected == null || elementCount != selected.length) {
//            Log.d(TAG, "Invalid selection array");
//            enableDefaultSelection = false;
//        }
//
//        setOrientation(LinearLayout.HORIZONTAL);
//        setGravity(Gravity.CENTER_VERTICAL);
//
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (mainLayout == null) {
//            mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
//        }
//        mainLayout.removeAllViews();
//
//        this.buttons = new ArrayList<>();
//        for (int i = 0; i < elementCount; i++) {
//            Button b = null;
//            if (i == 0) {
//                // Add a special view when there's only one element
//                if (elementCount == 1) {
//                    b = (Button) inflater.inflate(R.layout.view_single_toggle_button, mainLayout, false);
//                } else {
//                    b = (Button) inflater.inflate(R.layout.view_left_toggle_button, mainLayout, false);
//                }
//            } else if (i == elementCount - 1) {
//                b = (Button) inflater.inflate(R.layout.view_right_toggle_button, mainLayout, false);
//            } else {
//                b = (Button) inflater.inflate(R.layout.view_center_toggle_button, mainLayout, false);
//            }
//            b.setText(texts != null ? texts[i] : "");
//            if (imageResourceIds != null && imageResourceIds[i] != 0) {
//                b.setCompoundDrawablesWithIntrinsicBounds(imageResourceIds[i], 0, 0, 0);
//            }
//            final int position = i;
//            b.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    setValue(position);
//                }
//
//            });
//            mainLayout.addView(b);
//            if (enableDefaultSelection) {
//                setButtonState(b, selected[i]);
//            }
//            this.buttons.add(b);
//        }
//        mainLayout.setBackgroundResource(R.drawable.button_section_shape);
//    }
//
//
//    /**
//     * Set multiple buttons with the specified texts and default
//     * initial toggle. Initial states are allowed, but both
//     * arrays must be of the same size.
//     *
//     * @param buttons  the array of button views to use
//     * @param selected The default value for the buttons
//     */
//    public void setButtons(View[] buttons, boolean[] selected) {
//        final int elementCount = buttons.length;
//        if (elementCount == 0) {
//            throw new IllegalArgumentException("neither texts nor images are setup");
//        }
//
//        boolean enableDefaultSelection = true;
//        if (selected == null || elementCount != selected.length) {
//            Log.d(TAG, "Invalid selection array");
//            enableDefaultSelection = false;
//        }
//
//        setOrientation(LinearLayout.HORIZONTAL);
//        setGravity(Gravity.CENTER_VERTICAL);
//
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (mainLayout == null) {
//            mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
//        }
//        mainLayout.removeAllViews();
//
//        this.buttons = new ArrayList<>();
//        for (int i = 0; i < elementCount; i++) {
//            View b = buttons[i];
//            final int position = i;
//            b.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    setValue(position);
//                }
//
//            });
//            mainLayout.addView(b);
//            if (enableDefaultSelection) {
//                setButtonState(b, selected[i]);
//            }
//            this.buttons.add(b);
//        }
//        mainLayout.setBackgroundResource(R.drawable.button_section_shape);
//    }
//
//    public void setElements(CharSequence[] elements) {
//        int size = elements == null ? 0 : elements.length;
//        setElements(elements, null, new boolean[size]);
//    }
//
//    public void setElements(List<?> elements) {
//        int size = elements == null ? 0 : elements.size();
//        setElements(elements, new boolean[size]);
//    }
//
//    public void setElements(List<?> elements, Object selected) {
//        int size = 0;
//        int index = -1;
//        if (elements != null) {
//            size = elements.size();
//            index = elements.indexOf(selected);
//        }
//        boolean[] selectedArray = new boolean[size];
//        if (index != -1 && index < size) {
//            selectedArray[index] = true;
//        }
//        setElements(elements, new boolean[size]);
//    }
//
//    public void setElements(List<?> texts, boolean[] selected) {
//        int size = texts == null ? 0 : texts.size();
//        setElements(texts.toArray(new String[size]), null, selected);
//    }
//
//    public void setElements(int arrayResourceId, int selectedPosition) {
//        // Get resources
//        String[] elements = this.getResources().getStringArray(arrayResourceId);
//
//        // Set selected boolean array
//        int size = elements == null ? 0 : elements.length;
//        boolean[] selected = new boolean[size];
//        if (selectedPosition >= 0 && selectedPosition < size) {
//            selected[selectedPosition] = true;
//        }
//
//        // Super
//        setElements(elements, null, selected);
//    }
//
//    public void setElements(int arrayResourceId, boolean[] selected) {
//        setElements(this.getResources().getStringArray(arrayResourceId), null, selected);
//    }
//
//    public void setButtonState(View button, boolean selected) {
//        if (button == null) {
//            return;
//        }
//        button.setSelected(selected);
//        // TODO: Inherit these colors from primary/secondary colors
//        if (selected) {
//            button.setBackgroundResource(R.drawable.button_pressed);
//        } else {
//            button.setBackgroundResource(R.drawable.button_not_pressed);
//        }
//        if (button instanceof Button) {
//            ((Button) button).setTextAppearance(this.context, selected ? R.style.WhiteBoldText : R.style.PrimaryNormalText);
//        }
//    }
//
//    public int getValue() {
//        for (int i = 0; i < this.buttons.size(); i++) {
//            if (buttons.get(i).isSelected()) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public void setValue(int position) {
//        for (int i = 0; i < this.buttons.size(); i++) {
//            if (mMultipleChoice) {
//                if (i == position) {
//                    View b = buttons.get(i);
//                    if (b != null) {
//                        setButtonState(b, !b.isSelected());
//                    }
//                }
//            } else {
//                if (i == position) {
//                    setButtonState(buttons.get(i), true);
//                } else if (!mMultipleChoice) {
//                    setButtonState(buttons.get(i), false);
//                }
//            }
//        }
//        super.setValue(position);
//    }
//
//    public boolean[] getStates() {
//        int size = this.buttons == null ? 0 : this.buttons.size();
//        boolean[] result = new boolean[size];
//        for (int i = 0; i < size; i++) {
//            result[i] = this.buttons.get(i).isSelected();
//        }
//        return result;
//    }
//
//    public void setStates(boolean[] selected) {
//        if (this.buttons == null || selected == null ||
//                this.buttons.size() != selected.length) {
//            return;
//        }
//        int count = 0;
//        for (View b : this.buttons) {
//            setButtonState(b, selected[count]);
//            count++;
//        }
//    }
//}
