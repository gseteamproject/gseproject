package gseproject.infrastructure.contracts;

import gseproject.core.ServiceType;

public class ServiceTypeContract {

    @Override
    public String toString() {
	return "ServiceTypeContract [serviceType=" + serviceType + "]";
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ServiceTypeContract other = (ServiceTypeContract) obj;
	if (serviceType != other.serviceType)
	    return false;
	return true;
    }

    public ServiceType serviceType;

}
