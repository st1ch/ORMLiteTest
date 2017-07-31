package inc.getman.ormlitetest1.presentation;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import inc.getman.ormlitetest1.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Click({R.id.add_student_btn, R.id.add_teacher_btn, R.id.view_btn, R.id.view_teacher_btn})
    protected void onButtonsClick(View view){
        switch (view.getId()){
            case R.id.add_student_btn:
                StudentAddActivity_.intent(this).start();
                break;
            case R.id.add_teacher_btn:
                TeacherAddActivity_.intent(this).start();
                break;
            case R.id.view_btn:
                ViewStudentRecordActivity_.intent(this).start();
                break;
            case R.id.view_teacher_btn:
                ViewTeacherRecordActivity_.intent(this).start();
                break;
        }
    }

}
