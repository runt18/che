package org.eclipse.che.ide.editor.preferences;

/**
 * Created by roman on 27.03.17.
 */
public interface EditorPreferences {

    String toPreference();

    public enum AutoSaveSettings {
        ENABLE_AUTO_SAVE("enableAutosave");

        AutoSaveSettings(String enableAutosave) {

        }
    }
}
