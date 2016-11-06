package gseproject.core;

public enum ServiceType {
	
	HAS_BLOCK,
	IS_OCCUPIED,	
	I_OCCUPY,
	I_LEAVE,	
	TAKE_BLOCK,
	HAS_FINISHED_BLOCK,
	FINISH_BLOCK,
	//todo: this is the status of the block, it's simpler to parse an servicetype than an object
	GIVE_BLOCK_DIRTY,
	GIVE_BLOCK_CLEANED,
	GIVE_BLOCK_PAINTED,
	//info: if something can't be done
	NOPE
}
