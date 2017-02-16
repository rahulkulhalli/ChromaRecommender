package rahul.kulhalli.colorsuggestor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;

public class ImageFragment extends Fragment {

    private ImageView detected;
    private ImageView recommended;

    private float[] shiftHueBy180(float[] HSL){
        //H is already in degrees. No need for conversion
        //first, shift H by 180
        float hue = HSL[0];

        hue += 180;

        if(hue > 360){
            hue -= 360;
        }

        return new float[]{hue, HSL[1], HSL[2]};
    }

    private void convertRGBtoHSV(int color){

        float[] hsl = new float[3];

        ColorUtils.RGBToHSL(Color.red(color), Color.green(color), Color.blue(color), hsl);

        //hsv[0] = h (0->360), hsv[1] = s (0->1), hsv[2] = v

        float[] shifted_hsl = shiftHueBy180(hsl);

        //convert back to RGB
        int new_color = ColorUtils.HSLToColor(shifted_hsl);

        recommended.setBackgroundColor(new_color);
    }

    public void updateColor(int color){
        if(detected != null){
            detected.setBackgroundColor(color);
            convertRGBtoHSV(color);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.image_fragment, container, false);

        TextView label1 = (TextView) view.findViewById(R.id.d);
        TextView label2 = (TextView) view.findViewById(R.id.r);
        detected = (ImageView) view.findViewById(R.id.d_c);
        recommended = (ImageView) view.findViewById(R.id.r_c);

        return view;
    }
}
