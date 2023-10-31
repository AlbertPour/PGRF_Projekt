package rasterize;

import java.awt.event.KeyListener;

public class FilledLineRasterizer extends LineRasterizer {

    private boolean dotStyle = false;
    public FilledLineRasterizer(Raster raster) {
        super(raster);
    }
    //DDA (Digital Differential Analyzer) - Výhody: Rychlý, jednoduchý a univerzální.
    //DDA (Digital Differential Analyzer) - Nevýhody: ztrácí přesnost při zaokrouhlování, má problém při velkém sklonu úsečky.


    @Override
    protected void drawLine(int x1, int y1, int x2, int y2) {

        int dx = x2 - x1;
        int dy = y2 - y1;

        float x = x1;
        float y = y1;

        // Určení počtu kroků podle většího rozdílu (x nebo y)
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        // Kreslení úsečky
        for (int i = 0; i <= steps; i++) {
            int roundedX = Math.round(x);
            int roundedY = Math.round(y);

            // Nakreslení bodů (roundedX, roundedY)
            raster.setPixel(roundedX, roundedY, 0xff0000);

            // Aktualizace pozice
            x += xIncrement;
            y += yIncrement;
        }
    }

    @Override
    protected void dotDrawLine(int x1, int y1, int x2, int y2) {

        int dx = x2 - x1;
        int dy = y2 - y1;

        float x = x1;
        float y = y1;

        // Velikost Segmentu
        int segmentLength = 20;

        // Určení počtu kroků podle většího rozdílu (x nebo y)
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        boolean drawPixel = true;

        // Kreslení úsečky
        for (int i = 0; i <= steps; i++) {
            int roundedX = Math.round(x);
            int roundedY = Math.round(y);

           // Nakreslení bodů (roundedX, roundedY)
           if (drawPixel) {
                raster.setPixel(roundedX, roundedY, 0xff0000);
            }


            // Pokud jsme dosáhli délky segmentu, resetujte proměnnou pro vykreslení.
            if (i % segmentLength == 0) {
                drawPixel = !drawPixel;
            }

            // Aktualizace pozice
            x += xIncrement;
            y += yIncrement;
        }
    }
}
