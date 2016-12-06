# android-empty
Changes added before first (non-setup) commit: 

Main app activity uses a FragmentTabHost to hold and switch between fragments.

Fragments include: 
	History Fragment (just text)
	Turbine Fragment (
		an image of the turbine rotating,
		a compass that turns to match wind direction,
		text boxes for wind speed, direction, and power output.
		)
	Details Fragment (
		a place to put a visualization of power production,
		a graph that displays power output over a recent period.
		}
	Project Fragment (Placeholder for future plans, may remove)

There is also a TurbineData singleton class for holding turbine data.
