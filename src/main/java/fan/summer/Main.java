package fan.summer;

import fan.summer.database.DatabaseInit;
import fan.summer.ui.StartLoadingPage;
import fan.summer.ui.home.HomePage;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JWindow splash = new StartLoadingPage().getWindow();
            splash.setVisible(true);

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    try {
                        DatabaseInit.init();
                    } catch (Exception e) {
                        publish((Void) null);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    splash.dispose();
                    try {
                        get();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                            "Database initialization failed: " + e.getCause().getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                        return;
                    }
                    new HomePage().init();
                }
            }.execute();
        });
    }
}
