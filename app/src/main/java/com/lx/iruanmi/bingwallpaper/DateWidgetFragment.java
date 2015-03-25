package com.lx.iruanmi.bingwallpaper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.lx.iruanmi.bingwallpaper.DateWidgetFragment.OnDateWidgetFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateWidgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateWidgetFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH_OF_YEAR = "monthOfYear";
    private static final String ARG_DAY_OF_MONTH = "dayOfMonth";
    private static final String ARG_MIN_DATE = "minDate";
    private static final String ARG_MAX_DATE = "maxDate";

    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;
    private long mMinDate;
    private long mMaxDate;

    private OnDateWidgetFragmentInteractionListener mListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonthOfYear = monthOfYear;
                    mDayOfMonth = dayOfMonth;
                    //updateDisplay();

                    if (mListener != null) {
                        mListener.onDateWidgetFragmentInteraction(year, monthOfYear, dayOfMonth);
                    }
                }
            };

    public DateWidgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param year        Parameter 1.
     * @param monthOfYear Parameter 2.
     * @param dayOfMonth  Parameter 2.
     * @return A new instance of fragment DateWidgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateWidgetFragment newInstance(int year, int monthOfYear, int dayOfMonth, long minDate, long maxDate) {
        DateWidgetFragment fragment = new DateWidgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH_OF_YEAR, monthOfYear);
        args.putInt(ARG_DAY_OF_MONTH, dayOfMonth);
        args.putLong(ARG_MIN_DATE, minDate);
        args.putLong(ARG_MAX_DATE, maxDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYear = getArguments().getInt(ARG_YEAR);
            mMonthOfYear = getArguments().getInt(ARG_MONTH_OF_YEAR);
            mDayOfMonth = getArguments().getInt(ARG_DAY_OF_MONTH);
            mMinDate = getArguments().getLong(ARG_MIN_DATE);
            mMaxDate = getArguments().getLong(ARG_MAX_DATE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                mDateSetListener,
                mYear, mMonthOfYear, mDayOfMonth);
        dialog.getDatePicker().setMinDate(mMinDate);
        dialog.getDatePicker().setMaxDate(mMaxDate);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setOnDateWidgetFragmentInteractionListener(OnDateWidgetFragmentInteractionListener listener) {
        mListener = listener;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDateWidgetFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onDateWidgetFragmentInteraction(int year, int monthOfYear, int dayOfMonth);
    }
}
