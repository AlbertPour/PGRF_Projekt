package fill;

import rasterize.Raster;
import rasterize.RasterBufferedImage;

import java.awt.*;

public class SeedFiller implements Filler{
    private Raster raster;
    private int backGroundColor;
    private  int x,y;

    public SeedFiller(Raster raster, int backGroundColor, int x, int y){
        this.raster = raster;
        this.backGroundColor = backGroundColor;
        this.x = x;
        this.y = y;
    }
    @Override
    public void fill() {
        seedFill(x, y);
    }

    private void seedFill(int x, int y){
        //alg - zde rekurze
        //1. načtu barvu pixelu na souřadnici x, y
        int pixelColor = raster.getPixel(x,y); //backGroundColor = -1; !!! pixelColor = -16777216;

        //2. podmínka: pokud barva pozadí se rovná načtené -> obarvuji
        if (pixelColor != backGroundColor){
            return;
        }
        //3. obarvim
        raster.setPixel(x,y, 0xff000);
        //4. zavolám 4x pro sousedy
        seedFill(x+1,y);
        seedFill(x-1,y);
        seedFill(x,y+1);
        seedFill(x,y-1);
    }
}
