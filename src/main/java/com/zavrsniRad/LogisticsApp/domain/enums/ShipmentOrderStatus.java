package com.zavrsniRad.LogisticsApp.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum ShipmentOrderStatus {
    PENDING {
        @Override public Set<ShipmentOrderStatus> allowedNext(){
            return EnumSet.of(CONFIRMED, CANCELLED, ON_HOLD);
        }
    },
    CONFIRMED {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.of(ASSIGNED, CANCELLED, ON_HOLD);
        }
    },
    ASSIGNED {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.of(IN_TRANSIT, CANCELLED, ON_HOLD);
        }
    },
    IN_TRANSIT {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.of(PARTIALLY_DELIVERED, DELIVERED, ON_HOLD);
        }
    },
    PARTIALLY_DELIVERED {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.of(DELIVERED, ON_HOLD);
        }
    },
    DELIVERED {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.noneOf(ShipmentOrderStatus.class);
        }
    },
    CANCELLED {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.noneOf(ShipmentOrderStatus.class);
        }
    },
    ON_HOLD {
        @Override
        public Set<ShipmentOrderStatus> allowedNext() {
            return EnumSet.of(CONFIRMED, ASSIGNED, IN_TRANSIT, PARTIALLY_DELIVERED, CANCELLED);
        }
    };

    public abstract Set<ShipmentOrderStatus> allowedNext();

    public boolean canTransitionTo(ShipmentOrderStatus target) {
        return allowedNext().contains(target);
    }

}
