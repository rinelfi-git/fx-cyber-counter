package mg.rinelfi.observation;

public interface Observable {
	void addObserver(Observer observer);
	void clearObservable();
}
