package inc.getman.ormlitetest1.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import inc.getman.ormlitetest1.R;
import inc.getman.ormlitetest1.data.DatabaseHelper;
import inc.getman.ormlitetest1.model.StudentDetails;
import inc.getman.ormlitetest1.model.TeacherDetails;

import static inc.getman.ormlitetest1.R.id.address_et;
import static inc.getman.ormlitetest1.R.id.student_name_et;
import static inc.getman.ormlitetest1.R.id.teacher_sp;

@EActivity(R.layout.activity_student_add)
public class StudentAddActivity extends AppCompatActivity {

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<TeacherDetails, Integer> teacherDetailsDao;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<StudentDetails, Integer> studentDetailsDao;

    @ViewById(student_name_et)
    EditText etStudentName;
    @ViewById(address_et)
    EditText etAddress;
    @ViewById(R.id.reset_btn)
    Button btnReset;
    @ViewById(R.id.submit_btn)
    Button btnSubmit;
    @ViewById(teacher_sp)
    Spinner spTeacher;

    private List<TeacherDetails> teacherList;

    @AfterViews
    protected void init() {
        try {
            teacherList = teacherDetailsDao.queryForAll();
            spTeacher.setAdapter(new CustomAdapter(this, android.R.layout.simple_spinner_item,
                    android.R.layout.simple_spinner_dropdown_item, teacherList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click({R.id.reset_btn, R.id.submit_btn})
    void onButtonsClick(View view) {
        if (view == btnSubmit) {
            // Student Details record only be added, if atleast one Teacher Details data exists in the database
            if (teacherList.size() > 0) {
                // All input fields are mandatory, so made a check
                String name = etStudentName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                    // Create the StudentDetails object and set the inputed data into it
                    final StudentDetails stuDetails = new StudentDetails();
                    stuDetails.studentName = name;
                    stuDetails.address = address;
                    stuDetails.addedDate = new Date();

                    // StudentDetails has a reference to TeacherDetails, so set the reference as well
                    stuDetails.teacher = (TeacherDetails) spTeacher.getSelectedItem();

                    try {
                        //This is the way to insert data into a database table
                        studentDetailsDao.create(stuDetails);
                        reset();
                        showDialog();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Show a dialog with appropriate message in case input fields are blank
                else {
                    showMessageDialog("All fields are mandatory !!");
                }
            }
            // If no TeacherDetails data found in the database, show a dialog with appropriate message to user
            else {
                showMessageDialog("Please, add Teacher Details first !!");
            }
        } else if (view == btnReset) {

        }
    }

    private void showMessageDialog(final String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Clear the entered text
    private void reset() {
        etStudentName.setText("");
        etAddress.setText("");
    }

    private void showDialog() {
        // After submission, Dialog opens up with "Success" message. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Student record added successfully !!");

        // Add a positive button and it's action. In our case action would be, just hide the dialog box
        // so no need to write any code for that.
        alertDialogBuilder.setPositiveButton("Add More",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        // Add a negative button and it's action. In our case, just open up the ViewStudentRecordActivity screen
        //to display all the records
        alertDialogBuilder.setNegativeButton("View Records",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewStudentRecordActivity_.intent(getApplicationContext()).start();
                        finish();
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Custom Adapter to feed data to the Teacher Spinner
    @SuppressWarnings("rawtypes")
    class CustomAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;

        // Holds data of Teacher Details
        List objects;

        @SuppressWarnings("unchecked")
        public CustomAdapter(Context context, int resource, int dropDownViewResource, List objects) {
            super(context, resource, objects);
            this.setDropDownViewResource(dropDownViewResource);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // set the Teacher Details objects to populate the Spinner
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // Inflate the Android default spinner layout view, set the label according to passed data and
        // return the view to display as one row of the Teacher Spinner
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            final View row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

            final TextView label = (TextView) row.findViewById(android.R.id.text1);
            final TeacherDetails teacher = (TeacherDetails) this.objects.get(position);
            label.setText(teacher.teacherName);
            return row;
        }
    }
}
