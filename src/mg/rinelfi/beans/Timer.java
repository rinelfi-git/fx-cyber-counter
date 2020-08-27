package mg.rinelfi.beans;

import mg.rinelfi.observation.*;

import java.util.ArrayList;

public class Timer implements Observable, ValuesChangedNotifier, Runnable {
	private final ArrayList<Observer> observers = new ArrayList<>();
	private long timestamp;
	private int second, minute, hour, day;
	private boolean computing;
	
	public Timer() {
		timestamp = 0L;
		second = 0;
		minute = 0;
		hour = 0;
		day = 0;
	}
	
	public Timer(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getSecond() {
		return second;
	}
	
	public String getFormatedSecond() {
		String tmp = "0" + String.valueOf(getSecond());
		return tmp.substring(tmp.length() - 2, tmp.length());
	}
	
	public void setSecond(int second) {
		this.second = second;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public String getFormatedMinute() {
		String tmp = "0" + String.valueOf(getMinute());
		return tmp.substring(tmp.length() - 2, tmp.length());
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public String getFormatedHour() {
		String tmp = "0" + String.valueOf(getHour());
		return tmp.substring(tmp.length() - 2, tmp.length());
	}
	
	public int getDay() {
		return day;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	@Override
	public void clearObservable() {
		observers.clear();
	}
	
	@Override
	public void notifyConsumer() {
		observers.forEach(consumer -> {
			if (consumer instanceof ValueChangedListener) {
				((ValueChangedListener) consumer).getNotified();
			}
		});
	}
	
	@Override
	public void run() {
		computing = true;
		while (computing) {
			setSecond(convertSecond());
			setMinute(convertMinute());
			setHour(convertHour());
			setDay(convertDay());
			notifyConsumer();
			timestamp++;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pause() {
		computing = false;
	}
	
	public void stop() {
		computing = false;
		timestamp = 0L;
		setSecond(convertSecond());
		setMinute(convertMinute());
		setHour(convertHour());
		setDay(convertDay());
		notifyConsumer();
	}
	
	int convertMiliseconds() {
		return (int) timestamp % 1000;
	}
	
	int convertSecond() {
		return (int) (timestamp / 1000) % 60;
	}
	
	int convertMinute() {
		return (int) (timestamp / 1000 / 60) % 60;
	}
	
	int convertHour() {
		return (int) (timestamp / 1000 / 60 / 60) % 24;
	}
	
	int convertDay() {
		return (int) (timestamp / 1000 / 60 / 60 / 24) % 30;
	}
}
