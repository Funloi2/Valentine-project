module com.example.valentineproject {
    requires javafx.controls;
    requires javafx.graphics;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.valentineproject to javafx.graphics, javafx.controls;
    // Export the package so JavaFX can find your Testing class
    exports com.example.valentineproject;
}