package cn.feng.aluminium.util.data.image;

import lombok.Getter;

@Getter
public class RGB {
    private int red;

    private int green;

    private int blue;

    public RGB() {
    }

    public RGB(int red, int green, int blue) {
        setRed(red);
        setBlue(blue);
        setGreen(green);
    }

    public void setRed(int red) {
        if (red < 0) {
            this.red = 0;
        } else this.red = Math.min(red, 255);
    }

    public void setGreen(int green) {
        if (green < 0) {
            this.green = 0;
        } else this.green = Math.min(green, 255);
    }

    public void setBlue(int blue) {
        if (blue < 0) {
            this.blue = 0;
        } else this.blue = Math.min(blue, 255);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RGB)) return false;
        RGB theRGB = (RGB) obj;
        return this.getRed() == theRGB.getRed() && this.getGreen() == theRGB.getGreen() && this.getBlue() == theRGB.getBlue();
    }

    @Override
    public int hashCode() {
        return this.getRed() * 1000000 + this.getGreen() * 1000 + this.getBlue();
    }

    public String toString() {
        return "RGB {" + this.red + ", " + this.green + ", " + this.blue + "}";
    }
}
