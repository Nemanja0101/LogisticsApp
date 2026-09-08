package com.zavrsniRad.LogisticsApp.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Capability {
    TEMPERATURE_CONTROLLED{
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.allOf(TransportUnitType.class);
        }
    },
    DANGEROUS_GOODS{
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.allOf(TransportUnitType.class);
        }
    },
    LIQUID_REQ{
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.TRUCK, TransportUnitType.VESSEL, TransportUnitType.RAILCAR);
        }
    },
    CONTAINERISED{
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.allOf(TransportUnitType.class);
        }
    };

    public abstract Set<TransportUnitType> allowedTransportUnitTypes();

}