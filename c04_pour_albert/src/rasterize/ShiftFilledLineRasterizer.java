package rasterize;

public class ShiftFilledLineRasterizer extends LineRasterizer {

    public ShiftFilledLineRasterizer(Raster raster) {
        super(raster);
    }

    //DDA (Digital Differential Analyzer) - Výhody: Rychlý, jednoduchý a univerzální.
    //DDA (Digital Differential Analyzer) - Nevýhody: ztrácí přesnost při zaokrouhlování, má problém při velkém sklonu úsečky.

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2) {
        //Velikost úsečky
        int dx = x2 - x1;
        int dy = y2 - y1;
        //Směr úsečky
        int directionX;
        int directionY;

        if (x2 > x1) {
            directionX = 1;
        } else {
            directionX = -1;
        }
        if (y2 > y1) {
            directionY = 1;
        } else {
            directionY = -1;
        }

        int steps = Math.max(dx, dy);
        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        int x = x1;
        int y = y1;

        for (int i = 0; i <= steps; i++) {
            int roundedX = x;
            int roundedY = y;
            raster.setPixel(roundedX, roundedY, 0xff0000);

            x += xIncrement * directionX;
            y += yIncrement * directionY;
        }
    }
}
