package com.example.ema.classs;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ema.R;
import com.example.ema.activitys.AddFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Bill.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bill#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bill extends Fragment implements ImageAdapter.OnItemClicklistener {
    private static final String DESCRIBABLE_KEY = "describable_key";

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> bills;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Form1007 currentForm1007;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Bill() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bill.
     */
    // TODO: Rename and change types and number of parameters
    public static Bill newInstance(Form1007 form1007, String param2) {
        Bill fragment = new Bill();
        Bundle args = new Bundle();
        args.putSerializable(DESCRIBABLE_KEY, form1007);

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentForm1007 = (Form1007) getArguments().getSerializable(DESCRIBABLE_KEY);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bill, container, false);
        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());

        bills = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBill);

        if (AddFiles.currentForm1007.getBill() != null)
            bills.addAll(AddFiles.currentForm1007.getBill());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        imageAdapter = new ImageAdapter(getActivity(), bills);

        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClicklistener(Bill.this);

        imageAdapter.notifyDataSetChanged();

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
        if (context instanceof Bill.OnFragmentInteractionListener) {
            mListener = (Bill.OnFragmentInteractionListener) context;
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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDownloadClick(int position) {
        Uri uri=Uri.parse(bills.get(position));
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("חשבונית");
        request.setDescription("Downloadinf bill");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());


        DownloadManager manager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    @Override
    public void onDeleteClick(int position) {

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
}
