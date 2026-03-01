package fan.summer;

import fan.summer.ui.home.HomePage;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage().init());
    }

}
