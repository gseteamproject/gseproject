package gseproject.infrastructure.interaction;

public interface ISensor<IState> {

	void init();

	IState updateStatement();
}
