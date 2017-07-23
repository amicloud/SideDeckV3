package com.outplaysoftworks.sidedeck;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LpCalculator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LpCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LpCalculator extends Fragment {

    //Butterknife Viewbindings
    @BindView(R.id.LpCalculatorTextEnteredValue)
    TextView tvEnteredValue;


    private OnFragmentInteractionListener mListener;

    public LpCalculator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LpCalculator.
     */
    // TODO: Rename and change types and number of parameters
    public static LpCalculator newInstance() {
        LpCalculator fragment = new LpCalculator();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lp_calculator, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick({R.id.LpCalculatorButton0, R.id.LpCalculatorButton00, R.id.LpCalculatorButton000,
            R.id.LpCalculatorButton1, R.id.LpCalculatorButton2, R.id.LpCalculatorButton3,
            R.id.LpCalculatorButton4, R.id.LpCalculatorButton5, R.id.LpCalculatorButton6,
            R.id.LpCalculatorButton7, R.id.LpCalculatorButton8, R.id.LpCalculatorButton9})
    public void onClickCalculatorNumber(View view){
        int amount = Integer.parseInt(view.getTag().toString());
        if(LpCalculatorModel.appendToEnteredValue(amount)){
            onEnteredValueUpdated();
        }
    }

    public void onEnteredValueUpdated(){
        //TODO: Animations!
        tvEnteredValue.setText(Integer.toString(LpCalculatorModel.getEnteredValue()));
    }
}
