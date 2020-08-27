package mg.rinelfi.beans;

import mg.rinelfi.observation.Observer;
import mg.rinelfi.observation.ValueChangedListener;
import mg.rinelfi.observation.ValueChangedNotifier;

import java.util.ArrayList;

public class Counter implements ValueChangedNotifier, Runnable {
	private int tarif, minimal, montant, pourcentagePlafond, plafond, minutes;
	private boolean plafondDefinis, counting;
	private final ArrayList<Observer> observers = new ArrayList<>();
	
	public Counter() {
		tarif = 0;
		minimal = 0;
		montant = 0;
		pourcentagePlafond = 0;
		plafond = 0;
		minutes = 0;
		plafondDefinis = false;
		counting = false;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public void setMinutes(int minutes) {
		this.minutes = minutes;
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
	
	@Override
	public void notifyConsumer() {
		observers.forEach(consumer -> {
			if(consumer instanceof ValueChangedListener) {
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
		while(counting) {
			setMontant(getMinutes() * getTarif());
			notifyConsumer();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		counting = false;
		tarif = 0;
		minimal = 0;
		montant = 0;
		pourcentagePlafond = 0;
		plafond = 0;
		minutes = 0;
		plafondDefinis = false;
		notifyConsumer();
	}
}
