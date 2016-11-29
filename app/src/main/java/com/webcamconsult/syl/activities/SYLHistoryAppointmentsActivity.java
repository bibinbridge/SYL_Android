package com.webcamconsult.syl.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.webcamconsult.syl.R;
import com.webcamconsult.syl.adapters.SYLAppointmentsAdapter;
import com.webcamconsult.syl.databaseaccess.SYLAppointmentdetailsdatamanager;
import com.webcamconsult.syl.databaseaccess.SYLHistoryAppointmentsdatamanager;
import com.webcamconsult.syl.interfaces.SYLAppointmentRemoveListener;
import com.webcamconsult.syl.interfaces.SYLDeleteHistoryAppoitmentsListener;
import com.webcamconsult.syl.interfaces.SYLFetchHistoryAppointmentsListener;
import com.webcamconsult.syl.interfaces.SYLFinishedAppointmentListener;
import com.webcamconsult.syl.model.SYLAppointmentRemoveResponseModel;
import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;
import com.webcamconsult.syl.model.SYLFetchAppointmentsResponseModel;
import com.webcamconsult.syl.model.SYLFinishAppointmentResponseModel;
import com.webcamconsult.syl.modelmanager.SYLHistoryAppointmentsModelManager;
import com.webcamconsult.syl.providers.SYLSaveValues;
import com.webcamconsult.syl.swiperefresh.SwipeDismissListViewTouchListener;
import com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout;
import com.webcamconsult.syl.utilities.SYLUtil;
import com.webcamconsult.viewmanager.SYLAppointmentRemoveViewManager;
import com.webcamconsult.viewmanager.SYLDeleteHistoryAppointmentsViewmanager;
import com.webcamconsult.viewmanager.SYLFinishAppointmentViewManager;
import com.webcamconsult.viewmanager.SYLHistoryAppointmentsViewManager;

import java.util.ArrayList;

/**
 * Created by Sandeep on 11/17/2015.
 */
public class SYLHistoryAppointmentsActivity extends Activity implements com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout.OnRefreshListener,
        SYLFetchHistoryAppointmentsListener,SYLAppointmentRemoveListener,SYLFinishedAppointmentListener,SYLDeleteHistoryAppoitmentsListener {
    private com.webcamconsult.syl.swiperefresh.SwipeRefreshLayout swipeView;
    public String accesstoken, userid, GMTtimezone;
    public String appointmentid,delete_appointmentid,finishAppointmentid;
    ArrayList<SYLFetchAppointmentsDetails> todaysAppointmentsdetailarraylist;
    ArrayList<SYLFetchAppointmentsDetails> requestReceiveddetailarraylist;
    ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsdetailarraylist;
    ArrayList<SYLFetchAppointmentsDetails> requestSenddetailarraylist;
    ArrayList<SYLFetchAppointmentsDetails> cancelledRequestdetailarraylist;
    ArrayList<SYLFetchAppointmentsDetails> finishedAppointmentarraylist;
    String method="get";
    ProgressDialog msylProgressDialog;
    SYLAppointmentsAdapter sYLAppointmentsAdapter;
    ListView listView;
    View headerView;
    ImageView mbackArrowImageView;
LinearLayout  mLinearlayoutHistory;
    BroadcastReceiver br;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                finish();
            }
        };
        registerReceiver(
                br, intentFilter);






        setContentView(R.layout.syl_appointmentshistorylayout);
        listView = (ListView) findViewById(android.R.id.list);
        mLinearlayoutHistory=(LinearLayout)findViewById(R.id.liout_history);
        mLinearlayoutHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteHistoryAppointments();
            }
        });
        headerView = this.getLayoutInflater().inflate(
                R.layout.empty_search_result_cell, null);
mbackArrowImageView=(ImageView)findViewById(R.id.img_backarrow);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        setswipeviewProperties();
        if(SYLUtil.isNetworkAvailable(SYLHistoryAppointmentsActivity.this))
        {

            /*
            if (SYLUtil.checkHistoryAppointmentsCacheddata(SYLHistoryAppointmentsActivity.this))
            {
                getHistoryAppointmentsfromLocalDB();
            } else {
                getHistoryAppointments();
            }
*/
            getHistoryAppointments();

        }
else {
            listView.addHeaderView(headerView);
            if (SYLUtil.checkHistoryAppointmentsCacheddata(SYLHistoryAppointmentsActivity.this)) {
                getHistoryAppointmentsfromLocalDB();
            }
            else {
                Toast.makeText(SYLHistoryAppointmentsActivity.this,"No Appointment found",Toast.LENGTH_LONG).show();
            }
        }
mbackArrowImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      onBackPressed();
    }
});
    }



    private void getHistoryAppointments()
    {
        if(SYLUtil.isNetworkAvailable(SYLHistoryAppointmentsActivity.this)) {
            listView.removeHeaderView(headerView);
            msylProgressDialog = new ProgressDialog(SYLHistoryAppointmentsActivity.this);
            msylProgressDialog.setMessage("Please wait...");
            msylProgressDialog.setCancelable(false);
            msylProgressDialog.setCanceledOnTouchOutside(false);
            msylProgressDialog.show();
            GMTtimezone = SYLUtil.getTimeGMTZone(SYLHistoryAppointmentsActivity.this);
            accesstoken = SYLSaveValues.getSYLaccessToken(SYLHistoryAppointmentsActivity.this);
            userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
            SYLHistoryAppointmentsViewManager mHistoryappointmentsmanager = new SYLHistoryAppointmentsViewManager();
            mHistoryappointmentsmanager.mFetchhistoryAppointmentsListener = SYLHistoryAppointmentsActivity.this;
            mHistoryappointmentsmanager.getHistoryAppointments(method, getBaseContext(), accesstoken, userid, GMTtimezone);
        }
        else {
            Toast.makeText(SYLHistoryAppointmentsActivity.this,"Please check your network connection",Toast.LENGTH_LONG).show();        }


    }
  private void   getHistoryAppointmentsfromLocalDB()
  {
      try {
          sYLAppointmentsAdapter = new SYLAppointmentsAdapter(SYLHistoryAppointmentsActivity.this);
          SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
                  SYLHistoryAppointmentsActivity.this);
          ArrayList<SYLFetchAppointmentsDetails> requestReceivedarraylistcached = datamanager
                  .getHistoryAppointmentDetails("REQUEST RECEIVED");
          ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsarraylistcached = datamanager
                  .getHistoryAppointmentDetails("OTHER CONFIRMED");
          ArrayList<SYLFetchAppointmentsDetails> requestSendarraylistcached = datamanager
                  .getHistoryAppointmentDetails("REQUEST SENT");
          ArrayList<SYLFetchAppointmentsDetails> cancelledRequestarraylistcached = datamanager
                  .getHistoryAppointmentDetails("CANCELLED");
          ArrayList<SYLFetchAppointmentsDetails> finishedappointmentsarraylistcached = datamanager
                  .getHistoryAppointmentDetails("FINISHED");
          ArrayList<SYLFetchAppointmentsDetails> todaysappointmentsarraylistcached = datamanager
                  .getHistoryAppointmentDetails("TODAYS CONFIRMED");

          if ((todaysappointmentsarraylistcached.size() < 1)
                  &&
                  (requestReceivedarraylistcached.size() < 1)
                  &&
                  (confirmedAppointmentsarraylistcached.size() < 1)
                  &&
                  (requestSendarraylistcached.size() < 1)
                  &&
                  (cancelledRequestarraylistcached.size() < 1)
                  &&
                  (finishedappointmentsarraylistcached.size() < 1)
                  ) {

              SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this, "No appointments found");
              return;
          }


          if (todaysappointmentsarraylistcached.size() > 0) {
              sYLAppointmentsAdapter
                      .addSectionHeaderItem("Today's Appointments");
              for (SYLFetchAppointmentsDetails todaysAppointments : todaysappointmentsarraylistcached) {
                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(todaysAppointments.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = todaysAppointments.getParticipant()
                              .getName();
                      profile_image = todaysAppointments.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = todaysAppointments.getOrganizer().getName();
                      profile_image = todaysAppointments.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }
                  sYLAppointmentsAdapter.addItem(name + "_" + ""

                                  + "_"
                                  + todaysAppointments.getAppointmentDate1()
                                  + "_"
                                  + todaysAppointments.getAppointmentTime1()
                                  + "_"
                                  + profile_image
                                  + "_"
                                  + todaysAppointments.getListingType()
                                  + "_"
                                  + todaysAppointments.getAppointmentID()
                                  + "_"
                                  + appointment_indicator


                  );
              }
          }
          if (requestReceivedarraylistcached.size() > 0) {
              sYLAppointmentsAdapter.addSectionHeaderItem("Request Received");

              for (SYLFetchAppointmentsDetails reqReceived : requestReceivedarraylistcached) {
                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(reqReceived.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = reqReceived.getParticipant()
                              .getName();
                      profile_image = reqReceived.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = reqReceived.getOrganizer().getName();
                      profile_image = reqReceived.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }
                  sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                  + reqReceived.getAppointmentDate1() + "_"
                                  + reqReceived.getAppointmentTime1() + "_"
                                  + profile_image + "_"
                                  + reqReceived.getListingType() + "_"
                                  + reqReceived.getAppointmentID()
                                  + "_"
                                  + appointment_indicator

                  );

              }
          }
          if (confirmedAppointmentsarraylistcached.size() > 0) {
              sYLAppointmentsAdapter.addSectionHeaderItem("Confirmed Appointments");

              for (SYLFetchAppointmentsDetails confAppointment : confirmedAppointmentsarraylistcached) {
                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(confAppointment.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = confAppointment.getParticipant()
                              .getName();
                      profile_image = confAppointment.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = confAppointment.getOrganizer().getName();
                      profile_image = confAppointment.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }


                  sYLAppointmentsAdapter.addItem(name + "_"
                                  + "" + "_"
                                  + confAppointment.getAppointmentDate1() + "_"
                                  + confAppointment.getAppointmentTime1() + "_"
                                  + profile_image + "_"
                                  + confAppointment.getListingType() + "_"
                                  + confAppointment.getAppointmentID()
                                  + "_"
                                  + appointment_indicator


                  );

              }
          }
          if (requestSendarraylistcached.size() > 0) {
              sYLAppointmentsAdapter.addSectionHeaderItem("Request Sent");
              for (SYLFetchAppointmentsDetails reqSend : requestSendarraylistcached) {


                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(reqSend.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = reqSend.getParticipant()
                              .getName();
                      profile_image = reqSend.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = reqSend.getOrganizer().getName();
                      profile_image = reqSend.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }

                  sYLAppointmentsAdapter.addItem(
                          name + "_"
                                  + ""
                                  + "_" + reqSend.getAppointmentDate1() + "_"
                                  + reqSend.getAppointmentTime1() + "_"
                                  + profile_image
                                  + "_"
                                  + reqSend.getListingType() + "_"
                                  + reqSend.getAppointmentID() + "_"
                                  + appointment_indicator


                  );

              }


          }
          if (cancelledRequestarraylistcached.size() > 0) {
              sYLAppointmentsAdapter.addSectionHeaderItem("Cancelled Request");
              for (SYLFetchAppointmentsDetails cancelReq : cancelledRequestarraylistcached) {

                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(cancelReq.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = cancelReq.getParticipant()
                              .getName();
                      profile_image = cancelReq.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = cancelReq.getOrganizer().getName();
                      profile_image = cancelReq.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }


                  sYLAppointmentsAdapter.addItem(name + "_"
                                  + " " + "_" + cancelReq.getAppointmentDate1() + "_"
                                  + cancelReq.getAppointmentTime1() + "_" + profile_image
                                  + "_"
                                  + cancelReq.getListingType()
                                  + "_"
                                  + cancelReq.getAppointmentID()
                                  + "_"
                                  + appointment_indicator


                  );
              }
          }
          if (finishedappointmentsarraylistcached.size() > 0) {
              sYLAppointmentsAdapter
                      .addSectionHeaderItem("Finished Appointments");
              for (SYLFetchAppointmentsDetails finishedAppointments : finishedappointmentsarraylistcached) {
                  String name, profile_image, appointment_indicator;
                  String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                  String organizer_id = Integer.toString(finishedAppointments.getOrganizer().getUserId());
                  if (curr_userid.equals(organizer_id)) {
                      name = finishedAppointments.getParticipant()
                              .getName();
                      profile_image = finishedAppointments.getParticipant().getProfileImage();
                      appointment_indicator = "createdbyuser";
                  } else {
                      name = finishedAppointments.getOrganizer().getName();
                      profile_image = finishedAppointments.getOrganizer().getProfileImage();
                      appointment_indicator = "createdbyothers";
                  }
                  sYLAppointmentsAdapter.addItem(name + "_"
                                  + " " + "_" + finishedAppointments.getAppointmentDate1() + "_"
                                  + finishedAppointments.getAppointmentTime1() + "_" + profile_image
                                  + "_"
                                  + finishedAppointments.getListingType()
                                  + "_"
                                  + finishedAppointments.getAppointmentID()
                                  + "_"
                                  + appointment_indicator
                  );


              }


          }
          listView.setAdapter(sYLAppointmentsAdapter);
      }
          catch(Exception e)
          {
             Log.e("error",e.getMessage());
          }

      listView.setOnItemLongClickListener(mLongclickListener);
      listView.setOnItemClickListener(mOnItemClickListener);
      }



    private SYLFetchAppointmentsDetails getHistoryAppointmentDetailsFromDB(
            String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
        SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
                SYLHistoryAppointmentsActivity.this);
        mfetchappointmentdetailsobject = datamanager
                .getHistoryAppointmentFetchDetails("TodaysAppointments",
                        appointmentid);
        return mfetchappointmentdetailsobject;
    }
    @Override
    public void getHistoryAppointmentDetailsFinish(SYLFetchAppointmentsResponseModel mFetchappointmentsResponseModel) {
    try
    {

        if (mFetchappointmentsResponseModel != null) {
            todaysAppointmentsdetailarraylist = mFetchappointmentsResponseModel
                    .getTodaysAppointments();

            requestReceiveddetailarraylist = mFetchappointmentsResponseModel
                    .getRequestReceived();
            confirmedAppointmentsdetailarraylist = mFetchappointmentsResponseModel
                    .getConfirmedAppointments();

            requestSenddetailarraylist = mFetchappointmentsResponseModel
                    .getRequestSend();
            cancelledRequestdetailarraylist = mFetchappointmentsResponseModel
                    .getCancelledRequest();
            finishedAppointmentarraylist=mFetchappointmentsResponseModel
                    .getFinishedAppointments();
            sYLAppointmentsAdapter = new SYLAppointmentsAdapter(SYLHistoryAppointmentsActivity.this);
            if (todaysAppointmentsdetailarraylist.size() > 0) {
                sYLAppointmentsAdapter
                        .addSectionHeaderItem("Today's Appointments");

                for (SYLFetchAppointmentsDetails todaysAppointments : todaysAppointmentsdetailarraylist) {

                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(todaysAppointments.getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = todaysAppointments.getParticipant()
                                .getName();
                        profile_image = todaysAppointments.getParticipant().getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = todaysAppointments.getOrganizer().getName();
                        profile_image = todaysAppointments.getOrganizer().getProfileImage();
                        appointment_indicator="createdbyothers";
                    }


                    sYLAppointmentsAdapter.addItem(name + "_" + ""

                                    + "_"
                                    + todaysAppointments.getAppointmentDate1()
                                    + "_"
                                    + todaysAppointments.getAppointmentTime1()
                                    + "_"
                                    + profile_image
                                    + "_"
                                    + todaysAppointments.getListingType()
                                    + "_"
                                    + todaysAppointments.getAppointmentID()
                                    + "_"
                                    +appointment_indicator


                    );

                }








            }
            if (requestReceiveddetailarraylist.size() > 0) {
                sYLAppointmentsAdapter.addSectionHeaderItem("Request Received");

                for (SYLFetchAppointmentsDetails reqReceived : requestReceiveddetailarraylist) {
                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues
                            .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(reqReceived
                            .getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = reqReceived.getParticipant().getName();
                        profile_image = reqReceived.getParticipant()
                                .getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = reqReceived.getOrganizer().getName();
                        profile_image = reqReceived.getOrganizer()
                                .getProfileImage();
                        appointment_indicator="createdbyothers";
                    }

                    sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                    + reqReceived.getAppointmentDate1() + "_"
                                    + reqReceived.getAppointmentTime1() + "_"
                                    + profile_image + "_"
                                    + reqReceived.getListingType() + "_"
                                    + reqReceived.getAppointmentID()
                                    + "_"
                                    +appointment_indicator

                    );

                }
            }
            if (confirmedAppointmentsdetailarraylist.size() > 0) {
                sYLAppointmentsAdapter
                        .addSectionHeaderItem("Confirmed Appointments");

                for (SYLFetchAppointmentsDetails confAppointment : confirmedAppointmentsdetailarraylist) {
                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues
                            .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(confAppointment
                            .getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = confAppointment.getParticipant().getName();
                        profile_image = confAppointment.getParticipant()
                                .getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = confAppointment.getOrganizer().getName();
                        profile_image = confAppointment.getOrganizer()
                                .getProfileImage();
                        appointment_indicator="createdbyothers";
                    }
                    sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                    + confAppointment.getAppointmentDate1() + "_"
                                    + confAppointment.getAppointmentTime1() + "_"
                                    + profile_image + "_"
                                    + confAppointment.getListingType() + "_"
                                    + confAppointment.getAppointmentID()
                                    + "_"
                                    +appointment_indicator

                    );

                }
            }
            if (requestSenddetailarraylist.size() > 0) {
                sYLAppointmentsAdapter.addSectionHeaderItem("Request Sent");
                for (SYLFetchAppointmentsDetails reqSend : requestSenddetailarraylist) {
                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues
                            .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(reqSend
                            .getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = reqSend.getParticipant().getName();
                        profile_image = reqSend.getParticipant()
                                .getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = reqSend.getOrganizer().getName();
                        profile_image = reqSend.getOrganizer()
                                .getProfileImage();
                        appointment_indicator="createdbyothers";
                    }
                    sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                    + reqSend.getAppointmentDate1() + "_"
                                    + reqSend.getAppointmentTime1() + "_"
                                    + profile_image + "_" + reqSend.getListingType()
                                    + "_" + reqSend.getAppointmentID()
                                    + "_"
                                    +appointment_indicator

                    );

                }
            }
            if (cancelledRequestdetailarraylist.size() > 0) {
                sYLAppointmentsAdapter
                        .addSectionHeaderItem("Cancelled Request");
                for (SYLFetchAppointmentsDetails cancelReq : cancelledRequestdetailarraylist) {
                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues
                            .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(cancelReq
                            .getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = cancelReq.getParticipant().getName();
                        profile_image = cancelReq.getParticipant()
                                .getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = cancelReq.getOrganizer().getName();
                        profile_image = cancelReq.getOrganizer()
                                .getProfileImage();
                        appointment_indicator="createdbyothers";
                    }
                    sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                    + cancelReq.getAppointmentDate1() + "_"
                                    + cancelReq.getAppointmentTime1() + "_"
                                    + profile_image + "_" + cancelReq.getListingType()
                                    + "_" + cancelReq.getAppointmentID()
                                    + "_"
                                    +appointment_indicator
                    );

                }

            }
            if (finishedAppointmentarraylist.size() > 0) {
                sYLAppointmentsAdapter
                        .addSectionHeaderItem("Finished Appointments");
                for (SYLFetchAppointmentsDetails finishedAppointment : finishedAppointmentarraylist) {
                    String name, profile_image,appointment_indicator;
                    String curr_userid = SYLSaveValues
                            .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                    String organizer_id = Integer.toString(finishedAppointment
                            .getOrganizer().getUserId());
                    if (curr_userid.equals(organizer_id)) {
                        name = finishedAppointment.getParticipant().getName();
                        profile_image = finishedAppointment.getParticipant()
                                .getProfileImage();
                        appointment_indicator="createdbyuser";
                    } else {
                        name = finishedAppointment.getOrganizer().getName();
                        profile_image =finishedAppointment.getOrganizer()
                                .getProfileImage();
                        appointment_indicator="createdbyothers";
                    }
                    sYLAppointmentsAdapter.addItem(name + "_" + "" + "_"
                                    + finishedAppointment.getAppointmentDate1() + "_"
                                    + finishedAppointment.getAppointmentTime1() + "_"
                                    + profile_image + "_" + finishedAppointment.getListingType()
                                    + "_" + finishedAppointment.getAppointmentID()
                                    + "_"
                                    +appointment_indicator

                    );

                }

            }
            if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
                msylProgressDialog.dismiss();
            }
           swipeView.setRefreshing(false);
            listView.setAdapter(sYLAppointmentsAdapter);
            listView.setOnItemLongClickListener(mLongclickListener);
            listView.setOnItemClickListener(mOnItemClickListener);

        }
        else {
            if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
                msylProgressDialog.dismiss();
            }
            SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,
                    "Unexpected Server Error Happened");
        }
    }
    catch (Exception e)
    {
        if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
            msylProgressDialog.dismiss();
        }
        SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,
                "Unexpected Server Error Happened");

    }

    }
private void   setswipeviewProperties()
{
    swipeView.setOnRefreshListener(this);
    int color1=getResources().getColor(R.color.darkgreen);
    int color2=getResources().getColor(R.color.red);
    int color3=getResources().getColor(R.color.blue);
    int color4=getResources().getColor(R.color.banana_yellow);
    swipeView.setColorScheme(color1,color2,color3,color4);
    SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener(
                    listView ,
                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(int position) {
                            return true;
                        }

                        @Override
                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                            for (int position : reverseSortedPositions) {
                                //    mAdapter.remove(mAdapter.getItem(position));
                                //      arraylist.remove(0);
                                //       mContactsAdapter.notifyDataSetChanged();
                                try{
                                    Log.e("Dismiss position",""+position);
                                    String appointmentdetail=(String)  listView.getItemAtPosition(position);
                                    Log.e("value-h",appointmentdetail);
                                    if( ! ( appointmentdetail.equals("Confirmed Appointments")||appointmentdetail.equals("Request Send")|| appointmentdetail.equals("Cancelled Request")||  appointmentdetail.equals("Request Received")  ||    appointmentdetail.equals("Todays Appointments") )                  )
                                    {
                                        delete_appointmentid=SYLUtil.splitGetAppointmentid(appointmentdetail);
                                        Log.e("show alert dialog","show alert dialog");
                                        showDeleteAlertDialog();
                                    }

                                }

                                catch(Exception e)
                                {
                                    Log.e("error ",e.getMessage());
                                }
                            }

                        }
                    });


    listView.setOnTouchListener(touchListener);
    // Setting this scroll listener is required to ensure that during ListView scrolling,
    // we don't look for swipes.
    listView.setOnScrollListener(touchListener.makeScrollListener());















}
    @Override
    public void onRefresh()

    {
        swipeView.setRefreshing(true);

        SYLHistoryAppointmentsdatamanager mmanger = new SYLHistoryAppointmentsdatamanager(
                SYLHistoryAppointmentsActivity.this);
        mmanger.clearTable("appointmenthistory_details");
        getHistoryAppointments();
    }
    private void showDeleteAlertDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SYLHistoryAppointmentsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Delete SYL User");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to delete this appointment?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        doRemoveAppointment();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    private void doRemoveAppointment()
    {
        msylProgressDialog = new ProgressDialog(SYLHistoryAppointmentsActivity.this);
        msylProgressDialog.setMessage("Please wait...");
        msylProgressDialog.setCancelable(false);
        msylProgressDialog.setCanceledOnTouchOutside(false);
        msylProgressDialog.show();
        GMTtimezone = SYLUtil.getTimeGMTZone(SYLHistoryAppointmentsActivity.this);
        accesstoken = SYLSaveValues.getSYLaccessToken(SYLHistoryAppointmentsActivity.this);
        SYLAppointmentRemoveViewManager mViewmanager=new SYLAppointmentRemoveViewManager();
        mViewmanager.msylappointmentremoveListener=SYLHistoryAppointmentsActivity.this;
        mViewmanager.doRemoveAppointment(delete_appointmentid, accesstoken, GMTtimezone);
    }
    public void finishAppointmentRemove(SYLAppointmentRemoveResponseModel msylappointmentremoveResponseModel)
    {
        if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
            msylProgressDialog.dismiss();
        }
        // TODO Auto-generated method stub
        if(msylappointmentremoveResponseModel!=null)
        {
            if(msylappointmentremoveResponseModel.isSuccess())
            {
                //	progressdialogflag=true;
                getHistoryAppointments();
            }
            else {
                SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this, msylappointmentremoveResponseModel.getError().getErrorDetail());
            }
        }

    }
    AdapterView.OnItemLongClickListener mLongclickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
            Log.e("long clicked", "pos: " + pos);
            TextView header = (TextView) view.findViewById(R.id.textSeparator);
            if (header == null) {
                TextView tvappointmenttype = (TextView) view
                        .findViewById(R.id.tvappointmenttype);
                TextView tvappointmentid = (TextView) view
                        .findViewById(R.id.tvuserid);

                String appointmenttype = tvappointmenttype.getText().toString();
                finishAppointmentid = tvappointmentid.getText().toString();
                Log.e("Appointment type", appointmenttype);
                Log.e("Appointment id", finishAppointmentid);
                if (appointmenttype.equals("TODAYS CONFIRMED") || appointmenttype.equals("OTHER CONFIRMED")) {
                    showAppointmentFinishAlertDialog();
                }

            }
            return true;
        }
    };
    AdapterView.OnItemClickListener mOnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String appointmenttype = "";
            String appointmentid = "";
            String headervalue;
            Log.e("itemclick", "item click");
            TextView header = (TextView) view
                    .findViewById(R.id.textSeparator);
            if (header == null) {

                TextView tvappointmenttype = (TextView) view
                        .findViewById(R.id.tvappointmenttype);
                TextView tvappointmentid = (TextView) view
                        .findViewById(R.id.tvuserid);

                appointmenttype = tvappointmenttype.getText().toString();
                appointmentid = tvappointmentid.getText().toString();
                Log.e("Appointment type", appointmenttype);
                Log.e("Appointment id", appointmentid);

                if (appointmenttype.equals("REQUEST RECEIVED")) {
                    SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
                    if (mfetchappointmentdetails != null) {
                        Intent intent = new Intent(
                                SYLHistoryAppointmentsActivity.this,
                                SYLRequestReceivedAppointmentActivity.class);
                        String curr_userid = SYLSaveValues
                                .getSYLUserID(SYLHistoryAppointmentsActivity.this);
                        String organizer_id = Integer
                                .toString(mfetchappointmentdetails
                                        .getOrganizer().getUserId());
                        String participnat_id = Integer
                                .toString(mfetchappointmentdetails
                                        .getParticipant().getUserId());
                        if (curr_userid.equals(organizer_id)) {
                            intent.putExtra("name", mfetchappointmentdetails
                                    .getParticipant().getName());
                            int participantid = mfetchappointmentdetails
                                    .getParticipant().getUserId();
                            intent.putExtra("participantid",
                                    Integer.toString(participantid));
                            intent.putExtra("profile_image",
                                    mfetchappointmentdetails.getParticipant()
                                            .getProfileImage());
                            intent.putExtra("mobile", mfetchappointmentdetails
                                    .getParticipant().getMobile());
                            if (mfetchappointmentdetails
                                    .getParticipant().getSkypeId().equals("") || mfetchappointmentdetails
                                    .getParticipant().getSkypeId().equals("Not Available")) {
                                intent.putExtra("skypeid", "not available");
                            } else {
                                intent.putExtra("skypeid", mfetchappointmentdetails
                                        .getParticipant().getSkypeId());
                            }
                            if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
                            {
                                intent.putExtra("mobile", "not available");
                            }
                            else {
                                intent.putExtra("mobile", mfetchappointmentdetails
                                        .getParticipant().getMobile());
                            }

                            if (mfetchappointmentdetails
                                    .getParticipant().getHangoutId().equals("") ||
                                    mfetchappointmentdetails
                                            .getParticipant().getHangoutId().equals("Not Available")
                                    ) {
                                intent.putExtra("hangoutid", "not available"
                                );
                            } else {
                                intent.putExtra("hangoutid", mfetchappointmentdetails
                                        .getParticipant().getHangoutId());
                            }
                            if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("")||
                                    mfetchappointmentdetails.getParticipant().getFacetimeId().equals("Not Available")
                                    ) {
                                intent.putExtra("facetimeid", "not available");
                            } else {
                                intent.putExtra("facetimeid", mfetchappointmentdetails
                                        .getParticipant().getFacetimeId());
                            }

                        } else {
                            intent.putExtra("name", mfetchappointmentdetails
                                    .getOrganizer().getName());
                            int participantid = mfetchappointmentdetails
                                    .getOrganizer().getUserId();
                            intent.putExtra("participantid",
                                    Integer.toString(participantid));
                            intent.putExtra("profile_image",
                                    mfetchappointmentdetails.getOrganizer()
                                            .getProfileImage());

                            if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
                            {
                                intent.putExtra("mobile", "not available");
                            }
                            else {
                                intent.putExtra("mobile", mfetchappointmentdetails
                                        .getOrganizer().getMobile());
                            }

                            if (mfetchappointmentdetails
                                    .getOrganizer().getSkypeId().equals("") || mfetchappointmentdetails
                                    .getOrganizer().getSkypeId().equals("Not Available")       ) {
                                intent.putExtra("skypeid", "not available");
                            } else {
                                intent.putExtra("skypeid", mfetchappointmentdetails
                                        .getOrganizer().getSkypeId());
                            }

                            if (mfetchappointmentdetails.getOrganizer().getHangoutId().equals("")
                                    || mfetchappointmentdetails.getOrganizer().getHangoutId().equals("Not Available")          ) {
                                intent.putExtra("hangoutid", "not available");
                            } else {
                                intent.putExtra("hangoutid", mfetchappointmentdetails
                                        .getOrganizer().getHangoutId());
                            }
                            if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("")||
                                    mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")
                                    ) {
                                intent.putExtra("facetimeid", "not available");
                            } else {
                                intent.putExtra("facetimeid", mfetchappointmentdetails
                                        .getOrganizer().getFacetimeId());
                            }

                        }
                        intent.putExtra("topic",
                                mfetchappointmentdetails.getTopic());
                        intent.putExtra("description",
                                mfetchappointmentdetails.getDescription());


                        intent.putExtra("priority1", mfetchappointmentdetails
                                .getAppointmentPriority1());
                        if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
                            intent.putExtra("priority2", "not available");
                        } else {
                            intent.putExtra("priority2", mfetchappointmentdetails
                                    .getAppointmentPriority2());
                        }
                        if (mfetchappointmentdetails
                                .getAppointmentPriority3().equals("")) {
                            intent.putExtra("priority3", "not available");
                        } else {
                            intent.putExtra("priority3", mfetchappointmentdetails
                                    .getAppointmentPriority3());
                        }

                        intent.putExtra("appointmentid", appointmentid);
                        String date1 = mfetchappointmentdetails
                                .getAppointmentDate1();
                        String time1 = mfetchappointmentdetails
                                .getAppointmentTime1();
                        String date2 = mfetchappointmentdetails
                                .getAppointmentDate2();
                        String time2 = mfetchappointmentdetails
                                .getAppointmentTime2();
                        String date3 = mfetchappointmentdetails
                                .getAppointmentDate3();
                        String time3 = mfetchappointmentdetails
                                .getAppointmentTime3();
                        intent.putExtra("date1", date1 + " " + time1);
                        intent.putExtra("date2", date2 + " " + time2);
                        intent.putExtra("date3", date3 + " " + time3);
                        intent.putExtra("intentfrom","historyappointment");

                        startActivity(intent);
                        SYLHistoryAppointmentsActivity.this.finish();
                    }

                } else if (appointmenttype.equals("TODAYS CONFIRMED")) {
                    navigatetoTodaysAppointmentDetail(appointmentid);
                } else if (appointmenttype.equals("CANCELLED")) {
                    navigatetoCanelrequestDetailscreen(appointmentid);
                } else if (appointmenttype.equals("OTHER CONFIRMED")) {
                    navigatetoConfirmedAppointmentDetailScreen(appointmentid);
                } else if (appointmenttype.equals("REQUEST SENT")) {
                    navigatetoRequestsenddetailscreen(appointmentid);
                }
                else if (appointmenttype.equals("FINISHED")) {
                    navigatetoFinishedAppointmentDetailScreen(appointmentid);
                }





            } else {
                headervalue = header.getText().toString();
                Log.e("Header value", headervalue);
            }



        }
    };



















    private void showAppointmentFinishAlertDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SYLHistoryAppointmentsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Finish Appointment");

        // set dialog message
        alertDialogBuilder
                .setMessage("Would you like to mark this appointment as a finished?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        doFinishAppointment();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    private void doFinishAppointment()
    {
        if ( SYLUtil.isNetworkAvailable(SYLHistoryAppointmentsActivity.this)      ) {
            msylProgressDialog = new ProgressDialog(SYLHistoryAppointmentsActivity.this);
            msylProgressDialog.setMessage("Please wait...");
            msylProgressDialog.setCancelable(false);
            msylProgressDialog.setCanceledOnTouchOutside(false);
            msylProgressDialog.show();

            String timezone=SYLUtil.getTimeGMTZone(SYLHistoryAppointmentsActivity.this);
            SYLFinishAppointmentViewManager mViewmanager = new SYLFinishAppointmentViewManager();
            mViewmanager.mFinishAppointmentListener = SYLHistoryAppointmentsActivity.this;
            mViewmanager.doFinishAppointment(accesstoken,finishAppointmentid,timezone);
        } else {
            SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,
                    getString(R.string.network_alertmessage));
        }
    }

 public    void  finishAppointment(SYLFinishAppointmentResponseModel mFinishappointmentresponsemodel)
 {
     if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
         msylProgressDialog.dismiss();
     }
     try {
         if (mFinishappointmentresponsemodel.isSuccess()) {
          getHistoryAppointments();
         }
         else {
             SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,"Mark this appointment as finished is failed");
         }
     }
     catch (Exception e)
     {
         SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,"Unexpected server error happened");
     }
 }
    private SYLFetchAppointmentsDetails getHistoryAppointmentusingID(
            String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetailsobject = null;
        SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
                SYLHistoryAppointmentsActivity.this);
        mfetchappointmentdetailsobject = datamanager
                .getHistoryAppointmentFetchDetails("TodaysAppointments",
                        appointmentid);
        return mfetchappointmentdetailsobject;
    }
    private void navigatetoTodaysAppointmentDetail(String appointmentid) {
        SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
        if(mfetchappointmentdetails!=null) {
            Intent intent = new Intent(
                    SYLHistoryAppointmentsActivity.this,
                    SYLTodaysAppointmentDetailActivity.class);
            String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
            String organizer_id = Integer.toString(mfetchappointmentdetails
                    .getOrganizer().getUserId());
            String participnat_id = Integer.toString(mfetchappointmentdetails
                    .getParticipant().getUserId());
            if (curr_userid.equals(organizer_id)) {
                intent.putExtra("name", mfetchappointmentdetails.getParticipant()
                        .getName());
                int participantid = mfetchappointmentdetails.getParticipant()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getParticipant().getProfileImage());


                if(mfetchappointmentdetails.getParticipant()
                        .getMobile().equals("")||mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
                {
                    intent.putExtra("mobile", "not available");
                }
                else {
                    intent.putExtra("mobile", mfetchappointmentdetails.getParticipant()
                            .getMobile());
                }


                if (mfetchappointmentdetails.getParticipant()
                        .getSkypeId().equals("") || mfetchappointmentdetails.getParticipant()
                        .getSkypeId().equals("Not Available")           ) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails.getParticipant()
                            .getSkypeId());
                }
                if (mfetchappointmentdetails.getParticipant().getHangoutId().equals("")||
                        mfetchappointmentdetails.getParticipant().getHangoutId().equals("Not Available")
                        ) {
                    intent.putExtra("hangoutid", "not available");
                } else {
                    intent.putExtra("hangoutid", mfetchappointmentdetails.getParticipant().getHangoutId());
                }
                if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("")||
                        mfetchappointmentdetails.getParticipant().getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid", "not available");
                } else {
                    intent.putExtra("facetimeid", mfetchappointmentdetails.getParticipant().getFacetimeId());
                }

            } else {
                intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
                        .getName());
                int participantid = mfetchappointmentdetails.getOrganizer()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getOrganizer().getProfileImage());


                if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||
                        mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
                {
                    intent.putExtra("mobile", "not available");
                }
                else{
                    intent.putExtra("mobile", mfetchappointmentdetails.getOrganizer()
                            .getMobile());
                }


                if (mfetchappointmentdetails.getOrganizer()
                        .getSkypeId().equals("") || mfetchappointmentdetails.getOrganizer()
                        .getSkypeId().equals("Not Available")              ) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails.getOrganizer()
                            .getSkypeId());
                }
                if (mfetchappointmentdetails.getOrganizer().getHangoutId().equals("")||
                        mfetchappointmentdetails.getOrganizer().getHangoutId().equals("Not Available")
                        ) {
                    intent.putExtra("hangoutid", "not available");
                } else {
                    intent.putExtra("hangoutid", mfetchappointmentdetails.getOrganizer()
                            .getHangoutId());
                }
                if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("")||
                        mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid", "not available");
                } else {
                    intent.putExtra("facetimeid", mfetchappointmentdetails.getOrganizer()
                            .getFacetimeId());
                }

            }

            intent.putExtra("topic", mfetchappointmentdetails.getTopic());
            intent.putExtra("description",
                    mfetchappointmentdetails.getDescription());


            intent.putExtra("priority1",
                    mfetchappointmentdetails.getAppointmentPriority1());

            if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
                intent.putExtra("priority2",
                        "not available");
            } else {
                intent.putExtra("priority2",
                        mfetchappointmentdetails.getAppointmentPriority2());
            }
            if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
                intent.putExtra("priority3",
                        "not available");
            } else {
                intent.putExtra("priority3",
                        mfetchappointmentdetails.getAppointmentPriority3());
            }

            intent.putExtra("appointmentid", appointmentid);

            String date1 = mfetchappointmentdetails.getAppointmentDate1();
            String time1 = mfetchappointmentdetails.getAppointmentTime1();
            String date2 = mfetchappointmentdetails.getAppointmentDate2();
            String time2 = mfetchappointmentdetails.getAppointmentTime2();
            String date3 = mfetchappointmentdetails.getAppointmentDate3();
            String time3 = mfetchappointmentdetails.getAppointmentTime3();
            intent.putExtra("date1", date1 + " " + time1);
            intent.putExtra("date2", date2 + " " + time2);
            intent.putExtra("date3", date3 + " " + time3);
            intent.putExtra("intentfrom","historyappointment");

            startActivity(intent);
            finish();
        }
    }
    private void navigatetoRequestsenddetailscreen(String appointmentid) {


        SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
        if (mfetchappointmentdetails != null) {
            Intent intent = new Intent(
                    SYLHistoryAppointmentsActivity.this,
                    SYLRequestSendActivity.class);
            String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
            String organizer_id = Integer.toString(mfetchappointmentdetails
                    .getOrganizer().getUserId());
            String participnat_id = Integer.toString(mfetchappointmentdetails
                    .getParticipant().getUserId());
            if (curr_userid.equals(organizer_id)) {
                intent.putExtra("name", mfetchappointmentdetails.getParticipant()
                        .getName());
                int participantid = mfetchappointmentdetails.getParticipant()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getParticipant().getProfileImage());

                if (mfetchappointmentdetails.getParticipant()
                        .getMobile().equals("") ||
                        mfetchappointmentdetails.getParticipant()
                                .getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("mobile",
                            "not available");
                } else {
                    intent.putExtra("mobile",
                            mfetchappointmentdetails.getParticipant()
                                    .getMobile());
                }


                if (mfetchappointmentdetails.getParticipant()
                        .getFacetimeId().equals("") ||
                        mfetchappointmentdetails.getParticipant()
                                .getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid",
                            "not available");
                } else {
                    intent.putExtra("facetimeid",
                            mfetchappointmentdetails.getParticipant()
                                    .getFacetimeId());
                }

                if (mfetchappointmentdetails.getParticipant()
                        .getHangoutId().equals("") ||
                        mfetchappointmentdetails.getParticipant()
                                .getHangoutId().equals("Not Available")
                        ) {
                    intent.putExtra("hangoutid",
                            "not available");
                } else {
                    intent.putExtra("hangoutid",
                            mfetchappointmentdetails.getParticipant()
                                    .getHangoutId());
                }
                if (mfetchappointmentdetails
                        .getParticipant().getSkypeId().equals("") || mfetchappointmentdetails
                        .getParticipant().getSkypeId().equals("Not Available")) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails
                            .getParticipant().getSkypeId());
                }

            } else {
                intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
                        .getName());
                int participantid = mfetchappointmentdetails.getOrganizer()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getOrganizer().getProfileImage());


                if (mfetchappointmentdetails.getOrganizer().getMobile().equals("") ||
                        mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available")
                        ) {
                    intent.putExtra("mobile",
                            "not available");
                } else {
                    intent.putExtra("mobile",
                            mfetchappointmentdetails.getOrganizer()
                                    .getMobile());
                }


                if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("") ||
                        mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid",
                            "not available");
                } else {
                    intent.putExtra("facetimeid",
                            mfetchappointmentdetails.getOrganizer()
                                    .getFacetimeId());
                }
                if (mfetchappointmentdetails.getOrganizer().getHangoutId().equals("") ||
                        mfetchappointmentdetails.getOrganizer().getHangoutId().equals("Not Available")
                        ) {
                    intent.putExtra("hangoutid",
                            "not available");
                } else {
                    intent.putExtra("hangoutid",
                            mfetchappointmentdetails.getOrganizer()
                                    .getHangoutId());
                }
                if (mfetchappointmentdetails
                        .getOrganizer().getSkypeId().equals("") || mfetchappointmentdetails
                        .getOrganizer().getSkypeId().equals("Not Available")) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails
                            .getOrganizer().getSkypeId());
                }


            }

            intent.putExtra("topic", mfetchappointmentdetails.getTopic());
            intent.putExtra("description",
                    mfetchappointmentdetails.getDescription());

            intent.putExtra("priority1",
                    mfetchappointmentdetails.getAppointmentPriority1());

            if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
                intent.putExtra("priority2",
                        "not available");
            } else {
                intent.putExtra("priority2",
                        mfetchappointmentdetails.getAppointmentPriority2());
            }
            if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
                intent.putExtra("priority3",
                        "not available");
            } else {
                intent.putExtra("priority3",
                        mfetchappointmentdetails.getAppointmentPriority3());
            }


            intent.putExtra("appointmentid", appointmentid);

            String date1 = mfetchappointmentdetails.getAppointmentDate1();
            String time1 = mfetchappointmentdetails.getAppointmentTime1();
            String date2 = mfetchappointmentdetails.getAppointmentDate2();
            String time2 = mfetchappointmentdetails.getAppointmentTime2();
            String date3 = mfetchappointmentdetails.getAppointmentDate3();
            String time3 = mfetchappointmentdetails.getAppointmentTime3();
            intent.putExtra("date1", date1 + " " + time1);
            if (!date2.equals("")) {
                intent.putExtra("date2", date2 + " " + time2);
            } else {
                intent.putExtra("date2", " ");
            }
            if (!date3.equals("")) {
                intent.putExtra("date3", date3 + " " + time3);
            } else {
                intent.putExtra("date3", " ");
            }
            intent.putExtra("intentfrom","historyappointment");
            startActivity(intent);
            finish();
        }
    }
        private void navigatetoCanelrequestDetailscreen(String appointmentid)
        {
            Intent canceledrequestintent = new Intent(SYLHistoryAppointmentsActivity.this,
                    SYLCaneledRequestDetailedActivity.class);
            SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
            String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
            String organizer_id = Integer.toString(mfetchappointmentdetails
                    .getOrganizer().getUserId());
            String participnat_id = Integer.toString(mfetchappointmentdetails
                    .getParticipant().getUserId());
            if (curr_userid.equals(organizer_id)) {
                canceledrequestintent.putExtra("name", mfetchappointmentdetails
                        .getParticipant().getName());
                int participantid = mfetchappointmentdetails.getParticipant()
                        .getUserId();
                canceledrequestintent.putExtra("participantid",
                        Integer.toString(participantid));
                canceledrequestintent
                        .putExtra("profile_image", mfetchappointmentdetails
                                .getParticipant().getProfileImage());

                if(mfetchappointmentdetails.getParticipant().getMobile().equals("")|| mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))
                {
                    canceledrequestintent
                            .putExtra("mobile", "not available");
                }
                else {
                    canceledrequestintent
                            .putExtra("mobile", mfetchappointmentdetails
                                    .getParticipant().getMobile());
                }

                if(mfetchappointmentdetails
                        .getParticipant().getSkypeId().equals("") ||  mfetchappointmentdetails
                        .getParticipant().getSkypeId().equals("Not Available")           )
                {
                    canceledrequestintent
                            .putExtra("skypeid", "not available");
                }
                else {
                    canceledrequestintent
                            .putExtra("skypeid", mfetchappointmentdetails
                                    .getParticipant().getSkypeId());
                }


                if(mfetchappointmentdetails
                        .getParticipant().getFacetimeId().equals("")||
                        mfetchappointmentdetails
                                .getParticipant().getFacetimeId().equals("Not Available")
                        )
                {
                    canceledrequestintent
                            .putExtra("facetimeid", "not available");
                }
                else {
                    canceledrequestintent
                            .putExtra("facetimeid", mfetchappointmentdetails
                                    .getParticipant().getFacetimeId());
                }
                if(mfetchappointmentdetails
                        .getParticipant().getHangoutId().equals("") ||
                        mfetchappointmentdetails
                                .getParticipant().getHangoutId().equals("Not Available")
                        )
                {
                    canceledrequestintent
                            .putExtra("hangoutid","not available");
                }
                else{
                    canceledrequestintent
                            .putExtra("hangoutid", mfetchappointmentdetails
                                    .getParticipant().getHangoutId());
                }

            } else {
                canceledrequestintent.putExtra("name", mfetchappointmentdetails
                        .getOrganizer().getName());
                int participantid = mfetchappointmentdetails.getOrganizer()
                        .getUserId();
                canceledrequestintent.putExtra("participantid",
                        Integer.toString(participantid));
                canceledrequestintent.putExtra("profile_image",
                        mfetchappointmentdetails.getOrganizer().getProfileImage());

                if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")|| mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available"))
                {
                    canceledrequestintent
                            .putExtra("mobile","not avaialble");
                }
                else {
                    canceledrequestintent
                            .putExtra("mobile", mfetchappointmentdetails
                                    .getOrganizer().getMobile());
                }

                if(mfetchappointmentdetails
                        .getOrganizer().getSkypeId().equals("") || mfetchappointmentdetails
                        .getOrganizer().getSkypeId().equals("Not Available")           )
                {
                    canceledrequestintent
                            .putExtra("skypeid", "not available");
                }
                else {
                    canceledrequestintent
                            .putExtra("skypeid", mfetchappointmentdetails
                                    .getOrganizer().getSkypeId());
                }
                if( mfetchappointmentdetails
                        .getOrganizer().getFacetimeId().equals("") ||
                        mfetchappointmentdetails
                                .getOrganizer().getFacetimeId().equals("Not Available")
                        )
                {
                    canceledrequestintent
                            .putExtra("facetimeid","not available");
                }
                else{
                    canceledrequestintent
                            .putExtra("facetimeid", mfetchappointmentdetails
                                    .getOrganizer().getFacetimeId());
                }
                if(mfetchappointmentdetails
                        .getOrganizer().getHangoutId().equals("") ||
                        mfetchappointmentdetails
                                .getOrganizer().getHangoutId().equals("Not Available")
                        )
                {
                    canceledrequestintent
                            .putExtra("hangoutid", "not available");
                }
                else{
                    canceledrequestintent
                            .putExtra("hangoutid", mfetchappointmentdetails
                                    .getOrganizer().getHangoutId());
                }

            }

            canceledrequestintent.putExtra("topic",
                    mfetchappointmentdetails.getTopic());
            canceledrequestintent.putExtra("description",
                    mfetchappointmentdetails.getDescription());
            canceledrequestintent.putExtra("appointmentid", appointmentid);

            canceledrequestintent.putExtra("priority1",
                    mfetchappointmentdetails.getAppointmentPriority1());
            if(mfetchappointmentdetails.getAppointmentPriority2().equals(""))
            {
                canceledrequestintent.putExtra("priority2",
                        "not available");
            }
            else {
                canceledrequestintent.putExtra("priority2",
                        mfetchappointmentdetails.getAppointmentPriority2());
            }
            if(	mfetchappointmentdetails.getAppointmentPriority3().equals(""))
            {
                canceledrequestintent.putExtra("priority3",
                        "not available");
            }
            else {
                canceledrequestintent.putExtra("priority3",
                        mfetchappointmentdetails.getAppointmentPriority3());
            }

            String date1 = mfetchappointmentdetails.getAppointmentDate1();
            String time1 = mfetchappointmentdetails.getAppointmentTime1();
            String date2 = mfetchappointmentdetails.getAppointmentDate2();
            String time2 = mfetchappointmentdetails.getAppointmentTime2();
            String date3 = mfetchappointmentdetails.getAppointmentDate3();
            String time3 = mfetchappointmentdetails.getAppointmentTime3();
            canceledrequestintent.putExtra("date1", date1 + " " + time1);
            if (!date2.equals("")) {
                canceledrequestintent.putExtra("date2", date2 + " " + time2);
            } else {
                canceledrequestintent.putExtra("date2", " ");
            }
            if (!date3.equals("")) {
                canceledrequestintent.putExtra("date3", date3 + " " + time3);
            } else {
                canceledrequestintent.putExtra("date3", " ");
            }
            canceledrequestintent.putExtra("intentfrom","historyappointment");
            startActivity(canceledrequestintent);
            finish();
        }

    private void navigatetoFinishedAppointmentDetailScreen(String appointmentid)
    {
        Intent finishedappointmentintent = new Intent(SYLHistoryAppointmentsActivity.this,
                SYLFinishedAppointmentDetailActivity.class);
        SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
        String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
        String organizer_id = Integer.toString(mfetchappointmentdetails
                .getOrganizer().getUserId());
        String participnat_id = Integer.toString(mfetchappointmentdetails
                .getParticipant().getUserId());
        if (curr_userid.equals(organizer_id)) {
            finishedappointmentintent.putExtra("name", mfetchappointmentdetails
                    .getParticipant().getName());
            int participantid = mfetchappointmentdetails.getParticipant()
                    .getUserId();
            finishedappointmentintent.putExtra("participantid",
                    Integer.toString(participantid));
            finishedappointmentintent
                    .putExtra("profile_image", mfetchappointmentdetails
                            .getParticipant().getProfileImage());



            if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
                    mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available"))


            {
                finishedappointmentintent
                        .putExtra("mobile", "not available");
            }
            else{
                finishedappointmentintent
                        .putExtra("mobile", mfetchappointmentdetails
                                .getParticipant().getMobile());
            }

            if(mfetchappointmentdetails
                    .getParticipant().getSkypeId().equals("") || mfetchappointmentdetails
                    .getParticipant().getSkypeId().equals("Not Available")        )
            {
                finishedappointmentintent
                        .putExtra("skypeid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("skypeid", mfetchappointmentdetails
                                .getParticipant().getSkypeId());
            }
            if (mfetchappointmentdetails
                    .getParticipant().getFacetimeId().equals("") ||
                    mfetchappointmentdetails
                            .getParticipant().getFacetimeId().equals("Not Available")
                    )
            {
                finishedappointmentintent
                        .putExtra("facetimeid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("facetimeid", mfetchappointmentdetails
                                .getParticipant().getFacetimeId());
            }
            if(mfetchappointmentdetails
                    .getParticipant().getHangoutId().equals("")||
                    mfetchappointmentdetails
                            .getParticipant().getHangoutId().equals("Not Available")
                    )
            {
                finishedappointmentintent
                        .putExtra("hangoutid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("hangoutid", mfetchappointmentdetails
                                .getParticipant().getHangoutId());
            }

        } else {
            finishedappointmentintent.putExtra("name", mfetchappointmentdetails
                    .getOrganizer().getName());
            int participantid = mfetchappointmentdetails.getOrganizer()
                    .getUserId();
            finishedappointmentintent.putExtra("participantid",
                    Integer.toString(participantid));
            finishedappointmentintent.putExtra("profile_image",
                    mfetchappointmentdetails.getOrganizer().getProfileImage());

            if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||mfetchappointmentdetails.getOrganizer().getMobile().equals(""))
            {
                finishedappointmentintent
                        .putExtra("mobile", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("mobile", mfetchappointmentdetails
                                .getOrganizer().getMobile());
            }

            if(mfetchappointmentdetails
                    .getOrganizer().getSkypeId().equals("") ||
                    mfetchappointmentdetails
                            .getOrganizer().getSkypeId().equals("Not Available")

                    )
            {
                finishedappointmentintent
                        .putExtra("skypeid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("skypeid", mfetchappointmentdetails
                                .getOrganizer().getSkypeId());
            }
            if(mfetchappointmentdetails
                    .getOrganizer().getFacetimeId().equals("")||
                    mfetchappointmentdetails
                            .getOrganizer().getFacetimeId().equals("Not Available")

                    )
            {
                finishedappointmentintent
                        .putExtra("facetimeid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("facetimeid", mfetchappointmentdetails
                                .getOrganizer().getFacetimeId());
            }
            if(mfetchappointmentdetails
                    .getOrganizer().getHangoutId().equals("")||
                    mfetchappointmentdetails
                            .getOrganizer().getHangoutId().equals("Not Available")
                    )
            {
                finishedappointmentintent
                        .putExtra("hangoutid", "not available");
            }
            else {
                finishedappointmentintent
                        .putExtra("hangoutid", mfetchappointmentdetails
                                .getOrganizer().getHangoutId());
            }

        }

        finishedappointmentintent.putExtra("topic",
                mfetchappointmentdetails.getTopic());
        finishedappointmentintent.putExtra("description",
                mfetchappointmentdetails.getDescription());


        finishedappointmentintent.putExtra("priority1",
                mfetchappointmentdetails.getAppointmentPriority1());
        finishedappointmentintent.putExtra("priority2",
                mfetchappointmentdetails.getAppointmentPriority2());
        finishedappointmentintent.putExtra("priority3",
                mfetchappointmentdetails.getAppointmentPriority3());
        String date1 = mfetchappointmentdetails.getAppointmentDate1();
        String time1 = mfetchappointmentdetails.getAppointmentTime1();
        String date2 = mfetchappointmentdetails.getAppointmentDate2();
        String time2 = mfetchappointmentdetails.getAppointmentTime2();
        String date3 = mfetchappointmentdetails.getAppointmentDate3();
        String time3 = mfetchappointmentdetails.getAppointmentTime3();
        finishedappointmentintent.putExtra("date1", date1 + " " + time1);
        finishedappointmentintent.putExtra("date2", date2 + " " + time2);

        if (date3.equals("") || time3.equals("")) {
            finishedappointmentintent.putExtra("date3", "");
        } else {
            finishedappointmentintent.putExtra("date3", date3 + " " + time3);
        }
        finishedappointmentintent.putExtra("intentfrom","historyappointment");
        finishedappointmentintent.putExtra("created_at", "1-3-2014  8:00:00");
        startActivity(finishedappointmentintent);
        finish();
    }

    protected void navigatetoConfirmedAppointmentDetailScreen(
            String appointmentid) {

        SYLFetchAppointmentsDetails mfetchappointmentdetails = getHistoryAppointmentusingID(appointmentid);
        if(mfetchappointmentdetails!=null) {
            Intent intent = new Intent(
                    SYLHistoryAppointmentsActivity.this,
                    SYLConfirmedRequestAppointmentActivity.class);
            String curr_userid = SYLSaveValues.getSYLUserID(SYLHistoryAppointmentsActivity.this);
            String organizer_id = Integer.toString(mfetchappointmentdetails
                    .getOrganizer().getUserId());
            String participnat_id = Integer.toString(mfetchappointmentdetails
                    .getParticipant().getUserId());
            if (curr_userid.equals(organizer_id)) {
                intent.putExtra("name", mfetchappointmentdetails.getParticipant()
                        .getName());
                int participantid = mfetchappointmentdetails.getParticipant()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getParticipant().getProfileImage());

                if (mfetchappointmentdetails
                        .getParticipant().getHangoutId().equals("") ||
                        mfetchappointmentdetails
                                .getParticipant().getHangoutId().equals("Not Available")

                        ) {
                    intent.putExtra("hangoutid", "not available");
                } else {
                    intent.putExtra("hangoutid", mfetchappointmentdetails
                            .getParticipant().getHangoutId());
                }
                if(mfetchappointmentdetails.getParticipant().getMobile().equals("")||
                        mfetchappointmentdetails.getParticipant().getMobile().equals("Not Available")
                        )
                {
                    intent.putExtra("mobile", "not available");
                }
                else {
                    intent.putExtra("mobile", mfetchappointmentdetails.getParticipant()
                            .getMobile());
                }
                if (mfetchappointmentdetails.getParticipant().getSkypeId().equals("")
                        || mfetchappointmentdetails.getParticipant().getSkypeId().equals("Not Available")                  ) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails
                            .getParticipant().getSkypeId());
                }
                if (mfetchappointmentdetails.getParticipant().getFacetimeId().equals("") ||
                        mfetchappointmentdetails.getParticipant().getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid", "not available");
                } else {
                    intent.putExtra("facetimeid", mfetchappointmentdetails.getParticipant().getFacetimeId());
                }



            } else {
                intent.putExtra("name", mfetchappointmentdetails.getOrganizer()
                        .getName());
                int participantid = mfetchappointmentdetails.getOrganizer()
                        .getUserId();
                intent.putExtra("participantid", Integer.toString(participantid));
                intent.putExtra("profile_image", mfetchappointmentdetails
                        .getOrganizer().getProfileImage());
                intent.putExtra("hangoutid", mfetchappointmentdetails
                        .getOrganizer().getHangoutId());
                if (mfetchappointmentdetails.getOrganizer().getSkypeId().equals("") ||
                        mfetchappointmentdetails.getOrganizer().getSkypeId().equals("Not Available")
                        ) {
                    intent.putExtra("skypeid", "not available");
                } else {
                    intent.putExtra("skypeid", mfetchappointmentdetails
                            .getOrganizer().getSkypeId());
                }
                if(mfetchappointmentdetails.getOrganizer().getMobile().equals("")||
                        mfetchappointmentdetails.getOrganizer().getMobile().equals("Not Available")
                        )
                {
                    intent.putExtra("mobile","not available");
                }
                else {
                    intent.putExtra("mobile", mfetchappointmentdetails.getOrganizer()
                            .getMobile());
                }
                if (mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("") ||
                        mfetchappointmentdetails.getOrganizer().getFacetimeId().equals("Not Available")
                        ) {
                    intent.putExtra("facetimeid", "not available");
                } else {
                    intent.putExtra("facetimeid", mfetchappointmentdetails.getOrganizer().getFacetimeId());
                }

            }

            intent.putExtra("topic", mfetchappointmentdetails.getTopic());
            intent.putExtra("description",
                    mfetchappointmentdetails.getDescription());





            intent.putExtra("priority1",
                    mfetchappointmentdetails.getAppointmentPriority1());


            if (mfetchappointmentdetails.getAppointmentPriority2().equals("")) {
                intent.putExtra("priority2",
                        "not available");
            } else {
                intent.putExtra("priority2",
                        mfetchappointmentdetails.getAppointmentPriority2());
            }

            if (mfetchappointmentdetails.getAppointmentPriority3().equals("")) {
                intent.putExtra("priority3",
                        "not available");
            } else {
                intent.putExtra("priority3",
                        mfetchappointmentdetails.getAppointmentPriority3());
            }


            intent.putExtra("appointmentid", appointmentid);
            String date1 = mfetchappointmentdetails.getAppointmentDate1();
            String time1 = mfetchappointmentdetails.getAppointmentTime1();
            String date2 = mfetchappointmentdetails.getAppointmentDate2();
            String time2 = mfetchappointmentdetails.getAppointmentTime2();
            String date3 = mfetchappointmentdetails.getAppointmentDate3();
            String time3 = mfetchappointmentdetails.getAppointmentTime3();
            intent.putExtra("date1", date1 + " " + time1);

            if (date2.equals("") || time2.equals("")) {
                intent.putExtra("date2", " ");
            } else {
                intent.putExtra("date2", date2 + " " + time2);
            }
            if (date3.equals("") || time3.equals("")) {
                intent.putExtra("date3", " ");
            } else {
                intent.putExtra("date3", date3 + " " + time3);
            }
            intent.putExtra("intentfrom", "historyappointment");
            startActivity(intent);
            finish();
        }
    }

private void navigatetoFulureAppointmentsListingScreen()
{
    Intent intent = new Intent(SYLHistoryAppointmentsActivity.this,
            SYLFragmentChangeActivity.class);
    intent.putExtra("fragmentvalue", "newappointment");
    intent.putExtra("needrefresh", "false");
    startActivity(intent);
    finish();

}

    @Override
    public void onBackPressed() {
        navigatetoFulureAppointmentsListingScreen();
    }
    private void deleteHistoryAppointments()
    {
        if(SYLUtil.isNetworkAvailable(SYLHistoryAppointmentsActivity.this)) {

            msylProgressDialog = new ProgressDialog(SYLHistoryAppointmentsActivity.this);
            msylProgressDialog.setMessage("Please wait...");
            msylProgressDialog.setCancelable(false);
            msylProgressDialog.setCanceledOnTouchOutside(false);
            msylProgressDialog.show();

            accesstoken = SYLSaveValues.getSYLaccessToken(SYLHistoryAppointmentsActivity.this);
            GMTtimezone = SYLUtil.getTimeGMTZone(SYLHistoryAppointmentsActivity.this);
            SYLDeleteHistoryAppointmentsViewmanager mViewmanager=new SYLDeleteHistoryAppointmentsViewmanager();
            mViewmanager.mDeleteHistoryAppointmentsListener=SYLHistoryAppointmentsActivity.this;
            mViewmanager.deleteHistoryAppointments(accesstoken,GMTtimezone);

        }
        else {
            SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,"Please check your network connection");
        }

    }


    public void deleteHistoryAppointmentsFinish(SYLAppointmentRemoveResponseModel msylappointmentremoveResponseModel)
    {
        if (msylProgressDialog != null && msylProgressDialog.isShowing()) {
            msylProgressDialog.dismiss();
        }
        try
        {
            if(msylappointmentremoveResponseModel!=null)
            {
                if(msylappointmentremoveResponseModel.isSuccess())
                {
                  SYLHistoryAppointmentsdatamanager mdatamager=new SYLHistoryAppointmentsdatamanager(SYLHistoryAppointmentsActivity.this)  ;
                    mdatamager.deleteHistoryAppointmentData("TODAYS CONFIRMED");
                    mdatamager.deleteHistoryAppointmentData("REQUEST RECEIVED");
                    mdatamager.deleteHistoryAppointmentData("REQUEST SENT");
                    mdatamager.deleteHistoryAppointmentData("OTHER CONFIRMED");
                    mdatamager.deleteHistoryAppointmentData("CANCELLED");
                    mdatamager.deleteHistoryAppointmentData("FINISHED");
               getHistoryAppointmentsandclearList();

                }
                else
                {
                    showAlertDialog(msylappointmentremoveResponseModel.getError().getErrorDetail());
                }
            }
            else {
                SYLUtil.buildAlertMessage(SYLHistoryAppointmentsActivity.this,"Unexpected server error happened");
            }
        }
        catch (Exception e)
        {

        }
    }
    private void showAlertDialog(String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SYLHistoryAppointmentsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Delete SYL ");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });




        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    private void   getHistoryAppointmentsandclearList()
    {
        try {
            sYLAppointmentsAdapter = new SYLAppointmentsAdapter(SYLHistoryAppointmentsActivity.this);
            SYLHistoryAppointmentsdatamanager datamanager = new SYLHistoryAppointmentsdatamanager(
                    SYLHistoryAppointmentsActivity.this);
            ArrayList<SYLFetchAppointmentsDetails> requestReceivedarraylistcached = datamanager
                    .getHistoryAppointmentDetails("REQUEST RECEIVED");
            ArrayList<SYLFetchAppointmentsDetails> confirmedAppointmentsarraylistcached = datamanager
                    .getHistoryAppointmentDetails("OTHER CONFIRMED");
            ArrayList<SYLFetchAppointmentsDetails> requestSendarraylistcached = datamanager
                    .getHistoryAppointmentDetails("REQUEST SENT");
            ArrayList<SYLFetchAppointmentsDetails> cancelledRequestarraylistcached = datamanager
                    .getHistoryAppointmentDetails("CANCELLED");
            ArrayList<SYLFetchAppointmentsDetails> finishedappointmentsarraylistcached = datamanager
                    .getHistoryAppointmentDetails("FINISHED");
            ArrayList<SYLFetchAppointmentsDetails> todaysappointmentsarraylistcached = datamanager
                    .getHistoryAppointmentDetails("TODAYS CONFIRMED");

            if ((todaysappointmentsarraylistcached.size() < 1)
                    &&
                    (requestReceivedarraylistcached.size() < 1)
                    &&
                    (confirmedAppointmentsarraylistcached.size() < 1)
                    &&
                    (requestSendarraylistcached.size() < 1)
                    &&
                    (cancelledRequestarraylistcached.size() < 1)
                    &&
                    (finishedappointmentsarraylistcached.size() < 1)
                    ) {
       listView.setAdapter(sYLAppointmentsAdapter);


            }
        }
        catch (Exception e) {
        }
    }
}
