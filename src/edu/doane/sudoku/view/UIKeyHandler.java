package edu.doane.sudoku.view;

import edu.doane.sudoku.controller.SuDoKuController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import javax.swing.*;

/**
 * Class to handle keyboard input in the desktop SuDoKu game.
 *
 * @author Mark M. Meysenburg
 * @version 1/11/2020
 */
public class UIKeyHandler implements EventHandler<KeyEvent> {
    /**
     * Flag telling if we're in note entry mode or not.
     */
    private boolean notesMode;
    /**
     * Reference to the cells in the user interface.
     */
    private final UICell[][] cells;

    /**
     * Reference to the status bar.
     */
    private final UIStatusBar pnlStatusBar;

    /**
     * Reference to the pause functionality
     */
    private static boolean pausedMode;

    /**
     * Reference to the controller used by the app.
     */
    private SuDoKuController controller;

    /**
     * Construct the key handler.
     *
     * @param cells User interface cells
     * @param controller Reference to the application's controller
     * @param pnlStatusBar User interface status bar
     */
    public UIKeyHandler(UICell[][] cells, SuDoKuController controller, UIStatusBar pnlStatusBar) {
        this.cells = cells;
        this.controller = controller;
        notesMode = false;
        this.pnlStatusBar = pnlStatusBar;

        // set pauseMode to false initially
        pausedMode = false;
    }

    @Override
    public void handle(KeyEvent event) {
        // get the character typed
        char c = event.getCode().getChar().charAt(0);

        // prevents the user from inserting anything after winning the game
        if(!controller.isGameOver()) {

            // and handle the input
            switch (c) {
                // n toggles notes mode
                case 'n':
                case 'N':
                    notesMode = !notesMode;
                    setNotesOrNormal();

                    break;

                // created cases for the pause button
                case 'p':
                case 'P':
                    pausedMode = !pausedMode;
                    setPausedMode();

                    break;

                // 1 - 9 sets number or note
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (notesMode) {
                        setNote(c);
                    } else {
                        setNumber(c);
                    }
                    break;

                // space clears number from cell
                case ' ':
                    if (!notesMode) {
                        setNumber(' ');
                    }
                    break;
            }
        }

    }

    /**
     * Cause a note to be set in the selected cell.
     *
     * @param c Character holding number value to set.
     */
    private void setNote(char c) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (cells[row][col].isSelected()) {
                    // ask the controller to set the note
                    controller.setNote(row, col, Integer.parseInt(Character.toString(c)));

                    // get us out of the loop once the note has been set
                    break;
                }
            }
        }
    }

    /**
     * Toggle between notes and normal mode
     */
    private void setNotesOrNormal() {
        // update cells so they can display the correct image
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (notesMode) {
                    cells[row][col].setNotesMode();
                } else {
                    cells[row][col].setNormalMode();
                }
            }
        }
        // update status bar so it indicates notes mode status
        if (notesMode) {
            pnlStatusBar.setNotesMode();
        } else {
            pnlStatusBar.setNormalMode();
        }
    }

    /**
     * Checks to see if the controller is able to edit depending on pause status
     */
    public void setPausedMode() {

        if (pausedMode) {
            controller.pauseGame();
        } else {
            controller.resumeGame();
        }
    }



    /**
     * Cause a number to be set in the selected cell.
     *
     * @param c Character holding the number value to set.
     */
    private void setNumber(char c) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (cells[row][col].isSelected()) {
                    if(c == ' ') {
                        // ask the controller to remove the number
                        controller.removeNumber(row, col);
                    } else {
                        // ask the controller to set the number
                        controller.playNumber(row, col, Integer.parseInt(Character.toString(c)));
                    }

                    // get us out of the loop once the number has been set
                    break;
                }
            }
        }
    }
}
