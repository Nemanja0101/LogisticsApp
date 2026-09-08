package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.LicenseType;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import com.zavrsniRad.LogisticsApp.domain.models.crew.License;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Aircraft;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Truck;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
public class TransportCompatibilityChecker {

    public void assertCapacity(List<ShipmentSegment> segments, Collection<Freight> freights){

        double totalWeight = freights.stream().mapToDouble(Freight::getWeightKg).sum();
        double totalVolume = freights.stream().mapToDouble(this::volumeM3).sum();

        for (ShipmentSegment s: segments){

            Double transportUnitMaxWeight = s.getTransportUnit().getMaxWeightKg();
            Double transportUnitMaxVolume = s.getTransportUnit().getCargoVolumeM3();

            if (totalWeight > transportUnitMaxWeight){
                throw new BusinessRuleViolationException("Segment with ID: " + s.getId() + " and sequence number : "
                        + s.getSequenceNumber() + " doesn't have enough weight capacity to carry the freight on this shipment",
                        new ArrayList<>(List.of(totalWeight + " kg > " + transportUnitMaxWeight + " kg")));
            }

            if (totalVolume > transportUnitMaxVolume) {
                throw new BusinessRuleViolationException("Segment with ID: " + s.getId() + " and sequence number : "
                        + s.getSequenceNumber() + " doesn't have enough volume capacity to carry the freight on this shipment",
                        new ArrayList<>(List.of(totalVolume + " m3 > " + transportUnitMaxVolume + " m3")));
            }
        }

    }

    public void assertCapacity(List<ShipmentSegment> segments, Collection<Freight> existing, Freight incoming){

        assertCapacity(segments, Stream.concat(existing.stream(), Stream.of(incoming)).toList());

    }

    private double volumeM3(Freight freight){

        return (freight.getHeightCm() * freight.getWidthCm() * freight.getLengthCm()) / 1_000_000.0;

    }

    public void assertUnitCanCarry(TransportUnit unit, Collection<Freight> freights){

        for (Freight freight : freights){

            Set<Capability> required = EnumSet.noneOf(Capability.class);
            required.addAll(freight.getRequirements());
            required.addAll(freight.getForm().requiredCapabilities());

            if (!unit.getCapabilities().containsAll(required)){

                Set<Capability> missing = EnumSet.noneOf(Capability.class);
                missing.addAll(freight.getRequirements());
                missing.addAll(required);
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

    public void assertCapabilitiesValidForUnitType(TransportUnitType unitType, Collection<Capability> capabilities){

        if (capabilities == null || capabilities.isEmpty()){
            return;
        }

        Set<Capability> invalid = EnumSet.noneOf(Capability.class);

        for (Capability capability : capabilities){
            if (!capability.allowedTransportUnitTypes().contains(unitType)){
                invalid.add(capability);
            }
        }

        if (!invalid.isEmpty()){
            throw new BusinessRuleViolationException(
                    "Capabilities not applicable to a " + unitType,
                    invalid.stream().map(Enum::name).toList()
            );
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

    public void assertCrewMemberIsDangerousGoodsCertified(License license, Collection<Freight> freights){

        for (Freight freight : freights){

            if (freight.getRequirements().contains(Capability.DANGEROUS_GOODS) && !license.isDangerousGoodsCertified()){
                throw new BusinessRuleViolationException(
                        "Freight " + freight.getName() + " requires a crew member certified for dangerous goods",
                        List.of(Capability.DANGEROUS_GOODS.name())
                );
            }

        }
    }

}
