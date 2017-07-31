package inc.getman.ormlitetest1.presentation;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;

import inc.getman.ormlitetest1.R;
import inc.getman.ormlitetest1.data.DatabaseHelper;
import inc.getman.ormlitetest1.model.TeacherDetails;

@EActivity(R.layout.activity_teacher_add)
public class TeacherAddActivity extends AppCompatActivity {

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<TeacherDetails, Integer> teacherDetailsDao;

    @ViewById(R.id.teacher_name_et)
    EditText etTeacherName;
    @ViewById(R.id.address_et)
    EditText etAddress;
    @ViewById(R.id.reset_btn)
    Button btnReset;
    @ViewById(R.id.submit_btn)
    Button btnSubmit;

    @Click({R.id.reset_btn, R.id.submit_btn})
    void onButtonsClick(View view) {

        if (view == btnSubmit) {
            String name = etTeacherName.getText().toString();
            String address = etAddress.getText().toString();
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                final TeacherDetails teacherDetails = new TeacherDetails();

                teacherDetails.teacherName = name;
                teacherDetails.address = address;

                try {
                    teacherDetailsDao.create(teacherDetails);
                    resetFields();
                    showDialog();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showMessageDialog("All fields are mandatory !!");
            }
        } else if (view == btnReset) {
            resetFields();
        }

    }

    private void resetFields() {
        etTeacherName.setText("");
        etAddress.setText("");
    }

    private void showMessageDialog(final String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialog() {
        // After submission, Dialog opens up with "Success" message. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Teacher record added successfully !!");

        // Add a positive button and it's action. In our case action would be, just hide the dialog box ,
        // so no need to write any code for that.
        alertDialogBuilder.setPositiveButton("Add More",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                    }
                });

        // Add a negative button and it's action. In our case, just open up the ViewTeacherRecordActivity screen
        // to display all the records
        alertDialogBuilder.setNegativeButton("View Records",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewTeacherRecordActivity_.intent(getApplicationContext()).start();
                        finish();
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
