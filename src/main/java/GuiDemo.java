import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A frame that displays a multiline text, possibly with a background image and
 * with added icon images, in a DrawPanel, along with a variety of controllers.
 */
public class GuiDemo extends JFrame {

    /**
     * The main program just creates a GuiDemo frame and makes it visible.
     */
    public static void main(String[] args) {
        JFrame frame = new GuiDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private final DrawPanel DRAW_PANEL;
    private final SimpleFileChooser FILE_CHOOSER;
    private final TextMenu TEXT_MENU;
    private final JCheckBoxMenuItem GRADIENT_OVERLAY_CHECKBOX = new JCheckBoxMenuItem(
            "Gradient Overlay", true);

    /**
     * The constructor creates the frame, sizes it, and centers it horizontally
     * on the screen.
     */
    public GuiDemo() {

        super("Sayings");
        JPanel content = new JPanel();
        content.setBackground(Color.LIGHT_GRAY);
        content.setLayout(new BorderLayout());
        setContentPane(content);

        DRAW_PANEL = new DrawPanel();
        DRAW_PANEL.getTextItem()
                .setText("Hello World, Hello UoPeople "
                        + " It's our last unit and it's exciting, "
                        + "That we have completed it and ready for "
                        + " next subjects!");
        DRAW_PANEL.getTextItem().setFontSize(36);
        DRAW_PANEL.getTextItem().setJustify(TextItem.LEFT);
        DRAW_PANEL.setBackgroundImage(
                Util.getImageResource("resources/images/mandelbrot.jpeg"));
        content.add(DRAW_PANEL, BorderLayout.CENTER);

        IconSupport iconSupport = new IconSupport(DRAW_PANEL);
        content.add(iconSupport.createToolbar(true), BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(makeFileMenu());
        TEXT_MENU = new TextMenu(DRAW_PANEL);
        menuBar.add(TEXT_MENU);
        menuBar.add(makeBackgroundMenu());
        menuBar.add(iconSupport.createMenu());

        content.add(makeToolbar(), BorderLayout.NORTH);

        setJMenuBar(menuBar);

        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, 50);

        FILE_CHOOSER = new SimpleFileChooser();
        try {
            String userDir = System.getProperty("user.home");
            if (userDir != null) {
                File desktop = new File(userDir, "Desktop");
                if (desktop.isDirectory())
                    FILE_CHOOSER.setDefaultDirectory(desktop);
            }
        } catch (Exception ignored) {
        }

    }

    private JToolBar makeToolbar() {
        JToolBar tbar = new JToolBar(
                JToolBar.HORIZONTAL);
        tbar.add(newPictureAction);
        tbar.add(saveImageAction);

        tbar.addSeparator(new Dimension(15, 0));
        tbar.add(new ChooseBackgroundAction("Mandelbrot"));
        tbar.add(new ChooseBackgroundAction("Earthrise"));
        tbar.add(new ChooseBackgroundAction("Sunset"));
        tbar.add(new ChooseBackgroundAction("Cloud"));
        tbar.add(new ChooseBackgroundAction("Eagle_nebula"));

        tbar.add(new ChooseBackgroundAction("Custom..."));
        tbar.add(new ChooseBackgroundAction("Color..."));
        return tbar;
    }

    /**
     * Create the "File" menu from actions that are defined later in this class.
     */
    private JMenu makeFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(newPictureAction);
        menu.add(saveImageAction);
        menu.addSeparator();
        menu.add(quitAction);
        return menu;
    }

    /**
     * Create the "Background" menu, using objects of type
     * ChooseBackgroundAction, a class that is defined later in this file.
     */
    private JMenu makeBackgroundMenu() {
        JMenu menu = new JMenu("Background");
        menu.add(new ChooseBackgroundAction("Mandelbrot"));
        menu.add(new ChooseBackgroundAction("Earthrise"));
        menu.add(new ChooseBackgroundAction("Sunset"));
        menu.add(new ChooseBackgroundAction("Cloud"));
        menu.add(new ChooseBackgroundAction("Eagle_nebula"));
        menu.addSeparator();
        menu.add(new ChooseBackgroundAction("Custom..."));
        menu.addSeparator();
        menu.add(new ChooseBackgroundAction("Color..."));
        menu.addSeparator();
        menu.add(GRADIENT_OVERLAY_CHECKBOX);
        GRADIENT_OVERLAY_CHECKBOX.addActionListener(evt -> {
            if (GRADIENT_OVERLAY_CHECKBOX.isSelected())
                DRAW_PANEL.setGradientOverlayColor(Color.WHITE);
            else
                DRAW_PANEL.setGradientOverlayColor(null);
        });
        return menu;
    }

    private final AbstractAction newPictureAction = new AbstractAction("New",
            Util.iconFromResource("resources/action_icons/fileopen.png")) {
        public void actionPerformed(ActionEvent evt) {
            DRAW_PANEL.clear();
            GRADIENT_OVERLAY_CHECKBOX.setSelected(true);
            TEXT_MENU.setDefaults();
        }
    };

    private final AbstractAction quitAction = new AbstractAction("Quit",
            Util.iconFromResource("resources/action_icons/exit.png")) {
        public void actionPerformed(ActionEvent evt) {
            System.exit(0);
        }
    };

    private final AbstractAction saveImageAction = new AbstractAction("Save Image...",
            Util.iconFromResource("resources/action_icons/filesave.png")) {
        public void actionPerformed(ActionEvent evt) {
            File f = FILE_CHOOSER.getOutputFile(DRAW_PANEL, "Select Ouput File",
                    "saying.jpeg");
            if (f != null) {
                try {
                    BufferedImage img = DRAW_PANEL.copyImage();
                    String format;
                    String fileName = f.getName().toLowerCase();
                    if (fileName.endsWith(".png"))
                        format = "PNG";
                    else if (fileName.endsWith(".jpeg")
                            || fileName.endsWith(".jpg"))
                        format = "JPEG";
                    else {
                        JOptionPane.showMessageDialog(DRAW_PANEL,
                                "The output file name must end wth .png or .jpeg.");
                        return;
                    }
                    ImageIO.write(img, format, f);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DRAW_PANEL,
                            "Sorry, the image could not be saved.");
                }
            }
        }
    };

    /**
     * An object of type ChooseBackgroudnAction represents an action through
     * which the user selects the background of the picture. There are three
     * types of background: solid color background ("Color..." command), an
     * image selected by the user from the file system ("Custom..." command),
     * and four built-in image resources (Mandelbrot, Earthrise, Sunset, and
     * Eagle_nebula).
     */
    private class ChooseBackgroundAction extends AbstractAction {
        String text;

        ChooseBackgroundAction(String text) {
            super(text);
            this.text = text;
            if (!text.equals("Custom...") && !text.equals("Color...")) {
                putValue(Action.SMALL_ICON,
                        Util.iconFromResource("resources/images/"
                                + text.toLowerCase() + "_thumbnail.jpeg"));
            }
            if (text.equals("Color...")) {

                // add the color image icon
                BufferedImage colorBuffer = new BufferedImage(32, 32,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics g = colorBuffer.createGraphics();
                g.setColor(Color.RED);
                g.fillRect(0, 0, 32, 32);
                g.setColor(Color.GREEN);
                g.fillRect(10, 0, 32, 32);
                g.setColor(Color.BLUE);
                g.fillRect(20, 0, 32, 32);
                g.dispose();
                putValue(Action.SMALL_ICON, new ImageIcon(colorBuffer));
                putValue(Action.SHORT_DESCRIPTION,
                        "To set the background color."); // tooltip
            } else if (text.equals("Custom..."))
                putValue(Action.SMALL_ICON, Util.iconFromResource(
                        "resources/action_icons/fileopen.png"));
            else
                putValue(Action.SHORT_DESCRIPTION,
                        "Use this image as the background.");

        }

        public void actionPerformed(ActionEvent evt) {
            if (text.equals("Custom...")) {
                File inputFile = FILE_CHOOSER.getInputFile(DRAW_PANEL,
                        "Select Background Image");
                if (inputFile != null) {
                    try {
                        BufferedImage img = ImageIO.read(inputFile);
                        if (img == null)
                            throw new Exception();
                        DRAW_PANEL.setBackgroundImage(img);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(DRAW_PANEL,
                                "Sorry, couldn't read the file.");
                    }
                }
            } else if (text.equals("Color...")) {
                Color c = JColorChooser.showDialog(DRAW_PANEL,
                        "Select Color for Background",
                        DRAW_PANEL.getBackground());
                if (c != null) {
                    DRAW_PANEL.setBackground(c);
                    DRAW_PANEL.setBackgroundImage(null);
                }
            } else {
                Image bg = Util.getImageResource(
                        "resources/images/" + text.toLowerCase() + ".jpeg");
                DRAW_PANEL.setBackgroundImage(bg);
            }
        }
    }
}