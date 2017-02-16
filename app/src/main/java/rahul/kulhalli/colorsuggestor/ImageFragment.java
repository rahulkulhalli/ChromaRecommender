package rahul.kulhalli.colorsuggestor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageFragment extends Fragment {

    private ImageView detected;
    private ImageView recommended;
    private ImageView analog_1;
    private ImageView analog_2;
    private ImageView triad_1;
    private ImageView triad_2;

    private float[] shiftHueByX(int X, float[] HSL){
        //H is already in degrees. No need for conversion
        //first, shift H by X
        float hue = HSL[0];

        hue += X;

        if(hue > 360){
            hue -= 360;
        }

        return new float[]{hue, HSL[1], HSL[2]};
    }

    private void convertRGBtoHSV(int color){

        float[] hsl = new float[3];

        ColorUtils.RGBToHSL(Color.red(color), Color.green(color), Color.blue(color), hsl);

        //hsv[0] = h (0->360), hsv[1] = s (0->1), hsv[2] = v

        float[] shifted_hsl = shiftHueByX(180, hsl);

        //convert back to RGB
        int new_color = ColorUtils.HSLToColor(shifted_hsl);

        Log.d("COLOR_RECOMMENDED :: ", Color.red(new_color)+", "+Color.green(new_color)+", "+Color.blue(new_color));
        recommended.setBackgroundColor(new_color);
    }

    private List<Integer[]> getTriadicColors(int color){
        List<Integer[]> list = new ArrayList<>();

        //extract r,g,b
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        //triad :: r,g,b ; b,r,g ; g,b,r

        list.add(new Integer[]{r,g,b});
        list.add(new Integer[]{b,r,g});
        list.add(new Integer[]{g,b,r});

        return list;
    }

    private int[] getAnalogColors(int color){

        /*
        shift hue by 30.2 degree, meaning
        original color = [H,S,L] X,Y,Z
        prev color = [H-30.2, S, L] X1, Y1, Z1
        next color = [H+30.2, S, L] X2, Y2, Z2
        */

        //extract r,g,b
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        float[] now = new float[3];

        ColorUtils.RGBToHSL(red,green,blue,now);

        float H = now[0];

        //prev
        float H_prev = H;
        H_prev -= 30.2;
        if(H_prev < 0){
            H_prev += 360;
        }

        //next
        float H_next = H;
        H_next += 30.2;
        if(H_next > 360){
            H_next -= 360;
        }

        int prev_color = ColorUtils.HSLToColor(new float[]{H_prev, now[1], now[2]});
        int next_color = ColorUtils.HSLToColor(new float[]{H_next, now[1], now[2]});

        return new int[]{prev_color, color, next_color};
    }

    public void updateColor(int color){
        if(detected != null){
            Log.d("COLOR_DETECTED :: ", Color.red(color)+", "+Color.green(color)+", "+Color.blue(color));
            detected.setBackgroundColor(color);
            //call method to get complementary color
            convertRGBtoHSV(color);

            //call method to get triads
            List<Integer[]> my_list = getTriadicColors(color);
            //0 -> original
            Log.d("COLOR_TRIAD_1 :: ", Arrays.toString(my_list.get(1)));
            triad_1.setBackgroundColor(Color.rgb(my_list.get(1)[0], my_list.get(1)[1], my_list.get(1)[2]));

            Log.d("COLOR_TRIAD_2 :: ", Arrays.toString(my_list.get(2)));
            triad_2.setBackgroundColor(Color.rgb(my_list.get(2)[0], my_list.get(2)[1], my_list.get(2)[2]));

            //get analog colors
            int[] analog_colors = getAnalogColors(color);
            Log.d("COLOR_ANALOG_1 :: ", Color.red(analog_colors[0])+", "+Color.green(analog_colors[0])+", "+Color.blue(analog_colors[0]));
            analog_1.setBackgroundColor(analog_colors[0]);
            //1 -> original
            Log.d("COLOR_ANALOG_1 :: ", Color.red(analog_colors[2])+", "+Color.green(analog_colors[2])+", "+Color.blue(analog_colors[2]));
            analog_2.setBackgroundColor(analog_colors[2]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.image_fragment, container, false);

        detected = (ImageView) view.findViewById(R.id.d_c);
        recommended = (ImageView) view.findViewById(R.id.r_c);
        analog_1 = (ImageView) view.findViewById(R.id.analog_1);
        analog_2 = (ImageView) view.findViewById(R.id.analog_2);
        triad_1 = (ImageView) view.findViewById(R.id.triad_1);
        triad_2 = (ImageView) view.findViewById(R.id.triad_2);

        return view;
    }
}
