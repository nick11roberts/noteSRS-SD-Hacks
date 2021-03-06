package io.serious.not.notesrs;

import java.util.Locale;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainNoteSrs extends AppCompatActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static final int TAB_CORRECT = 1;
    private static final int TAB_UPLOAD = 2;

    private static final int MAX_LINES_CORRECT_TEXT_FIELD = 15;
    private static final int MAX_LINES_SINGLE_WORD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note_srs);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_note_srs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements CorrectAsyncResponse  {

        Button correctButton;
        Button uploadButton;

        EditText correctTextField;
        EditText uploadIncorrectField;
        EditText uploadCorrectField;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int mTab;
        LinearLayout layout;

        /**
         * onCreate, duh.
         * @param savedInstanceState
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null)
                mTab = getArguments().getInt(ARG_SECTION_NUMBER);
            else throw new RuntimeException("TabFragment needs a tab number");
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public void processFinish(String output){
            //this you will received result fired from async class of onPostExecute(result) method.
            if(correctTextField != null){
                correctTextField.setText(output);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final Context c = container.getContext();
            final Object currentClass = this;

            layout = new LinearLayout(c);
            layout.setOrientation(LinearLayout.VERTICAL);

            EditText txt = new EditText(c);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            txt.setLayoutParams(lp);
            txt.setSingleLine(false);
            txt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

            switch (mTab) {
                case TAB_CORRECT:
                    final CorrectAsyncTask placeholderCorrectAsyncTask = new CorrectAsyncTask();
                    placeholderCorrectAsyncTask.delegate = this;
                    correctTextField = new EditText(c);
                    correctTextField.setMaxLines(MAX_LINES_CORRECT_TEXT_FIELD);
                    layout.addView(correctTextField);
                    correctButton = new Button(c);
                    correctButton.setText(R.string.correct);
                    correctButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CorrectAsyncTask correctAsyncTask = new CorrectAsyncTask();
                            correctAsyncTask.delegate = placeholderCorrectAsyncTask.delegate;
                            correctAsyncTask.execute(new Pair<>(getContext(), correctTextField.getText().toString()));
                        }
                    });

                    layout.addView(correctButton);
                    break;

                case TAB_UPLOAD:
                    uploadIncorrectField = new EditText(c);
                    uploadIncorrectField.setMaxLines(MAX_LINES_SINGLE_WORD);
                    layout.addView(uploadIncorrectField);
                    uploadCorrectField = new EditText(c);
                    uploadCorrectField.setMaxLines(MAX_LINES_SINGLE_WORD);
                    layout.addView(uploadCorrectField);
                    uploadButton = new Button(c);
                    uploadButton.setText(R.string.upload);
                    uploadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new InsertAutoCucumberListItemAsyncTask()
                                    .execute(
                                            new SingleAutoCucumber(
                                                    uploadIncorrectField.getText().toString(),
                                                    uploadCorrectField.getText().toString(),
                                                    c
                                            )
                                    );
                        }
                    });
                    layout.addView(uploadButton);
                    break;
            }

            return layout;
        }

    }

}
