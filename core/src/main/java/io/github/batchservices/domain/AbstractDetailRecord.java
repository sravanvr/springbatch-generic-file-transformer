package io.github.batchservices.domain;

/***
 *
 * Acts just like a Marker interfaces. Serves as a super type for all "detail record" entities such as Customer, Cd, Checking, etc...
 * This is needed to make CustomReader and AbstractItemWriter classes to be generic.
 */
public abstract class AbstractDetailRecord extends AbstractEntity{
		
}
