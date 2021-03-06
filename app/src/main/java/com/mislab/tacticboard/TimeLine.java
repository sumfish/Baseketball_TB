package com.mislab.tacticboard;

import java.util.Vector;

import com.google.android.material.slider.RangeSlider;
import com.mislab.tacticboard.CircularSeekBar.OnSeekChangeListener;

import android.graphics.Typeface;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

public class TimeLine extends Fragment {

	public int pathNumber =0;
	private ToggleButton toDefenderButton;
	private int progressHighest=0;
	private static Vector<Button> timelineButtons=new Vector<Button>();

	public interface CallbackInterface{
		public void seekBarStartTime(int progress);
		public void seekBarDuration(int duration);
		public void seekBarId(int id);
	}

	private CallbackInterface mCallback;

	public void onAttach(Activity activity){
		super.onAttach(activity);

		try{
			mCallback = (CallbackInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException (activity.toString()+"must implement TimeLine.CallbackInterface!");
		}

	}

	//private CircularSeekBar directionSeekBar;
	private CircularControl directionSeekBar;
	private int seekBarId, runlineId;
	private int timeLineSeekBarProgressLow;
	private int timeLineSeekBarProgressHigh;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

		return inflater.inflate(R.layout.fragment_timeline, container,false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		directionSeekBar = (CircularControl) getView().findViewById(R.id.circularSeekBar);
		directionSeekBar.setOnTouchListener(directionOnTouch);
		Button back_button = (Button) getView().findViewById(R.id.button_back_to_main_timeline);
		back_button.setOnClickListener(backButtonOnclick);
		toDefenderButton = (ToggleButton) getView().findViewById(R.id.change_to_defender_timeline);
		toDefenderButton.setOnClickListener(changeToDefenderTimelineOnclick);

		Resources resources = getResources();
		for (int i=1;i<=5;i++){ //offender
			Button b=(Button) getView().findViewById(resources.getIdentifier("textPlayer"+i, "id", getActivity().getPackageName()));
			b.setOnClickListener(turnToRespectiveTimeline);
			timelineButtons.add(b);
		}

		//ball
		Button ball=(Button) getView().findViewById(R.id.textBall);
		ball.setOnClickListener(turnToRespectiveTimeline);
		timelineButtons.add(ball);

		for (int i=1;i<=5;i++){  //defender
			Button b=(Button) getView().findViewById(resources.getIdentifier("textD"+i, "id", getActivity().getPackageName()));
			b.setOnClickListener(turnToRespectiveTimeline);
			timelineButtons.add(b);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
	}

	@Override
	public void onPause(){
		super.onPause();
		getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
	}

	public void changeLayout(int whichLayout){
		disableAllPlayerTimeline();
		RelativeLayout target = (RelativeLayout) getView().findViewById(R.id.main_timeline_relativelayout);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.all_player_wrap);
		target.setVisibility(View.VISIBLE);
		directionSeekBar.setVisibility(View.VISIBLE);
		directionSeekBar.invalidate();
		TextView text = (TextView) getView().findViewById(R.id.player_timeline_title);

		switch(whichLayout){
			case 1:
				target = (RelativeLayout) getView().findViewById(R.id.player1_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Player1 TimeLine");
				break;
			case 2:
				target = (RelativeLayout) getView().findViewById(R.id.player2_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Player2 TimeLine");
				break;
			case 3:
				target = (RelativeLayout) getView().findViewById(R.id.player3_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Player3 TimeLine");
				break;
			case 4:
				target = (RelativeLayout) getView().findViewById(R.id.player4_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Player4 TimeLine");
				break;
			case 5:
				target = (RelativeLayout) getView().findViewById(R.id.player5_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Player5 TimeLine");
				break;
			case 6:
				target = (RelativeLayout) getView().findViewById(R.id.ball_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Ball TimeLine");
				break;
			case 7:
				target = (RelativeLayout) getView().findViewById(R.id.defender1_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Defender1 TimeLine");
				break;
			case 8:
				target = (RelativeLayout) getView().findViewById(R.id.defender2_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Defender2 TimeLine");
				break;
			case 9:
				target = (RelativeLayout) getView().findViewById(R.id.defender3_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Defender3 TimeLine");
				break;
			case 10:
				target = (RelativeLayout) getView().findViewById(R.id.defender4_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Defender4 TimeLine");
				break;
			case 11:
				target = (RelativeLayout) getView().findViewById(R.id.defender5_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				text.setText("Defender5 TimeLine");
				break;

		}
	}

	public void disableAllPlayerTimeline(){
		RelativeLayout target = (RelativeLayout) getView().findViewById(R.id.all_player_wrap);
		target.setVisibility(View.GONE);
		/**************後面要放全部player的timelinewrap****************/
		target = (RelativeLayout) getView().findViewById(R.id.player1_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.player2_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.player3_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.player4_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.player5_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.defender1_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.defender2_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.defender3_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.defender4_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.defender5_timeline_wrap);
		target.setVisibility(View.GONE);
		target = (RelativeLayout) getView().findViewById(R.id.ball_timeline_wrap);
		target.setVisibility(View.GONE);
	}


	private OnClickListener backButtonOnclick = new OnClickListener(){//"��^�D�ɶ��b"
		@Override
		public void onClick(View v) {  //"返回主時間軸"
			RelativeLayout target = (RelativeLayout) getView().findViewById(R.id.main_timeline_relativelayout);
			target.setVisibility(View.VISIBLE);
			target = (RelativeLayout) getView().findViewById(R.id.allplayer_timeline_wrap);
			target.setVisibility(View.VISIBLE);
			target = (RelativeLayout) getView().findViewById(R.id.defender_timeline_wrap);
			target.setVisibility(View.GONE);
			disableAllPlayerTimeline();
			directionSeekBar.setVisibility(View.GONE);
			toDefenderButton.setChecked(false);
		}
	};

	private OnClickListener changeToDefenderTimelineOnclick = new OnClickListener() {
		public void onClick(View v) {
			// 當按鈕第一次被點擊時候響應的事件
			if (toDefenderButton.isChecked()) {
				RelativeLayout target = (RelativeLayout) getView().findViewById(R.id.allplayer_timeline_wrap);
				target.setVisibility(View.GONE);
				target = (RelativeLayout) getView().findViewById(R.id.defender_timeline_wrap);
				target.setVisibility(View.VISIBLE);
			}
			// 當按鈕再次被點擊時候響應的事件
			else {
				RelativeLayout target = (RelativeLayout) getView().findViewById(R.id.allplayer_timeline_wrap);
				target.setVisibility(View.VISIBLE);
				target = (RelativeLayout) getView().findViewById(R.id.defender_timeline_wrap);
				target.setVisibility(View.GONE);
			}
		}
	};


	public void setSeekBarId(int id){
		seekBarId = id;
	}

	public void setRunlineId(int id){
		runlineId =id;
		Log.i("debug", "Set !   RunLineId="+Integer.toString(runlineId));
	}

	public void changeSeekBarId(int searchId,int changeToId){

		/*all player's timeline*/
		TextView alltexttmp = null;
		LinearLayout allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		if(allplayertextplace.findViewById(searchId)!=null){
			alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
		}
		else{
			allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
			if(allplayertextplace.findViewById(searchId)!=null){
				alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
			}
			else{
				allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
				if(allplayertextplace.findViewById(searchId)!=null){
					alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
				}
				else{
					allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
					if(allplayertextplace.findViewById(searchId)!=null){
						alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
					}
					else{
						allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
						if(allplayertextplace.findViewById(searchId)!=null){
							alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
						}
						else{
							allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
							if(allplayertextplace.findViewById(searchId)!=null){
								alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
							}
							else{
								allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D1timeline);
								if(allplayertextplace.findViewById(searchId)!=null){
									alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
								}
								else{
									allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D2timeline);
									if(allplayertextplace.findViewById(searchId)!=null){
										alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
									}
									else{
										allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D3timeline);
										if(allplayertextplace.findViewById(searchId)!=null){
											alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
										}
										else{
											allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D4timeline);
											if(allplayertextplace.findViewById(searchId)!=null){
												alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
											}
											else{
												allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D5timeline);
												if(allplayertextplace.findViewById(searchId)!=null){
													alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		alltexttmp.setText(Integer.toString(Integer.parseInt(alltexttmp.getText().toString())-1));
		/*這一定要的*/
		alltexttmp.setId(changeToId);
		alltexttmp.invalidate();






		TextView texttmp = null;
		LinearLayout playertextplace=(LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
		if(playertextplace.findViewById(searchId)!=null){
			texttmp = (TextView) playertextplace.findViewById(searchId);
		}
		else{
			playertextplace=(LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
			if(playertextplace.findViewById(searchId)!=null){
				texttmp = (TextView) playertextplace.findViewById(searchId);
			}
			else{
				playertextplace=(LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
				if(playertextplace.findViewById(searchId)!=null){
					texttmp = (TextView) playertextplace.findViewById(searchId);
				}
				else{
					playertextplace=(LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
					if(playertextplace.findViewById(searchId)!=null){
						texttmp = (TextView) playertextplace.findViewById(searchId);
					}
					else{
						playertextplace=(LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
						if(playertextplace.findViewById(searchId)!=null){
							texttmp = (TextView) playertextplace.findViewById(searchId);
						}
						else{
							playertextplace=(LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
							if(playertextplace.findViewById(searchId)!=null){
								texttmp = (TextView) playertextplace.findViewById(searchId);
							}
							else{
								playertextplace=(LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
								if(playertextplace.findViewById(searchId)!=null){
									texttmp = (TextView) playertextplace.findViewById(searchId);
								}
								else{
									playertextplace=(LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
									if(playertextplace.findViewById(searchId)!=null){
										texttmp = (TextView) playertextplace.findViewById(searchId);
									}
									else{
										playertextplace=(LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
										if(playertextplace.findViewById(searchId)!=null){
											texttmp = (TextView) playertextplace.findViewById(searchId);
										}
										else{
											playertextplace=(LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
											if(playertextplace.findViewById(searchId)!=null){
												texttmp = (TextView) playertextplace.findViewById(searchId);
											}
											else{
												playertextplace=(LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
												if(playertextplace.findViewById(searchId)!=null){
													texttmp = (TextView) playertextplace.findViewById(searchId);
												}
												else{
													texttmp=null;
												}
											}
										}
									}
								}
							}
						}

					}
				}
			}
		}
		texttmp.setText(Integer.toString(Integer.parseInt(texttmp.getText().toString())-1));
		/*這一定要的*/
		texttmp.setId(changeToId);
		texttmp.invalidate();


		ImageView rmtmp = null;
		LinearLayout playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player1_path_rm_button_place);
		if(playerrmbuttonplace.findViewById(searchId)!=null){
			rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
		}
		else{
			playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player2_path_rm_button_place);
			if(playerrmbuttonplace.findViewById(searchId)!=null){
				rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
			}
			else{
				playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player3_path_rm_button_place);
				if(playerrmbuttonplace.findViewById(searchId)!=null){
					rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
				}
				else{
					playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player4_path_rm_button_place);
					if(playerrmbuttonplace.findViewById(searchId)!=null){
						rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
					}
					else{
						playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player5_path_rm_button_place);
						if(playerrmbuttonplace.findViewById(searchId)!=null){
							rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
						}
						else{
							playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender1_path_rm_button_place);
							if(playerrmbuttonplace.findViewById(searchId)!=null){
								rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
							}
							else{
								playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender2_path_rm_button_place);
								if(playerrmbuttonplace.findViewById(searchId)!=null){
									rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
								}
								else{
									playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender3_path_rm_button_place);
									if(playerrmbuttonplace.findViewById(searchId)!=null){
										rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
									}
									else{
										playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender4_path_rm_button_place);
										if(playerrmbuttonplace.findViewById(searchId)!=null){
											rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
										}
										else{
											playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender5_path_rm_button_place);
											if(playerrmbuttonplace.findViewById(searchId)!=null){
												rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
											}
											else{
												playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.ball_path_rm_button_place);
												if(playerrmbuttonplace.findViewById(searchId)!=null){
													rmtmp = (ImageView) playerrmbuttonplace.findViewById(searchId);
												}
												else{
													rmtmp=null;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		rmtmp.setId(changeToId);
		rmtmp.invalidate();



		MySeekBar tmp = null;
		LinearLayout linearLayout=(LinearLayout) getView().findViewById(R.id.player1_timeline_place);
		if(linearLayout.findViewById(searchId)!=null){
			tmp = (MySeekBar) linearLayout.findViewById(searchId);
		}
		else{
			linearLayout=(LinearLayout) getView().findViewById(R.id.player2_timeline_place);
			if(linearLayout.findViewById(searchId)!=null){
				tmp = (MySeekBar) linearLayout.findViewById(searchId);
			}
			else{
				linearLayout=(LinearLayout) getView().findViewById(R.id.player3_timeline_place);
				if(linearLayout.findViewById(searchId)!=null){
					tmp = (MySeekBar) linearLayout.findViewById(searchId);
				}
				else{
					linearLayout=(LinearLayout) getView().findViewById(R.id.player4_timeline_place);
					if(linearLayout.findViewById(searchId)!=null){
						tmp = (MySeekBar) linearLayout.findViewById(searchId);
					}
					else{
						linearLayout=(LinearLayout) getView().findViewById(R.id.player5_timeline_place);
						if(linearLayout.findViewById(searchId)!=null){
							tmp = (MySeekBar) linearLayout.findViewById(searchId);
						}
						else{
							linearLayout=(LinearLayout) getView().findViewById(R.id.ball_timeline_place);
							if(linearLayout.findViewById(searchId)!=null){
								tmp = (MySeekBar) linearLayout.findViewById(searchId);
							}
							else{
								linearLayout=(LinearLayout) getView().findViewById(R.id.defender1_timeline_place);
								if(linearLayout.findViewById(searchId)!=null){
									tmp = (MySeekBar) linearLayout.findViewById(searchId);
								}
								else{
									linearLayout=(LinearLayout) getView().findViewById(R.id.defender2_timeline_place);
									if(linearLayout.findViewById(searchId)!=null){
										tmp = (MySeekBar) linearLayout.findViewById(searchId);
									}
									else{
										linearLayout=(LinearLayout) getView().findViewById(R.id.defender3_timeline_place);
										if(linearLayout.findViewById(searchId)!=null){
											tmp = (MySeekBar) linearLayout.findViewById(searchId);
										}
										else{
											linearLayout=(LinearLayout) getView().findViewById(R.id.defender4_timeline_place);
											if(linearLayout.findViewById(searchId)!=null){
												tmp = (MySeekBar) linearLayout.findViewById(searchId);
											}
											else{
												linearLayout=(LinearLayout) getView().findViewById(R.id.defender5_timeline_place);
												if(linearLayout.findViewById(searchId)!=null){
													tmp = (MySeekBar) linearLayout.findViewById(searchId);
												}
												else{
													tmp=null;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		tmp.setId(changeToId);
		tmp.invalidate();

	}

	public void setPathNumberText(int searchId , int showText){

		TextView alltexttmp = null;
		LinearLayout allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		if(allplayertextplace.findViewById(searchId)!=null){
			alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
		}
		else{
			allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
			if(allplayertextplace.findViewById(searchId)!=null){
				alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
			}
			else{
				allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
				if(allplayertextplace.findViewById(searchId)!=null){
					alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
				}
				else{
					allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
					if(allplayertextplace.findViewById(searchId)!=null){
						alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
					}
					else{
						allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
						if(allplayertextplace.findViewById(searchId)!=null){
							alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
						}
						else{
							allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
							if(allplayertextplace.findViewById(searchId)!=null){
								alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
							}
							else{
								allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D1timeline);
								if(allplayertextplace.findViewById(searchId)!=null){
									alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
								}
								else{
									allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D2timeline);
									if(allplayertextplace.findViewById(searchId)!=null){
										alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
									}
									else{
										allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D3timeline);
										if(allplayertextplace.findViewById(searchId)!=null){
											alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
										}
										else{
											allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D4timeline);
											if(allplayertextplace.findViewById(searchId)!=null){
												alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
											}
											else{
												allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D5timeline);
												if(allplayertextplace.findViewById(searchId)!=null){
													alltexttmp = (TextView) allplayertextplace.findViewById(searchId);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		alltexttmp.setText(Integer.toString(showText));
		/*這一定要的*/
		alltexttmp.invalidate();


		TextView texttmp = null;
		LinearLayout playertextplace=(LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
		if(playertextplace.findViewById(searchId)!=null){
			texttmp = (TextView) playertextplace.findViewById(searchId);
		}
		else{
			playertextplace=(LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
			if(playertextplace.findViewById(searchId)!=null){
				texttmp = (TextView) playertextplace.findViewById(searchId);
			}
			else{
				playertextplace=(LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
				if(playertextplace.findViewById(searchId)!=null){
					texttmp = (TextView) playertextplace.findViewById(searchId);
				}
				else{
					playertextplace=(LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
					if(playertextplace.findViewById(searchId)!=null){
						texttmp = (TextView) playertextplace.findViewById(searchId);
					}
					else{
						playertextplace=(LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
						if(playertextplace.findViewById(searchId)!=null){
							texttmp = (TextView) playertextplace.findViewById(searchId);
						}
						else{
							playertextplace=(LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
							if(playertextplace.findViewById(searchId)!=null){
								texttmp = (TextView) playertextplace.findViewById(searchId);
							}
							else{
								playertextplace=(LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
								if(playertextplace.findViewById(searchId)!=null){
									texttmp = (TextView) playertextplace.findViewById(searchId);
								}
								else{
									playertextplace=(LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
									if(playertextplace.findViewById(searchId)!=null){
										texttmp = (TextView) playertextplace.findViewById(searchId);
									}
									else{
										playertextplace=(LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
										if(playertextplace.findViewById(searchId)!=null){
											texttmp = (TextView) playertextplace.findViewById(searchId);
										}
										else{
											playertextplace=(LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
											if(playertextplace.findViewById(searchId)!=null){
												texttmp = (TextView) playertextplace.findViewById(searchId);
											}
											else{
												playertextplace=(LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
												if(playertextplace.findViewById(searchId)!=null){
													texttmp = (TextView) playertextplace.findViewById(searchId);
												}
												else{
													texttmp=null;
												}
											}
										}
									}
								}
							}
						}

					}
				}
			}
		}
		texttmp.setText(Integer.toString(showText));
		texttmp.invalidate();
	}

	public void setSeekBarProgressLow(int low){ //main fragment那邊增加一筆新的軌跡
		timeLineSeekBarProgressLow =low;
		timeLineSeekBarProgressHigh =low+1;
	}

	//renew play button total_max_time;
	public int getsetSeekBarProgressLow(){ return timeLineSeekBarProgressLow;}

	public void clearRecordLayout(){
		LinearLayout linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		linearLayout.removeAllViews();


		LinearLayout relative = (LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
		relative.removeAllViews();

		relative = (LinearLayout) getView().findViewById(R.id.player1_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player2_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player3_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player4_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player5_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.ball_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender1_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender2_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender3_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender4_timeline_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender5_timeline_place);
		relative.removeAllViews();

		relative = (LinearLayout) getView().findViewById(R.id.player1_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player2_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player3_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player4_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.player5_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.ball_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender1_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender2_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender3_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender4_path_rm_button_place);
		relative.removeAllViews();
		relative = (LinearLayout) getView().findViewById(R.id.defender5_path_rm_button_place);
		relative.removeAllViews();


		pathNumber =0;
	}

	public void seekBarProgressUndo(){
		timeLineSeekBarProgressLow=timeLineSeekBarProgressLow-1;
	}

	public void removeOneTimeline(int input_id){


		/*******************要刪掉rmbutton 跟  數字 然後要把數字的數量-1*********************/

		/*all player's timeline 找尋所有timeline裡的某一筆後消除*/
		TextView alltexttmp = null;
		LinearLayout allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		if(allplayertextplace.findViewById(input_id)!=null){
			alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
		}
		else{
			allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
			if(allplayertextplace.findViewById(input_id)!=null){
				alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
			}
			else{
				allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
				if(allplayertextplace.findViewById(input_id)!=null){
					alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
				}
				else{
					allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
					if(allplayertextplace.findViewById(input_id)!=null){
						alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
					}
					else{
						allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
						if(allplayertextplace.findViewById(input_id)!=null){
							alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
						}
						else{
							allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
							if(allplayertextplace.findViewById(input_id)!=null){
								alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
							}
							else{
								allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D1timeline);
								if(allplayertextplace.findViewById(input_id)!=null){
									alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
								}
								else{
									allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D2timeline);
									if(allplayertextplace.findViewById(input_id)!=null){
										alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
									}
									else{
										allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D3timeline);
										if(allplayertextplace.findViewById(input_id)!=null){
											alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
										}
										else{
											allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D4timeline);
											if(allplayertextplace.findViewById(input_id)!=null){
												alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
											}
											else{
												allplayertextplace=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_D5timeline);
												if(allplayertextplace.findViewById(input_id)!=null){
													alltexttmp = (TextView) allplayertextplace.findViewById(input_id);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		allplayertextplace.removeView(alltexttmp);













		/*player's own timeline*/
		TextView texttmp = null;
		LinearLayout playertextplace=(LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
		if(playertextplace.findViewById(input_id)!=null){
			texttmp = (TextView) playertextplace.findViewById(input_id);
		}
		else{
			playertextplace=(LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
			if(playertextplace.findViewById(input_id)!=null){
				texttmp = (TextView) playertextplace.findViewById(input_id);
			}
			else{
				playertextplace=(LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
				if(playertextplace.findViewById(input_id)!=null){
					texttmp = (TextView) playertextplace.findViewById(input_id);
				}
				else{
					playertextplace=(LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
					if(playertextplace.findViewById(input_id)!=null){
						texttmp = (TextView) playertextplace.findViewById(input_id);
					}
					else{
						playertextplace=(LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
						if(playertextplace.findViewById(input_id)!=null){
							texttmp = (TextView) playertextplace.findViewById(input_id);
						}
						else{
							playertextplace=(LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
							if(playertextplace.findViewById(input_id)!=null){
								texttmp = (TextView) playertextplace.findViewById(input_id);
							}
							else{
								playertextplace=(LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
								if(playertextplace.findViewById(input_id)!=null){
									texttmp = (TextView) playertextplace.findViewById(input_id);
								}
								else{
									playertextplace=(LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
									if(playertextplace.findViewById(input_id)!=null){
										texttmp = (TextView) playertextplace.findViewById(input_id);
									}
									else{
										playertextplace=(LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
										if(playertextplace.findViewById(input_id)!=null){
											texttmp = (TextView) playertextplace.findViewById(input_id);
										}
										else{
											playertextplace=(LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
											if(playertextplace.findViewById(input_id)!=null){
												texttmp = (TextView) playertextplace.findViewById(input_id);
											}
											else{
												playertextplace=(LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
												if(playertextplace.findViewById(input_id)!=null){
													texttmp = (TextView) playertextplace.findViewById(input_id);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		pathNumber--;
		playertextplace.removeView(texttmp);









		ImageView rmtmp = null;
		LinearLayout playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player1_path_rm_button_place);
		if(playerrmbuttonplace.findViewById(input_id)!=null){
			rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
		}
		else{
			playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player2_path_rm_button_place);
			if(playerrmbuttonplace.findViewById(input_id)!=null){
				rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
			}
			else{
				playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player3_path_rm_button_place);
				if(playerrmbuttonplace.findViewById(input_id)!=null){
					rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
				}
				else{
					playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player4_path_rm_button_place);
					if(playerrmbuttonplace.findViewById(input_id)!=null){
						rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
					}
					else{
						playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.player5_path_rm_button_place);
						if(playerrmbuttonplace.findViewById(input_id)!=null){
							rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
						}
						else{
							playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender1_path_rm_button_place);
							if(playerrmbuttonplace.findViewById(input_id)!=null){
								rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
							}
							else{
								playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender2_path_rm_button_place);
								if(playerrmbuttonplace.findViewById(input_id)!=null){
									rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
								}
								else{
									playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender3_path_rm_button_place);
									if(playerrmbuttonplace.findViewById(input_id)!=null){
										rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
									}
									else{
										playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender4_path_rm_button_place);
										if(playerrmbuttonplace.findViewById(input_id)!=null){
											rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
										}
										else{
											playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.defender5_path_rm_button_place);
											if(playerrmbuttonplace.findViewById(input_id)!=null){
												rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
											}
											else{
												playerrmbuttonplace=(LinearLayout) getView().findViewById(R.id.ball_path_rm_button_place);
												if(playerrmbuttonplace.findViewById(input_id)!=null){
													rmtmp = (ImageView) playerrmbuttonplace.findViewById(input_id);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		playerrmbuttonplace.removeView(rmtmp);









		RangeSlider tmp = null;
		LinearLayout linearLayout=(LinearLayout) getView().findViewById(R.id.player1_timeline_place);
		if(linearLayout.findViewById(input_id)!=null){
			tmp = (RangeSlider) linearLayout.findViewById(input_id);
		}
		else{
			linearLayout=(LinearLayout) getView().findViewById(R.id.player2_timeline_place);
			if(linearLayout.findViewById(input_id)!=null){
				tmp = (RangeSlider) linearLayout.findViewById(input_id);
			}
			else{
				linearLayout=(LinearLayout) getView().findViewById(R.id.player3_timeline_place);
				if(linearLayout.findViewById(input_id)!=null){
					tmp = (RangeSlider) linearLayout.findViewById(input_id);
				}
				else{
					linearLayout=(LinearLayout) getView().findViewById(R.id.player4_timeline_place);
					if(linearLayout.findViewById(input_id)!=null){
						tmp = (RangeSlider) linearLayout.findViewById(input_id);
					}
					else{
						linearLayout=(LinearLayout) getView().findViewById(R.id.player5_timeline_place);
						if(linearLayout.findViewById(input_id)!=null){
							tmp = (RangeSlider) linearLayout.findViewById(input_id);
						}
						else{
							linearLayout=(LinearLayout) getView().findViewById(R.id.defender1_timeline_place);
							if(linearLayout.findViewById(input_id)!=null){
								tmp = (RangeSlider) linearLayout.findViewById(input_id);
							}
							else{
								linearLayout=(LinearLayout) getView().findViewById(R.id.defender2_timeline_place);
								if(linearLayout.findViewById(input_id)!=null){
									tmp = (RangeSlider) linearLayout.findViewById(input_id);
								}
								else{
									linearLayout=(LinearLayout) getView().findViewById(R.id.defender3_timeline_place);
									if(linearLayout.findViewById(input_id)!=null){
										tmp = (RangeSlider) linearLayout.findViewById(input_id);
									}
									else{
										linearLayout=(LinearLayout) getView().findViewById(R.id.defender4_timeline_place);
										if(linearLayout.findViewById(input_id)!=null){
											tmp = (RangeSlider) linearLayout.findViewById(input_id);
										}
										else{
											linearLayout=(LinearLayout) getView().findViewById(R.id.defender5_timeline_place);
											if(linearLayout.findViewById(input_id)!=null){
												tmp = (RangeSlider) linearLayout.findViewById(input_id);
											}
											else{
												linearLayout=(LinearLayout) getView().findViewById(R.id.ball_timeline_place);
												if(linearLayout.findViewById(input_id)!=null){
													tmp = (RangeSlider) linearLayout.findViewById(input_id);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
		linearLayout.removeView(tmp);






		/*LinearLayout linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}

		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		if(linearLayout.findViewById(input_id)!=null){
			RangeSlider tmp = (RangeSlider) linearLayout.findViewById(input_id);
			Log.i("debug", "Id: "+Integer.toString(tmp.getId())+" seekbar been removed.");
			linearLayout.removeView(tmp);
		}*/

	}

	/*Load strategy's condition*/
	public void createSeekbar(int player,int id,int progresslow,int duration){
		//LinearLayout linearLayout=null;
		/*
		LinearLayout linearLayout=null;
		if(player==1){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		}
		else if (player==2){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		}
		else if (player==3){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		}
		else if (player==4){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		}
		else if (player==5){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		}
		else if (player==6){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		}
		else {
			Log.d("debug", "Error! player can't found.  Player code = "+Integer.toString(player));
		}

		mySeekBar tmp = new mySeekBar(getActivity());
		LayoutParams lp = new LayoutParams(100,50);
		*/
		/***************load***********************/
		/*
		tmp.setProgressLow(progresslow);
		tmp.setProgressHigh(progresslow+duration);
		lp.topMargin=55;
		tmp.setLayoutParams(lp);
		tmp.setId(id);
		tmp.setOnSeekBarChangeListener(mySeekBarOnChange);
		linearLayout.addView(tmp); //�[�J�e���W
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		Vector<Integer> input=new Vector();
		input.add(id);
		input.add(progresslow);
		input.add(duration);
		mainfrag.set_seekBar_to_RunBag(input);
		mCallback.seekBarStartTime(progresslow);
		mCallback.seekBarDuration(id);
		Log.d("debug", "Load!!!!!SeekBarId = "+Integer.toString(id));
		Log.d("debug", "Load!!!!!New View's id = "+Integer.toString(tmp.getId()));
		*/
		/*****************************************/



		LinearLayout linearLayout=null;
		LinearLayout playertimelineplace = null;
		LinearLayout playerpathnumplace=null;
		LinearLayout playerrmbutton=null;
		if(player==1){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player1_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player1_path_rm_button_place);
			pathNumber++;
		}
		else if (player==2){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player2_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player2_path_rm_button_place);
			pathNumber++;
		}
		else if (player==3){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player3_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player3_path_rm_button_place);
			pathNumber++;
		}
		else if (player==4){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player4_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player4_path_rm_button_place);
			pathNumber++;
		}
		else if (player==5){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player5_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player5_path_rm_button_place);
			pathNumber++;
		}
		else if (player==6){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.ball_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.ball_path_rm_button_place);
			pathNumber++;
		}
		else if (player==7){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D1timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender1_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender1_path_rm_button_place);
			pathNumber++;
		}
		else if (player==8){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D2timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender2_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender2_path_rm_button_place);
			pathNumber++;
		}
		else if (player==9){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D3timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender3_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender3_path_rm_button_place);
			pathNumber++;
		}
		else if (player==10){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D4timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender4_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender4_path_rm_button_place);
			pathNumber++;
		}
		else if (player==11){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D5timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender5_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender5_path_rm_button_place);
			pathNumber++;
		}
		else {
			Log.d("debug", "Error! player can't found.  Player code = "+Integer.toString(player));
		}


		/*Add pathnum*/
		/*all player's timeline*/
		TextView alltext = new TextView(getActivity());
		LayoutParams alltextlp = new LayoutParams(60,60);
		alltextlp.setMargins(5, 20, 10, 0);
		alltext.setLayoutParams(alltextlp);
		alltext.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		alltext.setText(Integer.toString(pathNumber));
		alltext.setBackgroundResource(R.drawable.path_num_3);
		alltext.setId(id);
		Log.d("seekbar", "set text id = "+Integer.toString(id));
		linearLayout.addView(alltext);

		/*player's own timeline*/
		TextView text = new TextView(getActivity());
		LayoutParams textlp = new LayoutParams(60,60);
		textlp.setMargins(5, 20, 10, 0);
		text.setLayoutParams(textlp);
		text.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		text.setText(Integer.toString(pathNumber));
		text.setTextColor(ResourcesCompat.getColor(getResources(),R.color.yellow, null));
		text.setBackgroundResource(R.drawable.path_num_3);
		text.setId(id);
		playerpathnumplace.addView(text);
		/*			 */

		/*Add SeekBar*/
		/**TODO setProgressHigh可以設定後滑塊的位置**/
		/****************Load*******************/
		/*
		MySeekBar tmp = new MySeekBar(getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

		tmp.setProgressLow(progresslow);
		tmp.setProgressHigh(progresslow+duration);
		lp.topMargin=55;
		tmp.setId(id);
		tmp.setOnSeekBarChangeListener(mySeekBarOnChange);
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		playertimelineplace.addView(tmp);

		Vector<Integer> input=new Vector();
		input.add(id);
		input.add(progresslow);//這也要記得改!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		input.add(duration);
		mainfrag.setSeekBarToRunBag(input);
		mCallback.seekBarStartTime(progresslow);
		mCallback.seekBarDuration(id);
		Log.d("seekbar", "Create SeekBar! ID = "+Integer.toString(id));
		*/
		/*************************************/
		/*			*/

		/*Add RangeSlider*/
		RangeSlider slider=new RangeSlider(getActivity());
		slider.setValueFrom(0);
		//Log.d("seekbar", "get = "+Float.toString(slider.getMinSeparation()));
		slider.setMinSeparation(2);
		slider.setValues((float)timeLineSeekBarProgressLow,(float)timeLineSeekBarProgressLow+1);
		slider.setValueTo(10);
		slider.setStepSize(1);
		slider.setThumbRadius(12);
		slider.setId(seekBarId);
		slider.addOnChangeListener(rangesliderOnchange);
		playertimelineplace.addView(slider);
		Log.d("seekbar", "Create SeekBar! ID = "+Integer.toString(runlineId));

		Vector<Integer> input=new Vector();
		input.add(runlineId);
		input.add(timeLineSeekBarProgressLow);//這也要記得改!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		input.add(1);
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		mainfrag.setSeekBarToRunBag(input);
		mCallback.seekBarStartTime(timeLineSeekBarProgressLow);
		mCallback.seekBarDuration(1);


		/*Add rmbutton*/
		/*
		ImageView rmbutton = new ImageView(getActivity());
		LayoutParams rmbuttonlp = new LayoutParams(60,60);
		rmbuttonlp.setMargins(10, 20, 0, 0);
		rmbutton.setLayoutParams(rmbuttonlp);
		rmbutton.setImageResource(R.drawable.btn_delete);
		rmbutton.setId(id);
		Log.d("seekbar", "rmbutton ID = "+Integer.toString(id));
		rmbutton.setOnTouchListener(rmbuttonListener);
		playerrmbutton.addView(rmbutton);
		*/
	}


	/*Normal record's condition*/
	public void createSeekbar(int player){
		//LinearLayout linearLayout=null;
		LinearLayout linearLayout=null;
		LinearLayout playertimelineplace = null;
		LinearLayout playerpathnumplace=null;
		LinearLayout playerrmbutton=null;
		if(player==1){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player1_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player1_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player1_path_rm_button_place);
			pathNumber++;
		}
		else if (player==2){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player2_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player2_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player2_path_rm_button_place);
			pathNumber++;
		}
		else if (player==3){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player3_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player3_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player3_path_rm_button_place);
			pathNumber++;
		}
		else if (player==4){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player4_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player4_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player4_path_rm_button_place);
			pathNumber++;
		}
		else if (player==5){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.player5_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.player5_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.player5_path_rm_button_place);
			pathNumber++;
		}
		else if (player==6){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.ball_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.ball_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.ball_path_rm_button_place);
			pathNumber++;
		}
		else if (player==7){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D1timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender1_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender1_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender1_path_rm_button_place);
			pathNumber++;
		}
		else if (player==8){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D2timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender2_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender2_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender2_path_rm_button_place);
			pathNumber++;
		}
		else if (player==9){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D3timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender3_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender3_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender3_path_rm_button_place);
			pathNumber++;
		}
		else if (player==10){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D4timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender4_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender4_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender4_path_rm_button_place);
			pathNumber++;
		}
		else if (player==11){
			linearLayout = (LinearLayout) getView().findViewById(R.id.LinearLayout_of_D5timeline);
			playertimelineplace = (LinearLayout) getView().findViewById(R.id.defender5_timeline_place);
			playerpathnumplace=(LinearLayout) getView().findViewById(R.id.defender5_pathNum_place);
			playerrmbutton=(LinearLayout) getView().findViewById(R.id.defender5_path_rm_button_place);
			pathNumber++;
		}
		else {
			Log.d("debug", "Error! player can't found.  Player code = "+Integer.toString(player));
		}

		//LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);




		/*Add pathnum*/
		/*all player's timeline*/
		TextView alltext = new TextView(getActivity());
		LayoutParams alltextlp = new LayoutParams(60,60);
		alltextlp.setMargins(5, 20, 10, 0);
		alltext.setLayoutParams(alltextlp);
		alltext.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		alltext.setText(Integer.toString(pathNumber));
		alltext.setBackgroundResource(R.drawable.path_num_3);
		alltext.setId(seekBarId);
		linearLayout.addView(alltext);

		/*player's own timeline*/
		TextView text = new TextView(getActivity());
		LayoutParams textlp = new LayoutParams(60,60);
		textlp.setMargins(5, 30, 10, 0);
		text.setLayoutParams(textlp);
		text.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		text.setText(Integer.toString(pathNumber));
		text.setTextColor(ResourcesCompat.getColor(getResources(),R.color.blue, null));
		text.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
		text.setBackgroundResource(R.drawable.path_num_3);
		text.setId(seekBarId);
		playerpathnumplace.addView(text);
		/*			 */


		/*Add RangeSlider*/
		RangeSlider slider=new RangeSlider(getActivity());
		slider.setValueFrom(0);
		//Log.d("seekbar", "get = "+Float.toString(slider.getMinSeparation()));
		slider.setMinSeparation(2);
		slider.setValues((float)timeLineSeekBarProgressLow,(float)timeLineSeekBarProgressLow+1);
		slider.setValueTo(10);
		//Log.d("seekbar", "get = "+Float.toString(slider.getMinSeparation()));
		slider.setStepSize(1);
		slider.setThumbRadius(12);
		slider.setId(seekBarId);
		slider.addOnChangeListener(rangesliderOnchange);
		playertimelineplace.addView(slider);
		Log.d("seekbar", "Create SeekBar! ID = "+Integer.toString(seekBarId));

		Vector<Integer> input=new Vector();
		input.add(runlineId);
		input.add(timeLineSeekBarProgressLow);//這也要記得改!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		input.add(1);
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		mainfrag.setSeekBarToRunBag(input);
		mCallback.seekBarStartTime(timeLineSeekBarProgressLow);
		mCallback.seekBarDuration(1);

		/*Add rmbutton*/
		/* 這邊先關掉(不能移掉任意一條戰術路徑)
		ImageView rmbutton = new ImageView(getActivity());
		LayoutParams rmbuttonlp = new LayoutParams(60,60);
		rmbuttonlp.setMargins(10, 20, 0, 0);
		rmbutton.setLayoutParams(rmbuttonlp);
		rmbutton.setImageResource(R.drawable.btn_delete);
		rmbutton.setId(seekBarId);
		Log.d("seekbar", "rmbutton ID = "+Integer.toString(seekBarId));
		rmbutton.setOnTouchListener(rmbuttonListener);
		playerrmbutton.addView(rmbutton);
		*/

	}

	//調整軌跡的rangeslider
	private RangeSlider.OnChangeListener rangesliderOnchange= new RangeSlider.OnChangeListener() {
		@Override
		public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
			Log.d("R Slider", "Change slider value"+slider.getValues());

			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			int progressHigh = (int)(float)(slider.getValues().get(1)); //To convert that object to a float, then to a int (cannot convert from lang.float to int directly)
			int progressLow = (int)(float)(slider.getValues().get(0));
			int duration =progressHigh-progressLow;
			Vector<Integer> input=new Vector();
			// 從sliderID反查是哪個runbag
			input.add(mainfrag.queryForRunbagIndex(slider.getId()));
			input.add(progressLow);
			input.add(duration);
			mainfrag.setSeekBarToRunBag(input);
			mCallback.seekBarStartTime((int)progressLow); //?
			mCallback.seekBarDuration(duration); //?

			if(progressHigh>progressHighest){ //下一次軌跡會從哪個progress開始
				progressHighest=progressHigh;
				mainfrag.setMainFragProLow(progressHighest);
				timeLineSeekBarProgressLow=progressHighest;
			}
			mainfrag.sortPathNumber();
		}
	};

	//從timeline overview跳到個別的timeline
	private OnClickListener turnToRespectiveTimeline= new OnClickListener() {
		@Override
		public void onClick(View v) {
			int whichTimeline=Integer.parseInt(v.getTag().toString());
			changeLayout(whichTimeline);
		}
	};

	private OnTouchListener rmbuttonListener = new OnTouchListener() {
		private int rmbutton_id=-1;
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) { // 判斷觸控的動作
				case MotionEvent.ACTION_DOWN:// 按下圖片時
					rmbutton_id=v.getId();
					MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
					mainfrag.removeOnePath(rmbutton_id);
					break;
			}
			return true;
		}
	};


	private MySeekBar.OnSeekBarChangeListener mySeekBarOnChange = new MySeekBar.OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(MySeekBar seekBar, double progressLow,
									  double progressHigh) {
			// TODO Auto-generated method stub
			int duration;
			duration = (int)progressHigh - (int)progressLow;
			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			/**
			 *
			 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
			 *
			 * **/
			timeLineSeekBarProgressLow =(int)progressHigh;
			Log.d("debug", "SeekBarId : "+Integer.toString(seekBar.getId())+"  set TimeLine_SeekBarProgressLow! "+Integer.toString(timeLineSeekBarProgressLow));

			Vector<Integer> input=new Vector();
			input.add(seekBar.getId());
			input.add((int)progressLow);
			input.add(duration);
			mainfrag.setSeekBarToRunBag(input);
			mCallback.seekBarStartTime((int)progressLow);
			mCallback.seekBarDuration(duration);

			//Log.d("debug", "OnChange! "+Integer.toString(input.get(0)));
		}

		@Override
		public void onProgressBefore() {
			// TODO Auto-generated method stub
		/*MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		mainfrag.setMainFragProLow(TimeLine_SeekBarProgressLow);
		Log.d("debug", "Progress before,TimeLine_SeekBarProgressLow = "+Integer.toString(TimeLine_SeekBarProgressLow));*/
		}

		@Override
		public void onProgressAfter() {
			// TODO Auto-generated method stub
			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			mainfrag.setMainFragProLow(timeLineSeekBarProgressLow);

			/*TODO �ˬd�Ƨ�*/
			mainfrag.sortPathNumber();
			//Log.d("debug", "Progress after,TimeLine_SeekBarProgressLow = "+Integer.toString(TimeLine_SeekBarProgressLow));
		}
	};

	private CircularControl.OnTouchListener directionOnTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			mainfrag.rotatePlayer((int) directionSeekBar.getDegree());
			return false;
		}
	};

	private CircularSeekBar.OnSeekChangeListener directionSeekBarOnChange= new OnSeekChangeListener() {

		@Override
		public void onProgressChange(CircularSeekBar view, int newProgress) {
			//Log.d("debug", "Progress:" + view.getProgress() + "/" + view.getMaxProgress());
			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			mainfrag.rotatePlayer(view.getProgress());
		}

	};

	public void setCircularSeekBarProgress(int input){
		directionSeekBar.setDegree(input);
	}

}
