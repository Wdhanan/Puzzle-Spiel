import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PuzzleSpiel extends Application {
   // Arrays fuer die  Texte und Buttons
   private String[] TEXT =
   { "1", "2", "3", "4", "5", "6", "7", "8", " " };
   private Button[] mButtons = new Button[9];
   // Index des freien Elements
   private int mFreeIndex;
   // Zaehlt die Anzahl der Versuche
   private int mCount;
   
	@Override
	public void start(Stage primaryStage) {
      TilePane p = new TilePane(2,2);
      p.setPadding(new Insets(10, 10, 10, 10));
      p.setPrefColumns(3);
      // Erzeugen der Buttons
      // und sie in den Container aufnehmen
      // und Ereignisbehandlung wird registriert
      for (int i = 0; i < TEXT.length; i++)
      {
         mButtons[i] = new Button();
         mButtons[i].setPrefSize(50, 50);
         mButtons[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         p.getChildren().add(mButtons[i]);
         // das Fenster behandelt alle Events
         mButtons[i].setFont(Font.font(40));
         mButtons[i].setTextFill(Color.BLUE);
         mButtons[i].setOnAction((e)-> {actionPerformed(e);}) ;
      }
      initGame();
      primaryStage.setScene(new Scene(p));
      primaryStage.setTitle("Puzzle-Spiel");
      primaryStage.show();
   }

   private void initGame()
   {
	    
      mFreeIndex = 8;
      mCount = 0;
      mix();
      setText();
   }

   private void setText()
   {
      for (int i = 0; i < TEXT.length; i++)
      {
         mButtons[i].setText(TEXT[i]);
      }
   }

   private void mix()
   {
      // die ersten 8 werden gemischt
      // Die Anzahl der Tausche muss gerade sein
      // Sonst ist das Spiel nicht loesbar!
      Random r = new Random();
      for (int i = 0; i < 100; ++i)
      {
         // zwei zufaellige Indizes werden ausgewuerfelt
         int index1 = r.nextInt(8);
         int index2 = r.nextInt(8);
         // Wenn gleiche Indizes => Wiederholung
         if (index1 == index2)
         {
            i--;
            continue;
         }
         // die Elemente tauschen
         String tmp = TEXT[index1];
         TEXT[index1] = TEXT[index2];
         TEXT[index2] = tmp;
      }
   }

   private boolean isReady()
   {
      // ueberprufen, ob alles richtig ist
      for (int i = 0; i < mButtons.length - 1; i++)
      {
         // wenn ein Feld falsch ist = weiter
         if (mButtons[i].getText().equals("" + (i + 1)) == false)
         {
            return false;
         }
      }
       return true;
   }

   public void actionPerformed(ActionEvent e)
   {
      // Herausfinden, welcher Button geklickt wurde
      Object src = e.getSource();
      int index = -1;
      // Suche nach dem entsprechenden Index im Array
      for (int i = 0; i < 9; ++i)
      {
         if (mButtons[i] == src)
         {
            index = i;
            break;
         }
      }
      // Der Benutzer klickt auf das freie Feld => keine Aktion
      if (index == mFreeIndex)
         return;

      // Position des freien Felds
      int xFree = mFreeIndex % 3;
      int yFree = mFreeIndex / 3;
      // Herausfinden, in welcher Zeile und Spalte der Klick ist
      int x = index % 3;
      int y = index / 3;

      // Gleiche Spalte, benachbarte Zeile
      // oder gleiche Zeile, benachbarte Spalte
      if ((xFree == x && Math.abs(yFree - y) == 1)
            || (yFree == y && Math.abs(xFree - x) == 1))
      {
         // Der Button wird nichts bewegt, sondern nur die
         // Texte werden vertauscht!
         // Hier wird die Anzahl der Schritte erhoeht
         mCount++;
         String txt = mButtons[index].getText();
         mButtons[index].setText("");
         mButtons[mFreeIndex].setText(txt);
         // Neue freie Position merken
         mFreeIndex = index;

         if (isReady())
         {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Gratulation");
            String text ="Sie haben das Problem in " + mCount +" "+ "Schritten"+" "+"\n"+"Wollen Sie weiter spielen?"; 
            
            alert.setContentText(text);

            // Ruckgabe
            Optional<ButtonType> result = alert.showAndWait();
                
            // Abfrage, welcher Button gedrueckt wurde
            if ( result.isPresent() && (result.get() == ButtonType.OK) )
            {
              initGame();
            }
            else
            {
               Platform.exit();
            }
         }
      }
   }


	public static void main(String[] args) {
		launch(args);
	}
}
