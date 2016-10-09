package gseproject.interaction;

public interface ISensor<IState> {

	void init();

	IState updateStatement();
}
