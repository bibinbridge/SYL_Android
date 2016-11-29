package com.webcamconsult.syl.databaseaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.webcamconsult.syl.model.SYLFetchAppointmentsDetails;
import com.webcamconsult.syl.model.SYLOrganizer;
import com.webcamconsult.syl.model.SYLParticipant;

import java.util.ArrayList;

/**
 * Created by Sandeep on 11/24/2015.
 */
public class SYLHistoryAppointmentsdatamanager
{

    SYLDatabase syldatabase;

    /** The context. */
    Context context;
    public SYLHistoryAppointmentsdatamanager(Context context) {
        this.context = context;
        if (syldatabase == null)
            syldatabase = new SYLDatabase(context);
    }



    public void insertappointmentsetails(
            ArrayList<SYLFetchAppointmentsDetails> appointmentdetails,String appointmenttype) {
        if (appointmentdetails != null && appointmentdetails.size() > 0) {
            deleteAppointmentData(appointmenttype);
            for (SYLFetchAppointmentsDetails syl_appointmentdetails : appointmentdetails) {

                this. insertHistoryAppointmentdata(syl_appointmentdetails,
                        this.context,appointmenttype);
            }
        }

    }

    private void insertHistoryAppointmentdata(SYLFetchAppointmentsDetails sylappdetails,
                                       Context c, String appointmenttype) {

        try {
            if (syldatabase == null)
                syldatabase = new SYLDatabase(context);

            SQLiteDatabase db = syldatabase.getWritableDatabase();
            ContentValues values;
            values = new ContentValues();
            values.put("appointment_id", Integer.toString(sylappdetails.getAppointmentID()));
            values.put("appointment_topic", sylappdetails.getTopic());
            values.put("appointment_description", sylappdetails.getDescription());
            values.put("priority1", sylappdetails.getAppointmentPriority1());
            values.put("priority2", sylappdetails.getAppointmentPriority2());
            values.put("priority3", sylappdetails.getAppointmentPriority3());
            values.put("preferreddate1", sylappdetails.getAppointmentDate1());
            values.put("preferredtime1", sylappdetails.getAppointmentTime1());
            values.put("optiondate1", sylappdetails.getAppointmentDate2());
            values.put("optiontime1", sylappdetails.getAppointmentTime2());
            values.put("optiondate2", sylappdetails.getAppointmentDate3());
            values.put("optiontime2", sylappdetails.getAppointmentTime3());
            values.put("user_id", Integer.toString(sylappdetails.getParticipant().getUserId()));
            values.put("firstname", sylappdetails.getParticipant().getName());
            values.put("mobilenumber", sylappdetails.getParticipant().getMobile());
            values.put("skypeid", sylappdetails.getParticipant().getSkypeId());
            values.put("profile_image", sylappdetails.getParticipant().getProfileImage());
            values.put("email", sylappdetails.getParticipant().getEmail());
            values.put("facetimeid", sylappdetails.getParticipant().getFacetimeId());
            values.put("hangoutid", sylappdetails.getParticipant().getHangoutId());

            values.put("appointment_type", sylappdetails.getListingType());

            values.put("organizer_id", sylappdetails.getOrganizer().getUserId());
            values.put("organizer_profileimage", sylappdetails.getOrganizer().getProfileImage());
            values.put("organizer_email", sylappdetails.getOrganizer().getEmail());
            values.put("organizer_name", sylappdetails.getOrganizer().getName());
            values.put("organizer_facetimeid", sylappdetails.getOrganizer().getFacetimeId());
            values.put("organizer_skypeid", sylappdetails.getOrganizer().getSkypeId());
            values.put("organizer_mobile", sylappdetails.getOrganizer().getMobile());
            values.put("organizer_hangoutid", sylappdetails.getOrganizer().getHangoutId());
            db.insert("appointmenthistory_details", null, values);
            db.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void deleteAppointmentData(String appointmenttype) {
//String whereargs[]={"Request Received"};
//String mSelectionClause =  " appointment_type=? " ;
        String		query = "delete from appointmenthistory_details where appointment_type='"
                + appointmenttype + "'                ";
        if (syldatabase != null) {
            try {
                SQLiteDatabase db = syldatabase.getWritableDatabase();
                //	int k=		db.delete("appointment_details", mSelectionClause, whereargs);
                db.execSQL(query);
                db.close();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    public void deleteHistoryAppointmentData(String appointmenttype) {
//String whereargs[]={"Request Received"};
//String mSelectionClause =  " appointment_type=? " ;
        String		query = "delete from appointmenthistory_details where appointment_type='"
                + appointmenttype + "'                ";
        if (syldatabase != null) {
            try {
                SQLiteDatabase db = syldatabase.getWritableDatabase();
                //	int k=		db.delete("appointment_details", mSelectionClause, whereargs);
                db.execSQL(query);
                db.close();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    public void clearTable(String tablename) {
        SQLiteDatabase db=null;
        if (syldatabase != null) {
            try {
                db = syldatabase.getWritableDatabase();
                db.delete(tablename, null, null);
                db.close();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                db.close();
            }
        }

    }
    public  SYLFetchAppointmentsDetails getHistoryAppointmentFetchDetails(
            String app_type,String appointmentid) {
        String query;
        SYLFetchAppointmentsDetails syl_appdetails=null;
        if (syldatabase == null)
            syldatabase = new SYLDatabase(this.context);
        ArrayList<SYLFetchAppointmentsDetails> syl_appointmentdetailsarraylist = new ArrayList<SYLFetchAppointmentsDetails>();
        Cursor cursor = null;
        SQLiteDatabase db = syldatabase.getReadableDatabase();
        try {
//			query = "select * from appointment_details where appointment_type='"
//					+ app_type + "'                ";
            query = "select * from appointmenthistory_details where appointment_id='"+appointmentid+"'";

            if (cursor == null)
                cursor = db
                        .rawQuery(
                                query,
                                null);

            while (cursor.moveToNext()) {
                syl_appdetails = new SYLFetchAppointmentsDetails();

                String appointmenttopic=cursor.getString(cursor
                        .getColumnIndex("appointment_topic"));
                String appointmentdescription=cursor.getString(cursor
                        .getColumnIndex("appointment_description"));
                String priority1=cursor.getString(cursor
                        .getColumnIndex("priority1"));
                String priority2=cursor.getString(cursor
                        .getColumnIndex("priority2"));
                String priority3=cursor.getString(cursor
                        .getColumnIndex("priority3"));
                String preferreddate1=cursor.getString(cursor
                        .getColumnIndex("preferreddate1"));
                String preferredtime1=cursor.getString(cursor
                        .getColumnIndex("preferredtime1"));
                String optiondate1=cursor.getString(cursor
                        .getColumnIndex("optiondate1"));
                String optiontime1=cursor.getString(cursor
                        .getColumnIndex("optiontime1"));
                String optiondate2=cursor.getString(cursor
                        .getColumnIndex("optiondate2"));
                String optiontime2=cursor.getString(cursor
                        .getColumnIndex("optiontime2"));
                String organizerid=cursor.getString(cursor
                        .getColumnIndex("organizer_id"));
                String organizer_profileimage=cursor.getString(cursor
                        .getColumnIndex("organizer_profileimage"));
                String organizer_email=cursor.getString(cursor
                        .getColumnIndex("organizer_email"));
                String organizer_name=cursor.getString(cursor
                        .getColumnIndex("organizer_name"));
                String organizer_facetimeid=cursor.getString(cursor
                        .getColumnIndex("organizer_facetimeid"));
                String organizer_skypeid=cursor.getString(cursor
                        .getColumnIndex("organizer_skypeid"));
                String organizer_mobilenumber=cursor.getString(cursor
                        .getColumnIndex("organizer_mobile"));
                String organizer_hangoutid=cursor.getString(cursor
                        .getColumnIndex("organizer_hangoutid"));

                syl_appdetails.setDescription(appointmentdescription);
                syl_appdetails.setTopic(appointmenttopic);
                syl_appdetails.setAppointmentPriority1(priority1);
                syl_appdetails.setAppointmentPriority2(priority2);
                syl_appdetails.setAppointmentPriority3(priority3);
                syl_appdetails.setAppointmentDate1(preferreddate1);
                syl_appdetails.setAppointmentTime1(preferredtime1);
                syl_appdetails.setAppointmentDate2(optiondate1);
                syl_appdetails.setAppointmentTime2(optiontime1);
                syl_appdetails.setAppointmentDate3(optiondate2);
                syl_appdetails.setAppointmentTime3(optiontime2);

                String mobilenumber=cursor.getString(cursor
                        .getColumnIndex("mobilenumber"));

                String profileimage=cursor.getString(cursor
                        .getColumnIndex("profile_image"));
                String name=cursor.getString(cursor
                        .getColumnIndex("firstname"));
                String skypeid=cursor.getString(cursor
                        .getColumnIndex("skypeid"));
                String facetimeid=cursor.getString(cursor
                        .getColumnIndex("facetimeid"));
                String hangoutid=cursor.getString(cursor
                        .getColumnIndex("hangoutid"));


                String participantid=cursor.getString(cursor
                        .getColumnIndex("user_id"));

                SYLParticipant participant=new SYLParticipant();
                participant.setName(name);
                participant.setUserId(Integer.parseInt(participantid));
                participant.setProfileImage(profileimage);
                participant.setMobile(mobilenumber);
                participant.setSkypeId(skypeid);
                participant.setFacetimeId(facetimeid);
                participant.setHangoutId(hangoutid);
                syl_appdetails.setParticipant(participant);
                SYLOrganizer organizer=new SYLOrganizer();
                organizer.setUserId(Integer.parseInt(organizerid));
                organizer.setProfileImage(organizer_profileimage);
                organizer.setName(organizer_name);
                organizer.setEmail(organizer_email);
                organizer.setFacetimeId(organizer_facetimeid);
                organizer.setSkypeId(organizer_skypeid);
                organizer.setMobile(organizer_mobilenumber);
                organizer.setHangoutId(organizer_hangoutid);
                syl_appdetails.setOrganizer(organizer);
//				syl_appdetails.set(cursor.getString(cursor
//						.getColumnIndex("firstname")));
//				syl_appdetails.setLastname(cursor.getString(cursor
//						.getColumnIndex("lastname")));
//				syl_appdetails.setDate(cursor.getString(cursor
//						.getColumnIndex("date")));
//				syl_appdetails.setTime(cursor.getString(cursor
//						.getColumnIndex("time")));
//				syl_appdetails.setProfile_image(cursor.getString(cursor
//						.getColumnIndex("profile_image")));
                syl_appointmentdetailsarraylist.add(syl_appdetails);

            }
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
        }
        cursor.close();
        return syl_appdetails;
    }
    public ArrayList<SYLFetchAppointmentsDetails> getHistoryAppointmentDetails(
            String app_type) {
        String query;


        if (syldatabase == null)
            syldatabase = new SYLDatabase(this.context);
        ArrayList<SYLFetchAppointmentsDetails> syl_appointmentdetailsarraylist = new ArrayList<SYLFetchAppointmentsDetails>();
        Cursor cursor = null;
        SQLiteDatabase db = syldatabase.getReadableDatabase();

        try {
            query = "select * from appointmenthistory_details where appointment_type='"
                    + app_type + "'                ";
            if (cursor == null)
                cursor = db
                        .rawQuery(
                                query,
                                null);

            while (cursor.moveToNext()) {
                SYLFetchAppointmentsDetails syl_appdetails = new SYLFetchAppointmentsDetails();
                String appointmentid=cursor.getString(cursor
                        .getColumnIndex("appointment_id"));
                String appointmentdescription=cursor.getString(cursor
                        .getColumnIndex("appointment_description"));
                String organizerid=cursor.getString(cursor
                        .getColumnIndex("organizer_id"));
                String profileimage=cursor.getString(cursor
                        .getColumnIndex("profile_image"));
                String name=cursor.getString(cursor
                        .getColumnIndex("firstname"));
                String preferreddate1=cursor.getString(cursor
                        .getColumnIndex("preferreddate1"));
                String preferredtime1=cursor.getString(cursor
                        .getColumnIndex("preferredtime1"));
                String listingtype=cursor.getString(cursor
                        .getColumnIndex("appointment_type"));
                String organizer_profileimage=cursor.getString(cursor
                        .getColumnIndex("organizer_profileimage"));
                String organizer_name=cursor.getString(cursor
                        .getColumnIndex("organizer_name"));
                String appointmenttype=	cursor.getString(cursor
                        .getColumnIndex("appointment_type"));
                SYLOrganizer organizer=new SYLOrganizer();

                organizer.setUserId(Integer.parseInt(organizerid));
                organizer.setProfileImage(organizer_profileimage);
                organizer.setName(organizer_name);
                syl_appdetails.setOrganizer(organizer);

                SYLParticipant participant=new SYLParticipant();
                participant.setName(name);

                participant.setProfileImage(profileimage);
                syl_appdetails.setParticipant(participant);
                syl_appdetails.setAppointmentDate1(preferreddate1);
                syl_appdetails.setAppointmentTime1(preferredtime1);
                syl_appdetails.setListingType(appointmenttype);
                syl_appdetails.setAppointmentID(Integer.parseInt(appointmentid));
                syl_appdetails.setDescription(appointmentdescription);
                syl_appointmentdetailsarraylist.add(syl_appdetails);

            }
        } catch (Exception e) {

        }
        cursor.close();
        db.close();
        return syl_appointmentdetailsarraylist;
    }





}
