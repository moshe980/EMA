package com.example.ema.fregments;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ema.classs.Form1007;
import com.example.ema.classs.ImageAdapter;
import com.example.ema.activitys.Menu;
import com.example.ema.R;
import com.example.ema.activitys.AddFiles;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Estimate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Estimate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Estimate extends Fragment implements ImageAdapter.OnItemClicklistener {
    private static final String DESCRIBABLE_KEY = "describable_key";

    private RecyclerView recyclerView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private DatabaseReference myRef;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private ImageAdapter imageAdapter;
    private List<String> estimats;
    private List<String> tmp;
    private ProgressBar progressBar;
    private Button uploadEstimatsBtn;
    private int counter = 1;
    private ValueEventListener mDBLisrener;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Form1007 currentForm1007;
    private String mParam2;

    private Estimate.OnFragmentInteractionListener mListener;

    public Estimate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     */
    // TODO: Rename and change types and number of parameters
    public static Estimate newInstance(Form1007 form1007, String param2) {
        Estimate fragment = new Estimate();
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
        final View view = inflater.inflate(R.layout.fragment_estimate, container, false);
        //English default:
        Configuration config = new Configuration();
        Locale locale = new Locale("heb");

        Locale.setDefault(locale);
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());

        progressBar = view.findViewById(R.id.bar4);

        storage = FirebaseStorage.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@')))
                .child("uploadsEstimate");
        estimats = new ArrayList<>();
        tmp = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEstimate);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");

        uploadEstimatsBtn = view.findViewById(R.id.uploadEstimats);
        currentForm1007= AddFiles.currentForm1007;

        if (currentForm1007.getStatus()!=null&&currentForm1007.getStatus().equals("ממתין לאישור")) {
            uploadEstimatsBtn.setVisibility(View.GONE);
            myRef = database.getReference("users");
            estimats.addAll(AddFiles.currentForm1007.getEstimats());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            imageAdapter = new ImageAdapter(getActivity(), estimats);

            recyclerView.setAdapter(imageAdapter);

            imageAdapter.setOnItemClicklistener(Estimate.this);

            imageAdapter.notifyDataSetChanged();

        } else {

            uploadEstimatsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openFileChooser();

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getActivity(), "נא להמתין", Toast.LENGTH_SHORT).show();
                    } else {
                    }

                }
            });
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            imageAdapter = new ImageAdapter(getActivity(), estimats);

            recyclerView.setAdapter(imageAdapter);

            imageAdapter.setOnItemClicklistener(Estimate.this);

            Query query = myRef.orderByChild("equipmentId").equalTo(AddFiles.currentForm1007.getEquipmentId());
            mDBLisrener = myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    estimats.clear();
                    tmp.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("estimats").getValue() != null) {
                            tmp.addAll((Collection<? extends String>) ds.child("estimats").getValue());
                            for (String s : tmp) {
                                if (s != null) {
                                    estimats.add(s);

                                }
                            }
                        }
                    }


                    imageAdapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Erorr in updating data");

                }
            });

            query.addListenerForSingleValueEvent(mDBLisrener);
        }
        return view;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }


        //Intent intent = new Intent();
        // intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri returnUri = data.getData();
            mImageUri = returnUri;

            uploadFile();

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadFile() {
        if (mImageUri != null) {
            storageReference = storageReference.child("estimats" + AddFiles.currentForm1007.getId() + counter + "." + getFileExtension(mImageUri));
            counter++;
            mUploadTask = storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar = getActivity().findViewById(R.id.bar4);
                    progressBar.setVisibility(View.VISIBLE);
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            Toast.makeText(getActivity(), "הקובץ עלה בהצלחה", Toast.LENGTH_SHORT).show();
                            myRef = database.getReference("users").child(Menu.user.getEmail().substring(0, Menu.user.getEmail().indexOf('@'))).child("opened1007");
                            Query query = myRef.orderByChild("equipmentId").equalTo(AddFiles.currentForm1007.getEquipmentId());
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    estimats.add(url);

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ds.child("estimats").getRef().setValue(estimats);
                                    }

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("Erorr in updating data");
                                    progressBar.setVisibility(View.GONE);

                                }
                            };
                            query.addListenerForSingleValueEvent(eventListener);


                        }

                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {

            Toast.makeText(getActivity(), "לא בחרת טופס 1007 חתום על ידי הספק", Toast.LENGTH_SHORT).show();
        }

        // Reload current fragment
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();

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
        if (context instanceof Estimate.OnFragmentInteractionListener) {
            mListener = (Estimate.OnFragmentInteractionListener) context;
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
        Uri uri=Uri.parse(estimats.get(position));
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(currentForm1007.getId()+"הצעת מחיר");
        request.setDescription("Downloading esimate");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());


        DownloadManager manager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


    @Override
    public void onDeleteClick(final int position) {
        if (currentForm1007.getStatus().equals("טרם אושר")) {
            StorageReference imageRef = storage.getReferenceFromUrl(estimats.get(position));
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Query query = myRef.orderByChild("equipmentId").equalTo(currentForm1007.getEquipmentId());
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.child("estimats").child(String.valueOf(position)).getRef().removeValue();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    query.addListenerForSingleValueEvent(eventListener);
                    Toast.makeText(getActivity(), "נמחק", Toast.LENGTH_SHORT).show();


                }
            });

        }
        // Reload current fragment
        // getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentForm1007.getStatus()==null)
            myRef.removeEventListener(mDBLisrener);
    }
}
