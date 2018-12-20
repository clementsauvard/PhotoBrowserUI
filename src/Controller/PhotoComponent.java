//Made by Clément SAUVARD

package Controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;

import Model.DrawAnnotations;
import Model.TextAnnotations;

public class PhotoComponent extends JComponent implements MouseListener, MouseMotionListener, KeyListener{

    private BufferedImage image;
    private Boolean photoLoaded;
    private Boolean isFlipped;
    private Boolean isDrawing;
    private Boolean isTyping;
    private Boolean outOfImage;
    private Cursor currentCursor;
    private Color colorDraw;
    private int fontSize;
    private String font;
    private int thickness;
    private int indexText;
    private int indexPath;
    ArrayList<DrawAnnotations> annotations;
    ArrayList<TextAnnotations> annotationsText;
    private int checkSelection;
    private boolean drawSelected;
    private int xOffset;
    private int yOffset;

    public PhotoComponent() {
    	setSize(new Dimension(1024,768));
        setPreferredSize(new Dimension(1024,768));
        
        image = null;
        photoLoaded = false;
        isFlipped = false;
        isDrawing = false;
        isTyping = false;
        outOfImage = false;
        fontSize = 32;
        font = "Arial";
        colorDraw = Color.black;
        thickness = 5;
        indexText = 0;
        indexPath = 0;
        checkSelection = -1;
        
        annotations = new ArrayList<DrawAnnotations>();
        annotationsText = new ArrayList<TextAnnotations>();
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        
        addKeyListener(this);
        setFocusable(true);
        
    }

    //Loading the image
    public void newPhoto(String file) {
    	try {                
            image = ImageIO.read(new File(file));
            setSize(new Dimension(image.getWidth(),image.getHeight()));
            setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
         } catch (IOException ex) {
              // handle exception...
         }
    	photoLoaded = true;
    	revalidate();
    }
    
    //---------------------------------------- Event listeners ------------------------------------------//
    
    //Draw on the drawAnnotation part + handle annotations movements
    public void mouseDragged(MouseEvent e) { 
    	if (isFlipped) {
    		// detect if its out of the annotation area
    		if (e.getX() > image.getWidth(this) || e.getY() > image.getHeight(this) 
    				|| e.getX() < 0 || e.getY() < 0 ) {
    			if (isDrawing) {
    				isDrawing = false;
    				outOfImage = true;
    				indexPath++;
    			}
    		}
    		else {
    			if (isDrawing) {
					if(isTyping) {
        				isTyping = false;
        				indexText++;
        			}
					//Add line in the GeneralPath
		    		annotations.get(indexPath).getPath().lineTo(e.getX(), e.getY());
		        	repaint();
    			}
    			//Moving annotations, handle if its a text that is selected or a drawing
    			else if (checkSelection != -1) {
    				if (drawSelected) {
    					annotations.get(checkSelection).path = 
    							new GeneralPath (AffineTransform.getTranslateInstance(e.getX()-xOffset,e.getY()-yOffset)
    	    					  .createTransformedShape(annotations.get(checkSelection).getPath()));
    					xOffset = e.getX();
    					yOffset = e.getY();
    				}
    				else {
    					annotationsText.get(checkSelection).setxPos(annotationsText.get(checkSelection).getxPos()+(e.getX()-xOffset));
    					annotationsText.get(checkSelection).setyPos(annotationsText.get(checkSelection).getyPos()+(e.getY()-yOffset));
    					xOffset = e.getX();
    					yOffset = e.getY();
    				}
					
					repaint();
				}
    			//If we are still in drag mode and out of the image and we re enter the image it starts a new drawing annotation
    			//at the location when we re enter the image
    			else if(outOfImage) {
    				annotations.add(new DrawAnnotations(thickness, colorDraw, new GeneralPath()));
    	        	annotations.get(indexPath).getPath().moveTo(e.getX(), e.getY());
    	        	isDrawing = true;
    	        	outOfImage = false;
    	        	repaint();
    			}
    		}
    	}
    }  
   
    // Mouse pressed for selecting and starting a drawAnnotation
    public void mousePressed(MouseEvent e) {
    	if (isFlipped) {
    		//Check if we are out of the image
    		if (e.getX() > image.getWidth(this) || e.getY() > image.getHeight(this)
    				|| e.getX() < 0 || e.getY() < 0 ) {
    			System.out.println("Out of border");
    		}
    		else {
    			// Check if we clicked on an annotation
    			checkSelection = checkComponent(e);
    			// if no annotation has been clicked then it starts a new drawing annotation
    			if (checkSelection == -1) {
    				annotations.add(new DrawAnnotations(thickness, colorDraw, new GeneralPath()));
    	        	annotations.get(indexPath).getPath().moveTo(e.getX(), e.getY());
    	        	isDrawing = true;
    	        	repaint();
    			}
    			// saving offset in case of an annotation selected (used when moving the selection)
    			else {
    				xOffset = e.getX();
    				yOffset = e.getY();
    			}
    		}
    	}
    }  
   
    public void mouseReleased(MouseEvent e) {
    	if (isFlipped && isDrawing) {
    		isDrawing = false;
    		indexPath++;
    	}
    }  
   
    public void mouseMoved(MouseEvent e) { 
    	if(isFlipped) {
    		currentCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        	setCursor(currentCursor);
    	}
    	else {
    		currentCursor = new Cursor(Cursor.HAND_CURSOR);
        	setCursor(currentCursor);
    	}
    }  
   
    public void mouseEntered(MouseEvent e) {  }  
   
    public void mouseExited(MouseEvent e) {  }  
   
    //If there is two mouse click, enter annotation mode, if there is one click, user can input text on the annotation area
    //at the pointer position
    public void mouseClicked(MouseEvent e) {
    	
    	if (isFlipped && e.getClickCount()==1) {	 
    		if(isTyping) {
    			//Removes possible empty text annotation due to multiple successive click
    			if (annotationsText.get(indexText).getText() == ""){
    				annotationsText.remove(indexText);
    				indexText--;
    			}
    			indexText++;
    			//text cursor to show that we are about to write a text
    			currentCursor = new Cursor(Cursor.TEXT_CURSOR);
            	setCursor(currentCursor);
            	annotationsText.add(new TextAnnotations("",e.getX(),e.getY(), font, fontSize, colorDraw));
            	isTyping = true;
            	isDrawing = false;
            	requestFocusInWindow();
    		}
    		else if (!isTyping){
    			//text cursor to show that we are about to write a text
	    		currentCursor = new Cursor(Cursor.TEXT_CURSOR);
	        	setCursor(currentCursor);
	        	annotationsText.add(new TextAnnotations("",e.getX(),e.getY(), font, fontSize, colorDraw));
	        	isTyping = true;
	        	isDrawing = false;
	        	requestFocusInWindow();
    		}
    	}
    	if(e.getClickCount()==2){
            isFlipped = !isFlipped;
            //text cursor to show that we are about to write a text
            if(isFlipped) {
        		currentCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
            	setCursor(currentCursor);
        	}
        	else {
        		currentCursor = new Cursor(Cursor.HAND_CURSOR);
            	setCursor(currentCursor);
        	}
            repaint();
        }
    }  
    
    //Writing notes on annotation area
    public void keyPressed(KeyEvent e) {
		
		if(isTyping && !isDrawing) {
			annotationsText.get(indexText).setText(Character.toString(e.getKeyChar()));
			annotationsText.get(indexText).strToChar();
			repaint();
		}
	}

	public void keyReleased(KeyEvent e) {  }
	public void keyTyped(KeyEvent e) {  }
    
	// ---------------------------------------------------------------------------------------//
	
	// paintComponent, where image and annotations are drawed
    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D)gg;
        
        //Anti aliasing for better strokes quality
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Background color
        g.setColor(Color.lightGray);
        g.fillRect(0,0,getWidth(),getHeight());
     
        //If photo loaded, we draw the image, if its in annotation mode we show all the annotations
        if (photoLoaded) {
        	if (!isFlipped) {
		        g.drawImage(image, 0,0, this); 
	        }
        	else {
        		g.drawImage(image, 0,0, this);
        		annotationsArea(g);
        	}
        }    
    }

    // Drawing of the annotations
	public void annotationsArea (Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Drawing of strokes, every strokes stored in annotations has its own color and thickness
		if(!annotations.isEmpty()) {
			for (DrawAnnotations paths: annotations) {
				//If an annotation is selected, it will draw a rectangle around the shape
				if (paths.isSelected) {
					g.setColor(Color.lightGray);
					g.setStroke(new BasicStroke(2));
					g.draw(paths.getPath().getBounds2D());
				}
				//Set color and thickness and draw the annotations
				g.setColor(paths.getColor());
				g.setStroke(new BasicStroke(paths.getThickness()));
				g.draw(paths.getPath());
			}	
		}
		//Drawing of text annotations
		if(!annotationsText.isEmpty()) {	
			try {
				for (TextAnnotations text: annotationsText) {
					
					
					//If an annotation is selected, it will draw a rectangle around the text
					if (text.isSelected) {
						int width;
						int height;
						if(text.sautL != 0) {
							width = text.totalW / text.sautL;
							height = text.metrics.getHeight()*(text.sautL+1);
						}
						else {
							width = text.totalW;
							height = text.metrics.getHeight();
						}
						g.setColor(Color.lightGray);
						g.setStroke(new BasicStroke(2));
						g.drawRect(text.getxPos(), text.getyPos() - (text.metrics.getHeight()-(text.metrics.getHeight()/10)) , width, height);
					}
					
					
					//Setting color and font for the text
					g.setColor(text.colorText);
					g.setFont(new Font(text.font, Font.PLAIN, text.fontSize));
				
					text.metrics = g.getFontMetrics();
					int sautLigne = 0;
					int width = 0;
					int totalWidth = 0;
					//Here I make sure that a text can't go out of the annotation area, if it does, it break the line (if it reached the right side)
					//if it reached the bottom part, it just stop showing additional character
					for (char ch: text.getChars()) {
						if (width + g.getFontMetrics().charWidth(ch) > image.getWidth(this) - text.getxPos()) {
							sautLigne++;
							width=0;
						}
						if (text.getyPos() + g.getFontMetrics().getHeight()*sautLigne < image.getHeight(this)) {
							g.drawString(String.valueOf(ch),text.getxPos()+width, text.getyPos() + g.getFontMetrics().getHeight()*sautLigne);
							width +=g.getFontMetrics().charWidth(ch);
							totalWidth +=g.getFontMetrics().charWidth(ch);
						}
						
					}
					text.sautL = sautLigne;
					text.totalW = totalWidth;
				}	
			}catch (NullPointerException e) {
            // handle exception...
			}
		}
	}
	
	public void setThickness (int thick) {
		thickness = thick;
	}
	public void setColor (Color col) {
		colorDraw = col;
	}
	
	public void setFontSize (int fs) {
		fontSize = fs;
	}
	
	public void setFontText (String f) {
		font = f;
	}
	
	// Popup color chooser. Set the color in general or change the color of the selected annotation
	public void showColorChooser() {
		if (checkSelection != -1) {
			if (drawSelected) {
				Color initialColor = Color.red;
			    Color newColor = JColorChooser.showDialog(null, "Dialog Title", initialColor);
			    annotations.get(checkSelection).setColor(newColor);
			}
			else {
				Color initialColor = Color.red;
			    Color newColor = JColorChooser.showDialog(null, "Dialog Title", initialColor);
			    annotationsText.get(checkSelection).setColorText(newColor);
			}
			
		    repaint();
		}
		else {
			Color initialColor = Color.red;
		    Color newColor = JColorChooser.showDialog(null, "Dialog Title", initialColor);
	        setColor(newColor);
		}
	}
	
	// Check if we clicked on an annotation, whether its a drawing or a text, then returns the index in the arrayList
	// of either drawAnnotations or TextAnnotations, then we can keep track of the selection
	public int checkComponent(MouseEvent e) {
		int selected = -1;
		if(!annotations.isEmpty()) {
			for (int i = 0; i < annotations.size();i++) {
				annotations.get(i).isSelected = false;
				//Check if the cursor is inside the rectangle area of the GeneralPath shape
				if(annotations.get(i).getPath().getBounds2D().contains(e.getX(),e.getY())) {
					selected = i;
					drawSelected = true;
					annotations.get(selected).isSelected = true; 
				}
			}
		}
		// Here we check if we click on a text area, and in case there has been a line break, it detects if we click on any of the line
		// not only the first one
		if(!annotationsText.isEmpty()) {
			for (int i = 0; i < annotationsText.size();i++) {
				int width;
				int height = annotationsText.get(i).sautL;
				if(annotationsText.get(i).sautL != 0) {
					width = annotationsText.get(i).totalW / annotationsText.get(i).sautL;
				}
				else {
					width = annotationsText.get(i).totalW;
				}
				
				annotationsText.get(i).isSelected = false;
				
				if(e.getX() > annotationsText.get(i).getxPos() &&
						e.getX() < annotationsText.get(i).getxPos() + width &&
						e.getY() > annotationsText.get(i).getyPos() - annotationsText.get(i).getMetrics().getHeight() &&
						e.getY() < annotationsText.get(i).getyPos() + annotationsText.get(i).getMetrics().getHeight()*height)
				{
					selected = i;
					drawSelected = false;
					annotationsText.get(selected).isSelected = true;
				}
			}
		}
		
		//we make sure that only one annotation is selected, the good one. 
		for (int i = 0; i < annotationsText.size();i++) {
			if (!drawSelected) {
				if (i != selected) {
					annotationsText.get(i).isSelected = false;
				}
			}
			else {
				annotationsText.get(i).isSelected = false;
			}
		}
		for (int i = 0; i < annotations.size();i++) {
			if (drawSelected) {
				if (i != selected) {
					annotations.get(i).isSelected = false;
				}
			}
			else {
				annotations.get(i).isSelected = false;
			}
		}
		repaint();
		return selected;
		
	}
}