package com.example.appthanhlong.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.appthanhlong.receiver.KhuBaReceiver;
import com.example.appthanhlong.receiver.KhuBonReceiver;
import com.example.appthanhlong.receiver.KhuHaiReceiver;
import com.example.appthanhlong.receiver.KhuMotReceiver;
import com.example.appthanhlong.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class DieuKhienFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference khu1 = database.getReference("DieuKien/Khu1");
    DatabaseReference khu2 = database.getReference("DieuKien/Khu2");
    DatabaseReference khu3 = database.getReference("DieuKien/Khu3");
    DatabaseReference khu4 = database.getReference("DieuKien/Khu4");

    DatabaseReference TGkhu1 = database.getReference("ThoiGian/MoKhu1");
    DatabaseReference TGkhu2 = database.getReference("ThoiGian/MoKhu2");
    DatabaseReference TGkhu3 = database.getReference("ThoiGian/MoKhu3");
    DatabaseReference TGkhu4 = database.getReference("ThoiGian/MoKhu4");

    DatabaseReference nhietdodb = database.getReference("ThongSo/NhietDo");
    DatabaseReference doamdb = database.getReference("ThongSo/DoAm");
    DatabaseReference doamdatdb = database.getReference("ThongSo/DoAmDat");
    DatabaseReference mucnuocdb = database.getReference("ThongSo/MucNuoc");

    int nhietdo;
    int doamkh;
    int doamdat;
    int mucnuoc;

    Calendar calendar = Calendar.getInstance();
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    int currentMinute = calendar.get(Calendar.MINUTE);
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    SharedPreferences sharedPreferences;
    TextToSpeech toSpeech;
    private TextView txtSpeechInput;
    private ImageView btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Read Nhiet Do
        nhietdodb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int tsnhiet = dataSnapshot.getValue(Integer.class);
                nhietdo = tsnhiet;

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Read Do Am
        doamdb.addValueEventListener(new ValueEventListener() {
            @Override
            final public void onDataChange(DataSnapshot dataSnapshot) {
                final int tsdoam = dataSnapshot.getValue(Integer.class);
                doamkh = tsdoam;
            }

            @Override
            final public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // Read Do Am Dat
        doamdatdb.addValueEventListener(new ValueEventListener() {
            @Override
            final public void onDataChange(DataSnapshot dataSnapshot) {
                final int tsdoamdat = dataSnapshot.getValue(Integer.class);
                doamdat = tsdoamdat;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Read Muc Nuoc
        mucnuocdb.addValueEventListener(new ValueEventListener() {
            @Override
            final public void onDataChange(DataSnapshot dataSnapshot) {
                final int tsmucnuoc = dataSnapshot.getValue(Integer.class);
                mucnuoc = tsmucnuoc;
            }

            @Override
            final public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.getDefault());
                }
            }
        });


        View view = inflater.inflate(R.layout.fragment_dieu_khien, container, false);
        final com.suke.widget.SwitchButton switch1 = (com.suke.widget.SwitchButton) view.findViewById(R.id.khu1);
        final com.suke.widget.SwitchButton switch2 = (com.suke.widget.SwitchButton) view.findViewById(R.id.khu2);
        final com.suke.widget.SwitchButton switch3 = (com.suke.widget.SwitchButton) view.findViewById(R.id.khu3);
        final com.suke.widget.SwitchButton switch4 = (com.suke.widget.SwitchButton) view.findViewById(R.id.khu4);
        com.suke.widget.SwitchButton switch5 = (com.suke.widget.SwitchButton) view.findViewById(R.id.tatca);
        txtSpeechInput = (TextView) view.findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageView) view.findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        ImageView hengiokhu1 = (ImageView) view.findViewById(R.id.hengiokhu1);
        ImageView hengiokhu2 = (ImageView) view.findViewById(R.id.hengiokhu2);
        ImageView hengiokhu3 = (ImageView) view.findViewById(R.id.hengiokhu3);
        ImageView hengiokhu4 = (ImageView) view.findViewById(R.id.hengiokhu4);
        final Intent intent1 = new Intent(getActivity(), KhuMotReceiver.class);
        final Intent intent2 = new Intent(getActivity(), KhuHaiReceiver.class);
        final Intent intent3 = new Intent(getActivity(), KhuBaReceiver.class);
        final Intent intent4 = new Intent(getActivity(), KhuBonReceiver.class);
        final TextView tvtimekhu1 = (TextView) view.findViewById(R.id.tvtimekhu1);
        final TextView tvtimekhu2 = (TextView) view.findViewById(R.id.tvtimekhu2);
        final TextView tvtimekhu3 = (TextView) view.findViewById(R.id.tvtimekhu3);
        final TextView tvtimekhu4 = (TextView) view.findViewById(R.id.tvtimekhu4);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        calendar = Calendar.getInstance();

//==================================1==================================================
        hengiokhu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDaykhu1, int minuteskhu1) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDaykhu1);
                        calendar.set(Calendar.MINUTE, minuteskhu1);
                        String thoigian1 = (hourOfDaykhu1 + ":" + minuteskhu1);
                        TGkhu1.setValue(thoigian1);
                        pendingIntent = PendingIntent.getBroadcast(
                                getActivity(), 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + (5 * 1000), pendingIntent);
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
//===================================2=================================================
        hengiokhu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDaykhu2, int minuteskhu2) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDaykhu2);
                        calendar.set(Calendar.MINUTE, minuteskhu2);
                        String thoigian2 = (hourOfDaykhu2 + ":" + minuteskhu2);
                        TGkhu2.setValue(thoigian2);
                        pendingIntent = PendingIntent.getBroadcast(
                                getActivity(), 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + (5 * 1000), pendingIntent);

                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
//=================================3===================================================
        hengiokhu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDaykhu3, int minuteskhu3) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDaykhu3);
                        calendar.set(Calendar.MINUTE, minuteskhu3);
                        String thoigian3 = (hourOfDaykhu3 + ":" + minuteskhu3);
                        TGkhu3.setValue(thoigian3);
                        pendingIntent = PendingIntent.getBroadcast(
                                getActivity(), 0, intent3, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + (5 * 1000), pendingIntent);

                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
//==================================4==================================================
        hengiokhu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDaykhu4, int minuteskhu4) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDaykhu4);
                        calendar.set(Calendar.MINUTE, minuteskhu4);
                        String thoigian4 = (hourOfDaykhu4 + ":" + minuteskhu4);
                        TGkhu4.setValue(thoigian4);
                        pendingIntent = PendingIntent.getBroadcast(
                                getActivity(), 0, intent4, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + (5 * 1000), pendingIntent);

                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
//====================================================================================

        // Read time1
        TGkhu1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String timekhu1 = dataSnapshot.getValue(String.class);
                tvtimekhu1.setText(timekhu1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // Read time2
        TGkhu2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String timekhu2 = dataSnapshot.getValue(String.class);
                tvtimekhu2.setText(timekhu2);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // Read time3
        TGkhu3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String timekhu3 = dataSnapshot.getValue(String.class);
                tvtimekhu3.setText(timekhu3);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        // Read time4
        TGkhu4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String timekhu4 = dataSnapshot.getValue(String.class);
                tvtimekhu4.setText(timekhu4);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // tai du lieu khu tuoi
        khu1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k1 = dataSnapshot.getValue(Integer.class);
                if (k1 == 1) {
                    switch1.setChecked(true);
                } else {
                    switch1.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // tai du lieu khu tuoi
        khu2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k2 = dataSnapshot.getValue(Integer.class);
                if (k2 == 1) {
                    switch2.setChecked(true);
                } else {
                    switch2.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // tai du lieu khu tuoi
        khu3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k3 = dataSnapshot.getValue(Integer.class);
                if (k3 == 1) {
                    switch3.setChecked(true);
                } else {
                    switch3.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // tai du lieu khu tuoi
        khu4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k4 = dataSnapshot.getValue(Integer.class);
                if (k4 == 1) {
                    switch4.setChecked(true);
                } else {
                    switch4.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        switch1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked == true) {
                    khu1.setValue(1);
                } else {
                    khu1.setValue(0);
                }

            }
        });

        switch2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked == true) {
                    khu2.setValue(1);
                } else {
                    khu2.setValue(0);
                }

            }
        });

        switch3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked == true) {
                    khu3.setValue(1);
                } else {
                    khu3.setValue(0);
                }

            }
        });

        switch4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked == true) {
                    khu4.setValue(1);
                } else {
                    khu4.setValue(0);
                }

            }
        });


        switch5.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if (isChecked == true) {
                    switch1.setChecked(true);
                    switch2.setChecked(true);
                    switch3.setChecked(true);
                    switch4.setChecked(true);
                    Toast.makeText(getContext(), "Bật Tưới Tất Cả", Toast.LENGTH_SHORT).show();
                } else {
                    switch1.setChecked(false);
                    switch2.setChecked(false);
                    switch3.setChecked(false);
                    switch4.setChecked(false);
                    Toast.makeText(getContext(), "Tắt Tưới Tất Cả", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //=======================================

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String voice = result.get(0).toLowerCase();
                    txtSpeechInput.setText(voice);

                    switch (voice) {
                        case "mở khu số 1":
                            khu1.setValue(1);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã mở khu 1", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "mở khu số 2":
                            khu2.setValue(1);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã mở khu 2", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "mở khu số 3":
                            khu3.setValue(1);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã mở khu 3", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "mở khu số 4":
                            khu4.setValue(1);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã mở khu 4", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "tắt khu số 1":
                            khu1.setValue(0);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã tắt khu 1", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "tắt khu số 2":
                            khu2.setValue(0);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã tắt khu 2", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "tắt khu số 3":
                            khu3.setValue(0);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã tắt khu 3", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "tắt khu số 4":
                            khu4.setValue(0);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã tắt khu 4", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "thông số nông trại":
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());

                                        toSpeech.speak("Nhiệt độ hiện tại là" + nhietdo + "độ sê," + ", độ ẩm không khí là " + doamkh + "phần trăm," + "độ ẩm đất là" + doamdat + "phần trăm," + "mực nước có trong bể là" + mucnuoc + "phần trăm", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "mở tất cả":
                            khu1.setValue(1);
                            khu2.setValue(1);
                            khu3.setValue(1);
                            khu4.setValue(1);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã mở tất cả hệ thống", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                        case "tắt tất cả":
                            khu1.setValue(0);
                            khu2.setValue(0);
                            khu3.setValue(0);
                            khu4.setValue(0);
                            toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        toSpeech.setLanguage(Locale.getDefault());
                                        toSpeech.speak("đã tắt tất cả hệ thống", TextToSpeech.QUEUE_FLUSH, null, null);
                                    }
                                }
                            });
                            break;
                    }
                }
                break;
            }

        }
    }

}
