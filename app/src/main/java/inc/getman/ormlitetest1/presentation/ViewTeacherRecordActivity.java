package inc.getman.ormlitetest1.presentation;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

import inc.getman.ormlitetest1.R;
import inc.getman.ormlitetest1.data.DatabaseHelper;
import inc.getman.ormlitetest1.model.TeacherDetails;
import inc.getman.ormlitetest1.presentation.adapter.RecordArrayAdapter;

import static inc.getman.ormlitetest1.R.id.listview;

/**
 * @developer Artem Getman (a.e.getman@gmail.com)
 * <p>
 * Created by Artem Getman on 7/28/17.
 */
@EActivity(R.layout.activity_view_student_record)
public class ViewTeacherRecordActivity extends AppCompatActivity {
    @ViewById(listview)
    ListView listView;

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<TeacherDetails, Integer> teacherDao;

    private List<TeacherDetails> teacherList;

    // This holds the value of the row number, which user has selected for further action
    private int selectedRecordPosition = -1;

    @AfterViews
    void initViews() {
        ((TextView) findViewById(R.id.header_tv)).setText("View Teacher Records");
    }

    @AfterViews
    void init() {
        try {
            teacherList = teacherDao.queryForAll();

            View rowView = LayoutInflater.from(this).inflate(R.layout.list_item, listView, false);
            ((TextView) rowView.findViewById(R.id.teacher_tv)).setText("Address");
            ((TextView) rowView.findViewById(R.id.student_name_tv)).setText("Teacher Name");
            listView.addHeaderView(rowView);

            listView.setAdapter(new RecordArrayAdapter(this, R.layout.list_item, teacherList, teacherDao));
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // If the long pressed row is not a header, update selectedRecordPosition and show the dialog
                    if (position > 0) {
                        selectedRecordPosition = position - 1;
                        showDialog();
                    }
                    return true;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // If the pressed row is not a header, update selectedRecordPosition and render ViewTeacherDetailsActivity screen
                    if (position > 0) {
                        selectedRecordPosition = position - 1;
                        // Details screen showing code can put over here
                        ViewTeacherDetailsActivity_.intent(ViewTeacherRecordActivity.this).extra("details", teacherList.get(selectedRecordPosition)).start();
                    }
                }
            });

            populateNoRecordMsg();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateNoRecordMsg() {
        // If, no record found in the database, appropriate message needs to be displayed.
        if (teacherList.size() == 0) {
            final TextView tv = new TextView(this);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextSize(15);
            tv.setText("No Record Found !!");
            listView.addFooterView(tv);
        }
    }

    private void showDialog() {
        // Before deletion of the long pressed record, need to confirm with the user. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Are you Really want to delete the selected record ?");

        // Add a positive button and it's action. In our case action would be deletion of the data
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            // This is how, data from the database can be deleted
                            teacherDao.delete(teacherList.get(selectedRecordPosition));

                            // Removing the same from the List to remove from display as well
                            teacherList.remove(selectedRecordPosition);
                            listView.invalidateViews();

                            // Reset the value of selectedRecordPosition
                            selectedRecordPosition = -1;
                            populateNoRecordMsg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        // Add a negative button and it's action. In our case, just hide the dialog box
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

