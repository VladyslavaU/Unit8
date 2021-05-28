import java.awt.*;
import javax.swing.*;

/**
 * Defines a modal dialog for inputting multiline text.
 */
class GetTextDialog extends JDialog {

    private boolean canceled = false;
    private final JTextArea TEXT;

    /**
     * Display the dialog box, wait for the user to dismiss it, and return the
     * user's input, or null if the user cancels the dialog.
     *
     * @param parent      A component whose frame is the parent of the dialog box.
     * @param initialText the initial contents of the inputs box; if null, the box is
     *                    initially empty.
     * @return the text from the input box, or null if the user cancels the
     * dialog. Note that the return can be a blank string if the user
     * clicks "OK" without entering any text.
     */
    public static String showDialog(Component parent, String initialText) {
        GetTextDialog dialog = new GetTextDialog(frameAncestor(parent),
                initialText);
        dialog.setVisible(true);
        if (dialog.canceled)
            return null;
        else
            return dialog.TEXT.getText();
    }

    private static Frame frameAncestor(Component c) {
        while (c != null && !(c instanceof Frame))
            c = c.getParent();
        return (Frame) c;
    }

    /**
     * Creates, but does not show, a dialog box.
     */
    private GetTextDialog(Frame parent, String initialText) {
        super(parent, "Input Your Text", true);
        JPanel content = new JPanel();
        setContentPane(content);
        content.setBackground(Color.LIGHT_GRAY);
        content.setLayout(new BorderLayout(3, 3));
        TEXT = new JTextArea(10, 50);
        TEXT.setMargin(new Insets(6, 6, 6, 6));
        if (initialText != null)
            TEXT.setText(initialText);
        content.add(TEXT, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        content.add(bottom, BorderLayout.SOUTH);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(evt -> {
            canceled = true;
            dispose();
        });
        JButton ok = new JButton("OK");
        ok.addActionListener(evt -> dispose());
        bottom.add(cancel);
        bottom.add(ok);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

}