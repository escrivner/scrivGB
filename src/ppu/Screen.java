package ppu;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import addressBus.Motherboard;

public class Screen extends JFrame{
    
    private Motherboard bus;
    private Canvas canvas;
    private BufferedImage originalScreen;
    private BufferedImage scaledScreen;
    private int screenScaleValue = 5;

    public Screen(Motherboard bus){
        
        originalScreen = new BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB);
        scaledScreen = new BufferedImage(160 * screenScaleValue, 144 * screenScaleValue, originalScreen.getType());
        
        for (int y = 0; y < originalScreen.getHeight(); y++)
        {
            for (int x = 0; x < originalScreen.getWidth(); x++)
            {
                Color color = (y % 2 == 0) ? Color.GRAY : Color.LIGHT_GRAY;
                int colorValue = color.getRGB();
                originalScreen.setRGB(x, y, colorValue);
  
            }
        }

        ImageIcon icon = new ImageIcon( scaledScreen );
        add( new JLabel(icon) );
        drawScaledImage();
    }

    public BufferedImage getBufferedImage(){
        return originalScreen;
    }

    public void drawScaledImage(){

        Graphics2D g = scaledScreen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(originalScreen, 0, 0, scaledScreen.getWidth(), scaledScreen.getHeight(), 0, 0, originalScreen.getWidth(), originalScreen.getHeight(), null);
        g.dispose();
    }

    public void intializeScreen(){
        JFrame frame = this;
        frame.setTitle("scrivGB");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }
}
