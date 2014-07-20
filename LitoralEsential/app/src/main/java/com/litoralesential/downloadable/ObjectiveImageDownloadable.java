package com.litoralesential.downloadable;

import com.litoralesential.Objective;

/**
 * Created by Korbul on 7/20/2014.
 */
public class ObjectiveImageDownloadable extends Downloadable {
	private Objective objective;

	public ObjectiveImageDownloadable() {
		setType(FlowType.OBJECTIVE_IMAGE);
	}

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}
}
