package com.example.wastemanagementvrushabh.ui.complaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.wastemanagementvrushabh.MainActivity;
import com.example.wastemanagementvrushabh.Model;
import com.example.wastemanagementvrushabh.R;
import com.example.wastemanagementvrushabh.databinding.FragmentGalleryBinding;
import com.example.wastemanagementvrushabh.ui.contact_us.SlideshowFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;

public class GalleryFragment extends Fragment {
    public GalleryFragment() {
        // Required empty public constructor
    }

    FragmentGalleryBinding binding;
    private FusedLocationProviderClient client;
    ProgressDialog progressDialog, progressDialog2, progressDialog3;
    private Uri imageUri;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    CountryCodePicker codePicker;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;


    //Firebase connection


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
//        ((AppCompatActivity)getActivity()).getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        //progress dialog for camera
        ((MainActivity)getActivity()).getSupportActionBar().show();
        ((MainActivity)getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Sending OTP on Your Phone number");

        //progress dialog for location
        progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setTitle("Loading");
        progressDialog2.setMessage("Getting your current Location....");

        //Progress dialog for Uploading image to server
        progressDialog3 = new ProgressDialog(getContext());
        progressDialog3.setTitle("Uploading");
        progressDialog3.setMessage("Uploading your Image.......");

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ;


        //Camera button......
//        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    openCamera();
//                } else {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{
//                            Manifest.permission.CAMERA
//                    }, 101);
//                }
//
//            }
//        });

        //Location button code......

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });


        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    progressDialog.dismiss();
                    getCurrentLocation();
                    progressDialog2.dismiss();


                } else {
                    //when permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest
                            .permission.ACCESS_COARSE_LOCATION}, 100
                    );
                }
            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!verificationInProgress) {
                    if (imageUri != null && !binding.etName.getText().toString().isEmpty() && !binding.etPhone.getText().toString()
                            .isEmpty()) {

                        String phoneNum = "+" + binding.ccp.getSelectedCountryCode() + binding.etPhone.getText().toString();
                        Toast.makeText(getActivity(), phoneNum, Toast.LENGTH_SHORT).show();
                        progressDialog.show();


//                    uploadToFirebase(imageUri);
                        requestOTP(phoneNum);
                    } else {
                        binding.etPhone.setError("Enter Valid Phone Number");
                        binding.etName.setError("Enter your Name");
                        Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String userOTP = binding.etVerifyOTP.getText().toString();
                    if (!userOTP.isEmpty() && userOTP.length() == 6) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);
                        verifyAuth(credential);
                    } else {
                        binding.etVerifyOTP.setError("Enter a valid OTP");
                    }
                }
            }
        });

        binding.btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (imageUri != null && !binding.etName.getText().toString().isEmpty() && !binding.etPhone.getText().toString()
                            .isEmpty()) {

                        String phoneNum = "+" + binding.ccp.getSelectedCountryCode() + binding.etPhone.getText().toString();
                        Toast.makeText(getActivity(), phoneNum, Toast.LENGTH_SHORT).show();
                        progressDialog.show();


//                    uploadToFirebase(imageUri);
                        requestOTP(phoneNum);
                    } else {
                        binding.etPhone.setError("Enter Valid Phone Number");
                        binding.etName.setError("Enter your Name");
                        Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_SHORT).show();
                    }
                }

        });

        return binding.getRoot();
    }


    //Verifying otp method
    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Authentication is successful", Toast.LENGTH_SHORT).show();
//                    Model model = new Model(binding.etName.getText().toString(),binding.etPhone.getText().toString(),binding.tvLocation
//                    .getText().toString());
                    String id = task.getResult().getUser().getUid();
//                    database.getReference().child("Users").child(id).setValue(model);
                    uploadToFirebase(imageUri,id);




                } else {
                    Toast.makeText(getActivity(), "Authentication is Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //request otp method firebase
    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressDialog.dismiss();
                verificationId = s;
                token = forceResendingToken;
                binding.etVerifyOTP.setVisibility(View.VISIBLE);
                binding.btnResendOTP.setVisibility(View.VISIBLE);
                verificationInProgress = true;
                binding.btnSubmit.setText("verify");
                binding.btnResendOTP.setEnabled(false);
                binding.etVerifyOTP.requestFocus();
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        binding.btnResendOTP.setText("" + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        binding.btnResendOTP.setText("resend");
                        binding.btnResendOTP.setEnabled(true);
                    }

                }.start();

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //upload to firebase method
    private void uploadToFirebase(Uri uri,String id) {

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uid = mAuth.getCurrentUser().getUid();
                        String status = "Not yet done";
                        String complaintId = root.child("complaints").push().getKey();
                        Model model = new Model(uri.toString(),binding.etName.getText().toString(),binding.etPhone.getText().toString()
                        ,binding.tvLatitude.getText().toString(),binding.tvLongitude.getText().toString(),uid,status,complaintId,false);
                        root.child(id).child("complaints").child(complaintId).setValue(model);
                        database.getReference("complaints").child(complaintId).setValue(model);
                        progressDialog3.dismiss();
                        Toast.makeText(getActivity(), "Image successfully Uploaded", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_nav_complaint_to_thanksFragment);

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog3.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog3.dismiss();
                Toast.makeText(getActivity(), "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExtension(Uri muri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }
//    //Open camera Method......
//    private void openCamera() {
//        progressDialog.show();
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 101);
//
//    }


    //Getting current location
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        progressDialog2.show();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        binding.tvLatitude.setText(String.valueOf(location.getLatitude()));
                        binding.tvLongitude.setText(String.valueOf(location.getLongitude()));
                        Toast.makeText(getActivity(), "Location get Successfully", Toast.LENGTH_SHORT).show();
                        if (binding.tvLatitude.getText().length() != 0 && binding.tvLongitude.getText().length() != 0) {
                            binding.btnLocation.setText("Done");
                            binding.btnLocation.setEnabled(false);
                        }
                    } else {
                        //When location result is null
                        //Initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        //Initialize location callback
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();
                                binding.tvLatitude.setText(String.valueOf(location1.getLatitude()));
                                binding.tvLongitude.setText(String.valueOf(location1.getLongitude()));

                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    //if location is off manually then this code work
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check condition

        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //when permission granted call method
            getCurrentLocation();
        } else {
            //when permission are denied
            Toast.makeText(getContext(), "Perimssion Denied", Toast.LENGTH_SHORT).show();
        }
    }

//    //saving capture image .....
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        progressDialog.show();
//
//        if (requestCode == 101) {
//            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
//            //Get Capture image
//            Toast.makeText(getActivity(), "Capture image successfully", Toast.LENGTH_SHORT).show();
//            binding.tvCamera.setText("Captured");
//            progressDialog.dismiss();
//
//        }
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Toast.makeText(getActivity(), "Image selected successfully", Toast.LENGTH_SHORT).show();
            binding.tvCamera.setText("captured");

        }
    }
}