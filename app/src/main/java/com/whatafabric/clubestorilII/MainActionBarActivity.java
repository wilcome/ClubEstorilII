package com.whatafabric.clubestorilII;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActionBarActivity extends ActionBarActivity{
	MyPageAdapter pageAdapter;
	ViewPager pager;
	List<Fragment> fragments = null;
	
	//String fase2Div1_class_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_1DIV_1314/Clasificacion_General.htm";
    String fase2Div1_class_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_1_DIV_1415/Clasificacion_General.htm";
	static public final int FASE2DIV1_CLASS_ID = 12;
	
	//String fase2Div2_class_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_2DIV_1314/Clasificacion_General.htm";
    String fase2Div2_class_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_2_DIV_1415/Clasificacion_General.htm";
	static public final int FASE2DIV2_CLASS_ID = 13;

    //String fase2Div1_rounds_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_1DIV_1314/Jornadas.htm";
    String fase2Div1_rounds_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_1_DIV_1415/Jornadas.htm";
	static public final int FASE2DIV1_ROUNDS_ID = 22;

    //String fase2Div2_rounds_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_2DIV_1314/Jornadas.htm";
    String fase2Div2_rounds_ws = "http://cdestoril2.sagois.eu/FSALA_SENIOR_2_DIV_1415/Jornadas.htm";
	static public final int FASE2DIV2_ROUNDS_ID = 23;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("-kk-MainActionBarActivity", "onCreate");
        setContentView(R.layout.main);
        
        fragments = getFragments();
        
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
        
        final ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       

        // Create a tab listener that is called when the user changes tabs.
        TabListener tabListener = new TabListener() {
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                // show the given tab
            	pager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }

        };
        
        pager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                    	actionBar.setSelectedNavigationItem(position);
                    }
                });
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Gen.2ªDiv")
                        .setTabListener(tabListener));
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Jor.2ªDiv")
                        .setTabListener(tabListener));
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Gen.1ªDiv")
                        .setTabListener(tabListener));
        
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Jor.1ªDiv")
                        .setTabListener(tabListener));
        

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Pichichi")
                        .setTabListener(tabListener));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Deportividad")
                        .setTabListener(tabListener));

    }
    
    private List<Fragment> getFragments(){
    	List<Fragment> fList = new ArrayList<Fragment>();
    	fList.add( ClassificationFragment.newInstance(fase2Div2_class_ws,FASE2DIV2_CLASS_ID));
    	fList.add(RoundsFragment.newInstance(fase2Div2_rounds_ws,FASE2DIV2_ROUNDS_ID));
    	fList.add( ClassificationFragment.newInstance(fase2Div1_class_ws,FASE2DIV1_CLASS_ID));
    	fList.add(RoundsFragment.newInstance(fase2Div1_rounds_ws,FASE2DIV1_ROUNDS_ID));
        fList.add(GoalScorerFragment.newInstance());
        fList.add(SportsmanshipFragment.newInstance());
    	return fList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
    	private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
     
        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        //I do this intentionally although there is an open discussion about if it is good or not. 
        Log.d("-kk-MainActionBarActivity", "Force kill");
        System.exit(0);
    }

}
