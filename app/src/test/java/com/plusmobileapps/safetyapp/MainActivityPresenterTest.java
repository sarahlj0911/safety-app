package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.main.MainActivityContract;
import com.plusmobileapps.safetyapp.main.MainActivityPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class MainActivityPresenterTest {

    @Mock
    private MainActivityContract.View view;

    private MainActivityPresenter presenter;

    @Before
    public void setupMainActivityPresenter() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainActivityPresenter(view);
    }

    @Test
    public void testNavButtonClicked() {

        presenter.navButtonClicked(1);
        verify(view).changePage(1);

        presenter.navButtonClicked(0);
        verify(view).changePage(0);
    }

    @Test
    public void testPageSwipedTo() {

        presenter.pageSwipedTo(2);
        verify(view).changeNavHighlight(R.id.navigation_history);

        presenter.pageSwipedTo(1);
        verify(view).changeNavHighlight(R.id.navigation_dashboard);

        presenter.pageSwipedTo(0);
        verify(view).changeNavHighlight(R.id.navigation_history);
    }



}
