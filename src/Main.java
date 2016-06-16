/**
 * Created by obama on 01/06/2016.
 */

import megaSnake.*;

import javax.swing.*;
import java.io.IOException;

/**
 * This Class is the Main class - the one that needed to run.
 *
 * @author      Barak Ohana
 */
public class Main {

    /**
     * The main method.
     * @param args
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

    /**
     * The method that going to run when the program is executed.
     */
    public Main(){
        String ip = "localhost";//JOptionPane.showInputDialog(null, "enter ip");
        try {
            new Game(ip, 15, 15);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
