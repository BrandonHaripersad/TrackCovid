package com.example.trackcovid.common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CaseTupleTest {

    private CaseTuple mCasetuple;

    @Before
    public void setUp() {
        mCasetuple = new CaseTuple(100000, 500, 200);
    }

    @Test
    public void getCases() {
        Integer caseResult = mCasetuple.getCases();
        assertThat(caseResult, is(equalTo(100000)));
    }

    @Test
    public void getDeaths() {
        Integer deathResult = mCasetuple.getDeaths();
        assertThat(deathResult, is(equalTo(500)));
    }

    @Test
    public void getRecoveries() {
        Integer caseRecoveries = mCasetuple.getRecoveries();
        assertThat(caseRecoveries, is(equalTo(200)));
    }
}