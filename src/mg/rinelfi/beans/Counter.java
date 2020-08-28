package mg.rinelfi.beans;

import mg.rinelfi.observation.*;

import java.util.ArrayList;

public class Counter implements ValueChangedNotifier, IntObservable, Runnable {
	private int tarif, minimal, montant, pourcentagePlafond, plafond;
	private long seconds;
	private boolean plafondDefinis, counting;
	private final ArrayList<Observer> observers = new ArrayList<>();
	
	public Counter() {
		tarif = 0;
		minimal = 0;
		montant = 0;
		pourcentagePlafond = 0;
		plafond = 0;
		seconds = 0;
		plafondDefinis = false;
		counting = false;
	}
	
	public long getSeconds() {
		return seconds;
	}
	
	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
	
	public int getTarif() {
		return tarif;
	}
	
	public void setTarif(int tarif) {
		this.tarif = tarif;
	}
	
	public int getMinimal() {
		return minimal;
	}
	
	public void setMinimal(int minimal) {
		this.minimal = minimal;
	}
	
	public int getMontant() {
		return montant;
	}
	
	public void setMontant(int montant) {
		this.montant = montant;
	}
	
	public int getPourcentagePlafond() {
		return pourcentagePlafond;
	}
	
	public void setPourcentagePlafond(int pourcentagePlafond) {
		this.pourcentagePlafond = pourcentagePlafond;
	}
	
	public int getPlafond() {
		return plafond;
	}
	
	public void setPlafond(int plafond) {
		this.plafond = plafond;
	}
	
	public boolean isPlafondDefinis() {
		return plafondDefinis;
	}
	
	public void setPlafondDefinis(boolean plafondDefinis) {
		this.plafondDefinis = plafondDefinis;
	}
	
	public void stop() {
		counting = false;
		tarif = 0;
		minimal = 0;
		montant = 0;
		pourcentagePlafond = 0;
		plafond = 0;
		seconds = 0;
		plafondDefinis = false;
		notifyConsumer();
		update(0);
	}
	
	public void pause() {
		counting = false;
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
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	@Override
	public void clearObservable() {
		observers.clear();
	}
	
	@Override
	public void run() {
		counting = true;
		while (counting) {
			int toMinute = (int) getSeconds() / 60;
			setMontant(toMinute * getTarif());
			if (isPlafondDefinis()) {
				long secondesTotal = getPlafond() * 60 / getTarif();
				int percentage = (int) (getSeconds() * 100 / secondesTotal);
				update(percentage <= 100 ? percentage : 100);
			} else update(0);
			notifyConsumer();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(int value) {
		observers.forEach(consumer -> {
			if (consumer instanceof IntObserver) {
				((IntObserver) consumer).update(value);
			}
		});
	}
}
