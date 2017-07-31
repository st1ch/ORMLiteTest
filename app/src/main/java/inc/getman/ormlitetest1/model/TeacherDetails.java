package inc.getman.ormlitetest1.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * @developer Artem Getman (a.e.getman@gmail.com)
 * <p>
 * Created by Artem Getman on 7/28/17.
 */
public class TeacherDetails implements Serializable {

    @DatabaseField(generatedId = true, columnName = "teacher_id")
    public int teacherId;

    @DatabaseField(columnName = "teacher_name")
    public String teacherName;

    @DatabaseField(columnName = "teacher_address")
    public String address;

    public TeacherDetails() {
    }

    public TeacherDetails(String teacherName, String address) {
        this.teacherName = teacherName;
        this.address = address;
    }
}
