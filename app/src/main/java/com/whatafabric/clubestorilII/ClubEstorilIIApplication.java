package com.whatafabric.clubestorilII;

import android.app.Application;
import android.util.Log;

import java.util.Vector;

public class ClubEstorilIIApplication extends Application{

	//States of the data stored in the Application class
	public static final int STOPPED  = 0;
	public static final int RUNNING  = 1;
	public static final int FINISHED = 2;

	//Initialize states
	private int fase2Div1ClassificationTaskStateFlag = STOPPED;
	private int fase2Div2ClassificationTaskStateFlag = STOPPED;
	private int fase2Div1RoundsTaskStateFlag = STOPPED;
	private int fase2Div2RoundsTaskStateFlag = STOPPED;
    private int sportsmanshipTaskStateFlag = STOPPED;
	private int goalScorerTaskStateFlag = STOPPED;

	//Tasks to store
	private ExtractClassificationTask fase2Div1ClassificationTask = null;
	private ExtractClassificationTask fase2Div2ClassificationTask = null;
	private ExtractRoundsTask fase2Div1RoundsTask = null;
	private ExtractRoundsTask fase2Div2RoundsTask = null;
    private ExtractSportsmanshipTask sportsmanshipTask = null;
	private ExtractGoalScorerTask goalScorerTask = null;

	//Objects to store
	private Vector<Vector<String>> fase2Div1ClassificationTable = null;
	private Vector<Vector<String>> fase2Div2ClassificationTable = null;
	private Vector<Vector<Vector<String>>> fase2Div1RoundsTable = null;
	private Vector<Vector<Vector<String>>> fase2Div2RoundsTable = null;
    private Vector<Vector<String>> sportsmanshipTable = null;
	private Vector<Vector<String>> goalScorerTable = null;


	public void onCreate(){
		Log.d("-kk-ClubEstorillApplication","OnCreate");
		setClassificationTaskState(STOPPED,MainActionBarActivity.FASE2DIV1_CLASS_ID);
		setClassificationTaskState(STOPPED,MainActionBarActivity.FASE2DIV2_CLASS_ID);
		setRoundsTaskState(STOPPED,MainActionBarActivity.FASE2DIV1_ROUNDS_ID);
		setRoundsTaskState(STOPPED,MainActionBarActivity.FASE2DIV2_ROUNDS_ID);
		setGoalScorerTaskState(STOPPED);
	}

	//Classification methods
	public void setClassificationTaskState(int state, int classId){
		Log.d("-kk-ClubEstoirilIIApplication", "setClassificationTaskState. State = " + state + ",classId = " + classId);
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			fase2Div1ClassificationTaskStateFlag= state;
			break;
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			fase2Div1ClassificationTaskStateFlag = state;
			break;
		}
	}

	public int getClassificationTaskState(int classId){
		Log.d("-kk-ClubEstoirilIIApplication", "getClassificationTaskState. classId = " + classId);
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			if(fase2Div1ClassificationTable != null){
				return fase2Div1ClassificationTaskStateFlag;
			}else{
				return STOPPED;
			}
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			if(fase2Div2ClassificationTable != null){
				return fase2Div2ClassificationTaskStateFlag;
			}else{
				return STOPPED;
			}
		}
		return -1;
	}

	public void setClassificationTask(ExtractClassificationTask task, int classId){
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			fase2Div1ClassificationTask = task;
			break;
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			fase2Div2ClassificationTask = task;
			break;
		}
	}

	public ExtractClassificationTask getClassificationTask(int classId){
		Log.d("-kk-ClubEstoirilIIApplication", "getClassificationTask. classId = "+classId);
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			return fase2Div1ClassificationTask;
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			return fase2Div2ClassificationTask;
		}

		return null;
	}

	public void setClassificationTable(Vector<Vector<String>> table, int classId){
		Log.d("-kk-ClubEstoirilIIApplication", "getClassificationTask. classId = "+classId);
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			fase2Div1ClassificationTable = table;
			break;
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			fase2Div2ClassificationTable = table;
			break;
		}
	}

	public Vector<Vector<String>> getClassificationTable(int classId){
		Log.d("-kk-ClubEstoirilIIApplication", "getClassificationTable. classId = "+classId);
		switch(classId){
		case MainActionBarActivity.FASE2DIV1_CLASS_ID:
			return fase2Div1ClassificationTable;
		case MainActionBarActivity.FASE2DIV2_CLASS_ID:
			return fase2Div2ClassificationTable;
		}

		return null;
	}



	// Rounds methods
	public void setRoundsTaskState(int state, int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "setRoundsTaskState. State = " + state + ",roundsId = " + roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			fase2Div1RoundsTaskStateFlag = state;
			break;
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			fase2Div2RoundsTaskStateFlag = state;
			break;
		}
	}

	public int getRoundsTaskState(int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "getRoundsTaskState. roundsId = "+roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			if(fase2Div1RoundsTable != null){
				return fase2Div1RoundsTaskStateFlag;
			}else{
				return STOPPED;
			}
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			if(fase2Div1RoundsTable != null){
				return fase2Div2RoundsTaskStateFlag;
			}else{
				return STOPPED;
			}
		}

		return -1;
	}

	public void setRoundsTask(ExtractRoundsTask task, int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "setRoundsTask. roundsId = "+roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			fase2Div1RoundsTask = task;
			break;
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			fase2Div2RoundsTask = task;
			break;
		}
	}

	public ExtractRoundsTask getRoundsTask(int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "getRoundsTask. roundsId = "+roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			return fase2Div1RoundsTask;
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			return fase2Div2RoundsTask;
		}

		return null;
	}

	public void setRoundsTable(Vector<Vector<Vector<String>>> table, int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "setRoundsTable. roundsId = "+roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			fase2Div1RoundsTable = table;
			break;
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			fase2Div2RoundsTable = table;
			break;
		}
	}

	public Vector<Vector<Vector<String>>> getRoundsTable(int roundsId){
		Log.d("-kk-ClubEstoirilIIApplication", "getRoundsTable. roundsId = "+roundsId);
		switch(roundsId){
		case MainActionBarActivity.FASE2DIV1_ROUNDS_ID:
			return fase2Div1RoundsTable;
		case MainActionBarActivity.FASE2DIV2_ROUNDS_ID:
			return fase2Div2RoundsTable;
		}

		return null;
	}


    //Sportsmanship methods
    public void setSportsmanshipTaskState(int state){
        Log.d("-kk-ClubEstoirilIIApplication", "setSportsmanshipTaskState.");
        sportsmanshipTaskStateFlag = state;
    }

    public int getSportsmanshipTaskState(){
        Log.d("-kk-ClubEstoirilIIApplication", "getSportsmanshipTaskState.");
        return sportsmanshipTaskStateFlag;
    }

    public void setSportsmanshipTask(ExtractSportsmanshipTask task){
        Log.d("-kk-ClubEstoirilIIApplication", "setSportsmanshipTask.");
        sportsmanshipTask = task;
    }

    public ExtractSportsmanshipTask getSportsmanshipTask(){
        Log.d("-kk-ClubEstoirilIIApplication", "getSportsmanshipTask.");
        return sportsmanshipTask;
    }

    public void setSportsmanshipTable(Vector<Vector<String>> table){
        Log.d("-kk-ClubEstoirilIIApplication", "setSportsmanshipTable.");
        sportsmanshipTable = table;
    }

    public Vector<Vector<String>> getSportsmanshipTable(){
        Log.d("-kk-ClubEstoirilIIApplication", "getSportsmanshipTable.");

        return sportsmanshipTable;
    }

	//GoalScorer methods
	public void setGoalScorerTaskState(int state){
		Log.d("-kk-ClubEstoirilIIApplication", "setGoalScorerTaskState.");
		goalScorerTaskStateFlag = state;
	}

	public int getGoalScorerTaskState(){
		Log.d("-kk-ClubEstoirilIIApplication", "getGoalScorerTaskState.");
		return goalScorerTaskStateFlag;
	}

	public void setGoalScorerTask(ExtractGoalScorerTask task){
		Log.d("-kk-ClubEstoirilIIApplication", "setGoalScorerTask.");
		goalScorerTask = task;
	}

	public ExtractGoalScorerTask getGoalScorerTask(){
		Log.d("-kk-ClubEstoirilIIApplication", "getGoalScorerTask.");
		return goalScorerTask;
	}

	public void setGoalScorerTable(Vector<Vector<String>> table){
		Log.d("-kk-ClubEstoirilIIApplication", "setGoalScorerTable.");
		goalScorerTable = table;
	}

	public Vector<Vector<String>> getGoalScorerTable(){
		Log.d("-kk-ClubEstoirilIIApplication", "getGoalScorerTable.");

		return goalScorerTable;
	}

}
