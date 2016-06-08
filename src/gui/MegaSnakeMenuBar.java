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
    JMenu joinRoom;
    /**
     * The join room item.
     */
    JMenuItem create_room;
    /**
     * the graphics game rooms list.
     */
    JList<String> rooms_list;

    /**
     * Constructor - construct the menuBar.
     * @param rooms_name - the vector of the game rooms.
     * @param roomSelectionListener - the selection listener for the rooms JList.
     * @param roomCreationListener - the action listener for the new room button.
     */
    public MegaSnakeMenuBar(Vector<String> rooms_name, ListSelectionListener roomSelectionListener, ActionListener roomCreationListener){

        rooms_menu = new JMenu("Rooms");
        this.add(rooms_menu);

        joinRoom = new JMenu("Join Room");
        rooms_menu.add(joinRoom);

        rooms_list = new JList<>(rooms_name);
        joinRoom.add(rooms_list);
        rooms_list.addListSelectionListener(roomSelectionListener);
        rooms_list.setVisibleRowCount(-1);
        rooms_list.setFixedCellWidth(100);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) rooms_list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        create_room = new JMenuItem("Create Room");
        create_room.setActionCommand("create-room");
        create_room.addActionListener(roomCreationListener);
        rooms_menu.add(create_room);
    }
}
