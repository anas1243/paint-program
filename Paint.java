package paint1;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Paint extends Application
{
    ArrayList <Shape> list = new ArrayList<>();
    int history = -1;
    GraphicsContext gc ;
    double xBegin=0,yBegin=0,xEnd=0,yEnd=0;
    double width=0,height=0;

    public void start(Stage primaryStage) 
    {
        FileChooser fc = new FileChooser();
        Menu file = new Menu("File");
            file.setStyle("-fx-font-weight:bold");
        MenuItem newSketch = new MenuItem("New");
        MenuItem saveSketch = new MenuItem("Save");
        MenuItem exitSketch = new MenuItem("Exit");
            file.getItems().addAll(newSketch,saveSketch,exitSketch);
        Menu edit = new Menu("Edit");
            edit.setStyle("-fx-font-weight:bold");
            MenuItem undo = new MenuItem("Undo");
            MenuItem redo = new MenuItem("Redo");
                edit.getItems().addAll(undo,redo);
                    
        MenuBar menu = new MenuBar();
            menu.setStyle("-fx-background-color:gray");
                menu.getMenus().addAll(file,edit);
        
        HBox top = new HBox(20);
            top.getChildren().add(menu);
                top.setStyle("-fx-background-color:gray");
        
        VBox left = new VBox(10);
            left.setAlignment(Pos.CENTER_LEFT);
                left.setStyle("-fx-background-color:lightgray");
                    left.setPadding(new Insets(10,10,10,10));
        Button brush = new Button("Brush");
            brush.setMaxSize(80, 10);
        Button eraser = new Button("Eraser");
            eraser.setMaxSize(80, 10);
        Button polygon = new Button("Polygon");
            polygon.setMaxSize(80, 10);
        Button rectangle = new Button("Rectangle");
            rectangle.setMaxSize(80, 10);
        Button ellipse = new Button("Ellipse");
            ellipse.setMaxSize(80, 10);
        Button line = new Button("Line");
            line.setMaxSize(80, 10);
        Button image = new Button("Image");
            image.setMaxSize(80,10);
        
        ColorPicker pc = new ColorPicker(Color.BLACK);
            pc.setMaxSize(80, 25);
            pc.setOnAction(e->{
                gc.setStroke(pc.getValue());
            });              
        Slider sl = new Slider(1, 10, 5);
            sl.setMaxSize(80, 100);
                sl.setShowTickLabels(true);
                    sl.setShowTickMarks(true);
                    sl.setOnMouseDragged(e->{
                        gc.setLineWidth(sl.getValue());
                    });
        left.getChildren().addAll(brush,eraser,rectangle,polygon,ellipse,line,image,pc,sl);
        
        StackPane center = new StackPane();
            center.setStyle("-fx-background-color:AliceBlue");
        
        BorderPane root = new BorderPane();
            root.setTop(top);
                root.setCenter(center);
                    root.setLeft(left);
                    
        Canvas canvas = new Canvas(900,600);
            gc = canvas.getGraphicsContext2D();
                gc.setStroke(Color.BLACK);
                    gc.setLineWidth(5);;
        center.getChildren().add(canvas);
        
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("My Paint");
            primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                    primaryStage.show();
 
        center.setOnMousePressed(event1->{
            if(brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //brushWORKS
                gc.setStroke(pc.getValue());
                    gc.beginPath();
                        gc.lineTo(event1.getX(),event1.getY());
                            gc.stroke();
            }
            if(!brush.isFocused() && eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //eraserWORKS
                gc.setStroke(Color.ALICEBLUE);
                    gc.beginPath();
                        gc.lineTo(event1.getX(),event1.getY());
                            gc.stroke();
            }
            if(!brush.isFocused() && !eraser.isFocused() && line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //lineWORKS
                gc.setStroke(pc.getValue());
                gc.setLineWidth(sl.getValue());
                xBegin=event1.getX();   yBegin=event1.getY();
            }
            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //polygonWORKS
                xBegin = event1.getX();     yBegin = event1.getY();
            }
            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //rectangleWORKS
                xBegin = event1.getX();     yBegin = event1.getY();
            }
            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //ellipseWORKS      "Oval"
                xBegin = event1.getX();     yBegin = event1.getY();
            }
        });                                       
    
        center.setOnMouseDragged(event2->{
            if(brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //burshWORKS
                gc.lineTo(event2.getX(),event2.getY());
                    gc.stroke();
            }
            if(!brush.isFocused() && eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //eraserWORKS
                gc.lineTo(event2.getX(),event2.getY());
                    gc.stroke();
            }        
        });
    
        center.setOnMouseReleased(event3->{
            if(!brush.isFocused() && !eraser.isFocused() && line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //lineWORKS
                xEnd=event3.getX();  yEnd=event3.getY();
                MyLine myLine = new MyLine(xBegin,xEnd,yBegin,yEnd);
                    myLine.draw(gc);
                list.add(myLine);
                    history++;
            }
            
            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //rectangleWORKS
                gc.setStroke(pc.getValue());
                    xEnd = event3.getX();   yEnd = event3.getY();
                if(xEnd > xBegin && yEnd > yBegin)
                {
                    width = xEnd - xBegin;      height = yEnd - yBegin;
                    MyRect myRect = new MyRect(xBegin, yBegin, width, height);
                        myRect.draw(gc);
                    list.add(myRect);
                        history++;
                }
                if(xBegin > xEnd && yBegin < yEnd)
                {
                    width = xBegin - xEnd;      height = yEnd - yBegin;
                    MyRect myRect = new MyRect((xBegin-width), (yEnd-height), width, height);
                        myRect.draw(gc);
                    list.add(myRect);
                        history++;
                }
                if(xBegin < xEnd && yBegin > yEnd)
                {
                    width = xEnd - xBegin;      height = yBegin - yEnd;
                    MyRect myRect = new MyRect((xEnd-width), (yBegin-height), width, height);
                        myRect.draw(gc);
                    list.add(myRect);
                        history++;
                }
                if(xBegin > xEnd && yBegin > yEnd)
                {
                    width = xBegin - xEnd;      height = yBegin - yEnd;
                    MyRect myRect = new MyRect(xEnd, yEnd, width, height);
                        myRect.draw(gc);
                    list.add(myRect);
                        history++;
                }
            }

            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && !polygon.isFocused() && !rectangle.isFocused() && ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //ellipseWORKS  "Oval"
                gc.setStroke(pc.getValue());
                    xEnd = event3.getX();     yEnd = event3.getY();
                if(xBegin>xEnd && yBegin<yEnd)
                {
                    height = yEnd-yBegin;
                    width = xBegin-xEnd;
                    MyOval myOval = new MyOval(xEnd, yBegin, width, height);
                        myOval.draw(gc);
                    list.add(myOval);
                        history++;
                }
                if(xBegin<xEnd && yBegin<yEnd)
                {
                    height = yEnd-yBegin;
                    width = xEnd-xBegin;
                    MyOval myOval = new MyOval(xBegin, yBegin, width, height);
                        myOval.draw(gc);
                    list.add(myOval);
                        history++;
                }
                if(xBegin>xEnd && yBegin>yEnd)
                {
                    height = yBegin-yEnd;
                    width = xBegin-xEnd;
                    MyOval myOval = new MyOval(xEnd, yEnd, width, height);
                        myOval.draw(gc);
                    list.add(myOval);
                        history++;
                }
                if(xBegin<xEnd && yBegin>yEnd)
                {
                    height = yBegin-yEnd;
                    width = xEnd-xBegin;
                    MyOval myOval = new MyOval(xBegin, yEnd, width, height);
                        myOval.draw(gc);
                    list.add(myOval);
                        history++;
                }
            }
            if(!brush.isFocused() && !eraser.isFocused() && !line.isFocused() && polygon.isFocused() && !rectangle.isFocused() && !ellipse.isFocused() && !sl.isFocused() && !pc.isFocused() )
            {   //polygonWORKS
                xEnd = event3.getX();     yEnd = event3.getY();
                double x1=0,x2=0,x3=0,x4=0,x5=0;
                double y1=0,y2=0,y3=0,y4=0,y5=0;
                if(xBegin>xEnd && yBegin<yEnd)
                {
                    x1 = xBegin;    x2 = xEnd;  x3 = xEnd;  x4 = xBegin;
                    y1 = yBegin;    y2 = yEnd;  y3 = yBegin;    y4 = yEnd;
                    x5 = ((xBegin - xEnd)/2) + xEnd ;
                    y5 = yBegin - ((yEnd-yBegin)/2); 
                }
                if(xBegin<xEnd && yBegin<yEnd)
                {
                    x1 = xEnd;  x2 = xBegin;    x3 = xBegin;    x4 = xEnd;
                    y1 = yBegin;    y2 = yEnd;  y3 = yBegin;    y4 = yEnd;
                    x5 = ((xEnd-xBegin)/2) + xBegin;
                    y5 = yBegin - ((yEnd-yBegin)/2); 
                }
                if(xBegin>xEnd && yBegin>yEnd)
                {
                    x1 = xBegin;  x2 = xEnd;  x3 = xEnd;    x4 = xBegin;
                    y1 = yEnd;  y2 = yBegin;    y3 = yEnd;  y4 = yBegin;
                    x5 = ((xBegin-xEnd)/2) + xEnd;
                    y5 = yEnd - ((yBegin - yEnd)/2);
                }
                if(xBegin<xEnd && yBegin>yEnd)
                {
                    x1 = xEnd;  x2 = xBegin;    x3 = xBegin;  x4 = xEnd;
                    y1 = yEnd;  y2 = yBegin;    y3 = yEnd;  y4 = yBegin;
                    x5 = ((xEnd-xBegin)/2) + xBegin;
                    y5 = yEnd - ((yBegin-yEnd)/2);
                }
                double [] totalX = {x1,x4,x2,x3,x5};
                double [] totalY = {y1,y4,y2,y3,y5};
                gc.setStroke(pc.getValue());
                MyPoly myPoly = new MyPoly(totalX, totalY);
                    myPoly.draw(gc);
                list.add(myPoly);
                    history++;
            }
        });
        
        newSketch.setOnAction(newSketchEvent->{
                gc.clearRect(0, 0, 900.0, 600.0);
            });
        
        saveSketch.setOnAction(saveSketchEvent->{
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
                fc.setTitle("Save Sketch As");
            File filee = fc.showSaveDialog(primaryStage);
                if(filee != null){
                    WritableImage wi = new WritableImage(800, 600);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null,wi),null),"png",filee);
                        } catch (IOException ex) {
                            System.out.println("Saving error");
                        }
                    }
        });
        
        exitSketch.setOnAction(e->{
            Platform.exit();
        });
        
        image.setOnAction(imageEvent->{
            fc.setTitle("Select Image");
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fc.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
             
            File imgFile = fc.showOpenDialog(primaryStage);
                      
            try {
                BufferedImage bufferedImage = ImageIO.read(imgFile);
                Image myImage = SwingFXUtils.toFXImage(bufferedImage, null);
                //myImageView.setImage(myImage);
                gc.drawImage(myImage, 0, 0,canvas.getWidth(), canvas.getHeight());
            } catch (IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        undo.setOnAction(undoEvent->{
            if(history >= 0)
            {
                history--;
                gc.clearRect(0, 0, 900.0,600.0);
                for(int i=0 ; i<=history ;i++)
                {
                    list.get(i).draw(gc);
                }
            }
        });
        
        redo.setOnAction(redoEvent->{
            if(history >= 0)
            {
                history++ ;
                list.get(history).draw(gc);
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }    
}


abstract class Shape
{
    double x;
    double y;
    double xBegin;
    double xEnd;
    double yBegin;
    double yEnd;
    double height;
    double width;
    double []X;
    double []Y;
    public GraphicsContext gc;
    abstract void draw(GraphicsContext gc);
}

class MyLine extends Shape
{
    public MyLine (double x1 ,double x2 ,double y1 ,double y2)
    {
        xBegin = x1;    xEnd = x2;  yBegin = y1;    yEnd = y2;
    }
    public void draw(GraphicsContext gc) 
    {
        this.gc = gc;
        this.gc.strokeLine(xBegin,yBegin,xEnd,yEnd);
    }
}

class MyOval extends Shape
{
    public MyOval (double x ,double y ,double width ,double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    void draw(GraphicsContext gc) 
    {
        this.gc = gc;
        this.gc.strokeOval(x,y,width,height);
    }
}

class MyRect extends Shape
{
    public MyRect (double x ,double y ,double width ,double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void draw(GraphicsContext gc)
    {
        this.gc = gc;
        this.gc.strokeRect(x, y, width, height);
    }
}

class MyPoly extends Shape
{
    public MyPoly (double []X ,double []Y)
    {
        this.X = X;
        this.Y = Y;
    }

    void draw(GraphicsContext gc) 
    {
        this.gc = gc;
        this.gc.strokePolygon(X, Y, 5);
    }
}