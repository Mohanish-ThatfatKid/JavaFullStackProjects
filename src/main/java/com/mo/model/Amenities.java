package com.mo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amenities {

		private boolean pickUpDrop;
		private boolean wifi;
		private boolean swimmingPool;
		private boolean freeBreakfast;
		private boolean garden;
		private boolean playArea;
}
