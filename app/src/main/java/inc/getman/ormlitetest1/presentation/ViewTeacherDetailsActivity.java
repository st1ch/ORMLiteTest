package inc.getman.ormlitetest1.presentation;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import inc.getman.ormlitetest1.R;
import inc.getman.ormlitetest1.data.DatabaseHelper;
import inc.getman.ormlitetest1.model.StudentDetails;
import inc.getman.ormlitetest1.model.TeacherDetails;

@EActivity(R.layout.activity_view_teacher_details)
public class ViewTeacherDetailsActivity extends AppCompatActivity {

    @ViewById(R.id.teacher_name_et)
    TextView tvTeacherName;

    @ViewById(R.id.address_et)
    TextView tvAddress;

    @ViewById(R.id.students_et)
    TextView tvStudents;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<StudentDetails, Integer> studentDao;

    @AfterViews
    void init() {
        final TeacherDetails tDetails = (TeacherDetails) getIntent().getExtras().getSerializable("details");

        tvTeacherName.setText(tDetails.teacherName);
        tvAddress.setText(tDetails.address);

        final List<String> studentName = new ArrayList<>();

        final QueryBuilder<StudentDetails, Integer> queryBuilder = studentDao.queryBuilder();

        try {
            queryBuilder.where().eq(StudentDetails.TEACHER_ID_FIELD, tDetails.teacherId);

            final PreparedQuery<StudentDetails> preparedQuery = queryBuilder.prepare();

            final Iterator<StudentDetails> studentsIt = studentDao.query(preparedQuery).iterator();

            while (studentsIt.hasNext()) {
                final StudentDetails sDetails = studentsIt.next();
                studentName.add(sDetails.studentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvStudents.setText(studentName.toString());
    }

    @Click(R.id.close_btn)
    protected void onCloseClick() {
        finish();
    }
}
