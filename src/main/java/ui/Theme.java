package ui;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;


import javax.swing.*;

public enum Theme {

    DARK(new FlatDarkLaf()),
    LIGHT(new FlatLightLaf()),
    MACDARK(new FlatMacDarkLaf()),
    MACLIGHT(new FlatMacLightLaf());

    private final LookAndFeel laf;
    Theme(LookAndFeel laf) { this.laf = laf; }
    public LookAndFeel getLaf() { return laf; }


}
