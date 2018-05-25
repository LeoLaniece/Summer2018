package javaServer;

import test.Draw2Model;

public class SecondStageThread extends Thread{
	public Draw2Model model;
	public SecondStageThread() {}
	
	
	public void run() {
		SecondStage s2 = new SecondStage();
		model = s2.m;
	}

}
