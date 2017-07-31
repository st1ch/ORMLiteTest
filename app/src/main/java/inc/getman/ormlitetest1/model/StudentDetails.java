package inc.getman.ormlitetest1.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

/**
 * @developer Artem Getman (a.e.getman@gmail.com)
 * <p>
 * Created by Artem Getman on 7/28/17.
 */
public class StudentDetails implements Serializable {

    public static final String ID_FIELD = "student_id";
    public static final String TEACHER_ID_FIELD = "teacher_id";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int studentId;

    @DatabaseField(columnName = "student_name")
    public String studentName;

    @DatabaseField(columnName = "address")
    public String address;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public TeacherDetails teacher;

    @DatabaseField(columnName = "added_date")
    public Date addedDate;

    public StudentDetails() {
    }

    public StudentDetails(String studentName, String address, TeacherDetails teacher) {
        this.studentName = studentName;
        this.address = address;
        this.teacher = teacher;
    }
}
