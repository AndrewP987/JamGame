module moduleInfo {
    requires transitive javafx.controls;
    requires java.desktop;
    requires java.logging;
    exports commonJam.solver;
    exports jam;
}