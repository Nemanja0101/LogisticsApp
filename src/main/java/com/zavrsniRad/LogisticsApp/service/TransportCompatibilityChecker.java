package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.LicenseType;
import com.zavrsniRad.LogisticsApp.domain.models.crew.License;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Truck;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class TransportCompatibilityChecker {

    public void assertUnitCanCarry(TransportUnit unit, Collection<Freight> freights){

        for (Freight freight : freights){

            if (!unit.getCapabilities().containsAll(freight.getRequirements())){

                Set<Capability> missing = EnumSet.noneOf(Capability.class);
                missing.addAll(freight.getRequirements());
                missing.removeAll(unit.getCapabilities());

                throw new BusinessRuleViolationException(
                        "Transport unit lacks capabilities required by freight " + freight.getName(),
                        missing.stream().map(Enum::name).toList()
                );
            }

            if (unit instanceof Truck truck
                    && !freight.getForm().isCompatible(truck.getTrailerType())) {

                throw new BusinessRuleViolationException(
                        "Freight " + freight.getName() + " (" + freight.getForm() + ") can't travel in a "
                                + truck.getTrailerType() + " trailer",
                        freight.getForm().compatibleTrailerType().stream().map(Enum::name).toList()
                );
            }

        }
    }

    public void assertCrewMemberLicenseValidForSegmentTransportType(LicenseType licenseType, ShipmentSegment shipmentSegment){

        TransportUnitType unitType = shipmentSegment.getTransportUnit().getType();

        if (!licenseType.canOperate(unitType)) {
            throw new BusinessRuleViolationException(
                    "License " + licenseType + " does not permit operating a " + unitType,
                    licenseType.allowedTransportUnitTypes().stream().map(Enum::name).toList()
            );
        }

    }

    public void assertCrewMemberIsAdrCertified(License license, Collection<Freight> freights){

        for (Freight freight : freights){

            if (freight.getRequirements().contains(Capability.ADR) && !license.isAdrCertified()){
                throw new BusinessRuleViolationException(
                        "Freight " + freight.getName() + " requires an ADR certified crew member",
                        List.of(Capability.ADR.name())
                );
            }

        }
    }

}
