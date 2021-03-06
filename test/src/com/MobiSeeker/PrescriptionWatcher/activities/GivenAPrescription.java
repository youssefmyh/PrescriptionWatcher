package com.MobiSeeker.PrescriptionWatcher.activities;

import android.content.Context;
import android.widget.Toast;

import com.MobiSeeker.PrescriptionWatcher.data.Entry;
import com.MobiSeeker.PrescriptionWatcher.data.PrescriptionRepository;

import org.junit.Ignore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class GivenAPrescription {

    private ShadowApplication shadowApplication;
    private Context context;

    Prescription activity;

    private
    @Mock
    PrescriptionRepository prescriptionRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.shadowApplication = Robolectric.getShadowApplication();
        this.context = shadowApplication.getApplicationContext();

        this.activity = new Prescription();
        this.activity.onCreate(null);
        this.activity.prescriptionRepository = this.prescriptionRepository;
    }

    @Test
    public void whenCallingOnCreateShouldNotThrow() {
        assertNotNull(this.activity);
    }

    @Test
    public void whenCallingOnCreateShouldRenderLayout() {
        assertNotNull(this.activity.drugName);
        assertNotNull(this.activity.comment);
        assertNotNull(this.activity.startDate);
        assertNotNull(this.activity.endDate);
        assertNotNull(this.activity.startTime);
        assertNotNull(this.activity.endTime);
    }

    @Test
    public void startDateShouldShowsToday() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        String formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        assertEquals(formattedDate, this.activity.startDate.getText().toString());
    }

    @Test
    public void endDateShouldShowsNextWeek() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 7);

        String formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        assertEquals(formattedDate, this.activity.endDate.getText().toString());
    }

    @Test
    public void startTimeShouldShowsSevenAM() {
        assertEquals("07:00:00", this.activity.startTime.getText().toString());
    }

    @Test
    public void endTimeShouldShowsSevenAM() {
        assertEquals("22:00:00", this.activity.endTime.getText().toString());
    }

    @Ignore
    @Test
    public void whenCallingShowDatePickerShouldShowsCorrectDate() {
        this.activity.showDatePicker(this.activity.startDate);

        ShadowDialog dialog =  this.shadowApplication.getLatestDialog();
        assertNotNull(dialog);

       //assertTrue(dialog.isShowing());
    }

    @Ignore
    @Test
    public void whenCallingShowTimePickerShouldShowsCorrectDate() {
        this.activity.showDatePicker(this.activity.startTime);

        ShadowDialog dialog =  Robolectric.getShadowApplication().getLatestDialog();
        assertNotNull(dialog);

        //assertTrue(dialog.isShowing());
    }

    @Test
    public void whenCallingonDateSetShouldUpdateSelectedDate() {
        this.activity.showDatePicker(this.activity.startDate);

        this.activity.onDateSet(null, 1979, Calendar.MARCH, 31);

        assertEquals("Mar 31, 1979", this.activity.startDate.getText().toString());
    }

    @Test
    public void whenCallingonDateSetAndStartDateIsEmptyShouldShowNow() {
        this.activity.startDate.setText("");
        this.activity.showDatePicker(this.activity.startDate);

        this.activity.onDateSet(null, 1979, Calendar.MARCH, 31);

        assertEquals("Mar 31, 1979", this.activity.startDate.getText().toString());
    }

    @Test
    public void whenCallingOnTimeSetShouldUpdateSelectedTimeForStartTime() {
        this.activity.showTimePicker(this.activity.startTime);

        this.activity.onTimeSet(null, 10, 50);

        assertEquals("10:50", this.activity.startTime.getText().toString());
    }

    @Test
    public void whenCallingOnTimeSetShouldUpdateSelectedTimeForEndTime() {
        this.activity.showTimePicker(this.activity.endTime);

        this.activity.onTimeSet(null, 22, 50);

        assertEquals("22:50", this.activity.endTime.getText().toString());
    }

    @Test
    public void whenCallingSaveShouldNotThrow() {
        this.activity.save(null);
    }

    @Test
    public void whenCallingCancelShouldNotThrow() {
        this.activity.cancel(null);
    }

    @Test
    public void whenCallingOnCreateShouldAllocatePrescriptionRepository() {
        assertNotNull(this.activity.prescriptionRepository);
    }

    @Test
    public void whenCallingSaveShouldCallPrescriptionRepositorySave() throws Exception{

        Date startDate = DateFormat.getDateInstance(DateFormat.MEDIUM).parse("Dec 10, 2012");

        Date endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).parse("Dec 20, 2012");

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        ArgumentCaptor<Entry> entryCaptor = ArgumentCaptor.forClass(Entry.class);

        this.activity.drugName.setText("NAME");
        this.activity.dosage.setText("3");
        this.activity.timesPerDay.setText("5");
        this.activity.comment.setText("COMMENT");
        this.activity.startDate.setText("Dec 10, 2012");
        this.activity.endDate.setText("Dec 20, 2012");
        this.activity.startTime.setText("10:00:00");
        this.activity.endTime.setText("17:00:00");

        this.activity.save(null);

        verify(this.prescriptionRepository).save(contextCaptor.capture(), entryCaptor.capture());

        assertEquals("NAME", entryCaptor.getValue().getMedicineName());
        assertEquals("COMMENT", entryCaptor.getValue().getComment());
        assertEquals(3.0, entryCaptor.getValue().getDosage());
        assertEquals(5, entryCaptor.getValue().getTimesPerDay());

        assertEquals(startDate, entryCaptor.getValue().getStartDate());
        assertEquals(endDate, entryCaptor.getValue().getEndDate());
        assertEquals(Time.valueOf("10:00:00"), entryCaptor.getValue().getStartTime());
        assertEquals(Time.valueOf("17:00:00"), entryCaptor.getValue().getEndTime());
    }

    @Test
    public void whenCallingSaveAndSucceededShouldShowToast() throws Exception{
        this.activity.drugName.setText("NAME");
        this.activity.dosage.setText("3");
        this.activity.timesPerDay.setText("5");

        this.activity.save(null);

        List<Toast> toasts = Robolectric.getShadowApplication().getShownToasts();

        assertNotNull(toasts);
        assertEquals(1, toasts.size());
        Toast toast = toasts.get(0);
        assertEquals(Toast.LENGTH_SHORT, toast.getDuration());
        assertEquals(this.activity.addingPrescriptionSucceeded, ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void whenCallingSaveAndFailedShouldShowToast() throws Exception{
        this.activity.drugName.setText("NAME");
        this.activity.dosage.setText("0");
        this.activity.save(null);

        List<Toast> toasts = Robolectric.getShadowApplication().getShownToasts();

        assertNotNull(toasts);
        assertEquals(1, toasts.size());
        Toast toast = toasts.get(0);
        assertEquals(Toast.LENGTH_LONG, toast.getDuration());
        assertEquals("adding prescription failed. Invalid prescription dosage. Should be greater than zero.", ShadowToast.getTextOfLatestToast());
    }
}
