package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created by obama on 01/06/2016.
 *
 * The class packs all of the menuBar information.
 */
public class MegaSnakeMenuBar extends JMenuBar {
    /**
     * The room menu
     */
    JMenu rooms_menu;
    /**
     * the join room sub-menu.
     */
    JMenuItem joinRoom;
    /**
     * The join room item.
     */
    JMenuItem create_room;

    JMenu game_menu;

    JMenuItem exit;

    JMenuItem restart;


    /**
     * Constructor - construct the menuBar.
     * @param roomCreationListener - the action listener for the new room button.
     */
    public MegaSnakeMenuBar(ActionListener roomCreationListener, ActionListener joinRoomListener, ActionListener restartActionListener, ActionListener exitActionListener){

        game_menu = new JMenu("game");
        exit = new JMenuItem("exit");
        exit.addActionListener(exitActionListener);
        exit.setActionCommand("exit");
        game_menu.add(exit);



        restart = new JMenuItem("restart");
        restart.addActionListener(restartActionListener);
        restart.setActionCommand("restart");
        game_menu.add(restart);

        add(game_menu);

        rooms_menu = new JMenu("Rooms");
        this.add(rooms_menu);


        joinRoom = new JMenuItem("Join Room");
        joinRoom.setActionCommand("join-room");
        joinRoom.addActionListener(joinRoomListener);
        rooms_menu.add(joinRoom);

        create_room = new JMenuItem("Create Room");
        create_room.setActionCommand("create-room");
        create_room.addActionListener(roomCreationListener);
        rooms_menu.add(create_room);
    }
}
