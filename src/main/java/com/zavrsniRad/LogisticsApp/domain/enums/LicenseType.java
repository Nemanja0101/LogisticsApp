package com.zavrsniRad.LogisticsApp.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum LicenseType {

    B {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VAN);
        }
    },
    C1 {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VAN, TransportUnitType.TRUCK);
        }
    },
    C {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VAN, TransportUnitType.TRUCK);
        }
    },
    C1E {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VAN, TransportUnitType.TRUCK);
        }
    },
    CE {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VAN, TransportUnitType.TRUCK);
        }
    },

    MASTER_MARINER {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VESSEL);
        }
    },
    DECK_OFFICER {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VESSEL);
        }
    },
    ABLE_SEAFARER {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.VESSEL);
        }
    },

    TRAIN_DRIVER_A {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.RAILCAR);
        }
    },
    TRAIN_DRIVER_B {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return EnumSet.of(TransportUnitType.RAILCAR);
        }
    },
    PILOT {
        @Override
        public Set<TransportUnitType> allowedTransportUnitTypes() {
            return Set.of(TransportUnitType.AIRCRAFT);
        }
    }
    ;


    public abstract Set<TransportUnitType> allowedTransportUnitTypes();

    public boolean canOperate(TransportUnitType transportUnitType) {
        return allowedTransportUnitTypes().contains(transportUnitType);
    }

}
