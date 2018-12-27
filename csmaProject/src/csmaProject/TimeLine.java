package csmaProject;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class TimeLine extends Canvas
{
    static final int shift = 1;
    private Image offImage;
    private Dimension offDimension = this.getSize();
    
    public void paint(Graphics graphics) {
	update(graphics);
    }
    
    public void update(Color color, Color color_0_) {
	try {
	    int i = offDimension.width - 1;
	    int i_1_ = offDimension.height;
	    int i_2_ = (int) ((double) i_1_ * 0.1);
	    Graphics graphics = offImage.getGraphics();
	    Image image
		= this.createImage(offDimension.width, offDimension.height);
	    Graphics graphics_3_ = image.getGraphics();
	    graphics_3_.drawImage(offImage, 0, 0, this);
	    graphics.drawImage(image, 1, 0, i + 1, i_1_, 0, 0, i, i_1_, this);
	    graphics.setColor(color);
	    graphics.fillRect(0, i_2_, 1, 7 * i_2_);
	    graphics.setColor(color_0_);
	    graphics.fillRect(0, 9 * i_2_, 1, 9 * i_2_);
	    graphics.setColor(Color.black);
	    this.repaint();
	} catch (Exception exception) {
	    /* empty */
	}
    }
    
    public void update(Graphics graphics) {
	if (!offDimension.equals(this.getSize())) {
	    offDimension = this.getSize();
	    offImage
		= this.createImage(offDimension.width, offDimension.height);
	}
	graphics.drawImage(offImage, 0, 0, this);
    }
}
