package graphics.fx;

import game.Game;

import java.awt.*;


public class FadeEffect extends Effect {

    private final int fadeFrames;
    private boolean fadeIn;
    private int fadeFrame;

    public FadeEffect(boolean fadeIn, int seconds){
        this.fadeIn = fadeIn;
        fadeFrames = (int)(60 * seconds);
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        float fadeAmount = 0f;
        if(fadeIn){
            fadeFrame++;
            fadeAmount = 1 - (float)fadeFrame / fadeFrames;
            if(fadeFrame >= fadeFrames){
                stopEffect();
            }

        } else {
            fadeFrame++;
            fadeAmount = (float)fadeFrame / fadeFrames;
            if(fadeFrame >= fadeFrames){
                stopEffect();
            }
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAmount));
        Color tmpColor = g2d.getColor();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g2d.setColor(tmpColor);
    }
    
    
}
