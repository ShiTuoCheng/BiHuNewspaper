package Model;

/**
 * Created by shituocheng on 16/5/9.
 */
public class ThemeEditorModel {

    private int editor_id;
    private String avatar;
    private String editor_name;
    private String nrl;
    private String bio;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getEditor_id() {
        return editor_id;
    }

    public void setEditor_id(int editor_id) {
        this.editor_id = editor_id;
    }

    public String getEditor_name() {
        return editor_name;
    }

    public void setEditor_name(String editor_name) {
        this.editor_name = editor_name;
    }

    public String getNrl() {
        return nrl;
    }

    public void setNrl(String nrl) {
        this.nrl = nrl;
    }
}